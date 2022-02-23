package com.littleo.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.service.LoginService;
import com.littleo.blog.service.SysUserService;
import com.littleo.blog.utils.JWTUtils;
import com.littleo.blog.vo.ErrorCode;
import com.littleo.blog.vo.params.LoginParam;
import com.littleo.blog.vo.params.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 * @author 欧阳巍
 * @Date 2021/12/31 16:33
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    @Lazy
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 1.检查参数是否合法
     * 2.根据用户名和密码查询去user表中查询是否存在
     * 3.如果不存在则登录失败
     * 4.如果存在。使用jwt生成token返回给前端
     * 5.token放入redis当中，生成user:token信息，设置过期时间（登录认证的时候先认证token是否合法，去redis是否存在）
     * @param loginParam
     * @return
     */
    //加密盐
    private static final String salt = "oylz@!~";

    //登录
    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        String pwd = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account,pwd);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser));
        return Result.success(token);
    }


    //校验token
    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        //解析失败
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }


    //退出
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);

        return Result.success(null);
    }


    //注册
    @Override
    public Result register(LoginParam loginParam) {
        /*
        * 1.判断账户是否合法
        * 2.判断账户是否已经被注册
        * 3.若通过注册则返回token，存入redis
        * 4.加上事务，如果中间出现错误则回滚
        * */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        //判断账号是否存在

        //若存在
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        //不存在
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/user/user_1.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        //token
        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser));
        return Result.success(token);

    }
}

package com.littleo.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.littleo.blog.dao.mapper.SysUserMapper;
import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.service.LoginService;
import com.littleo.blog.service.SysUserService;
import com.littleo.blog.utils.JWTUtils;
import com.littleo.blog.vo.ErrorCode;
import com.littleo.blog.vo.LoginUserVo;
import com.littleo.blog.vo.UserVo;
import com.littleo.blog.vo.params.Result;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 欧阳巍
 * @Date 2021/12/28 9:53
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    @Lazy
    private LoginService loginService;




    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("littleo");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");


        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /*
        * 1.校验token合法性:是否为空？解析是否成功？redis是否有缓存
        * 2.如果校验失败返回错误
        * 3.如果成功，返回对应结果
        * */

        SysUser sysUser = loginService.checkToken(token);
        if (sysUser==null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");


        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/user/user_2.png");
            sysUser.setNickname("游客");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);

        return userVo;
    }
}

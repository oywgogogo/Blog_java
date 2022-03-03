package com.littleo.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.littleo.blog.dao.mapper.ArticleMapper;
import com.littleo.blog.dao.mapper.CommentMapper;
import com.littleo.blog.pojo.Article;
import com.littleo.blog.pojo.Comment;
import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.service.CommentService;
import com.littleo.blog.service.SysUserService;
import com.littleo.blog.utils.UserThreadLocal;
import com.littleo.blog.vo.CommentVo;
import com.littleo.blog.vo.UserVo;
import com.littleo.blog.vo.params.CommentParam;
import com.littleo.blog.vo.params.Result;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 欧阳巍
 * @Date 2022/2/15 16:24
 */

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleMapper articleMapper;


    //根据文章id查询评论列表
    @Override
    public Result commentsByArticleId(Long id) {
        /*
        * 1,根据文章id查询评论列表从 comment表中查询
        * 2.根据作者的d查询作者的信息
        * 3.判断如果1eve1=1要去查询它有没有子评论
        * 4.如果有根据评论d进行查询( parent id)
        * */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);

        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);

        Article article = articleMapper.selectById(commentParam.getArticleId());
        int commentCount = article.getCommentCounts();
        Article article1 = new Article();
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        article1.setCommentCounts(commentCount+1);
        updateWrapper.eq(Article::getCommentCounts,article.getCommentCounts());
        updateWrapper.eq(Article::getId,commentParam.getArticleId());
        articleMapper.update(article1,updateWrapper);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment: comments){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        Long authorId = comment.getAuthorId();
        //评论者信息
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        commentVo.setAuthor(userVo);
        //评论的评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if (comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }
}

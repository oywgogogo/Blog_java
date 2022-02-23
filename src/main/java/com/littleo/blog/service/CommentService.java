package com.littleo.blog.service;

import com.littleo.blog.vo.params.CommentParam;
import com.littleo.blog.vo.params.Result;

/**
 * @author 欧阳巍
 * @Date 2022/2/15 16:24
 */
public interface CommentService {
    //根据文章id查询评论列表
    Result commentsByArticleId(Long id);


    //评论功能
    Result comment(CommentParam commentParam);
}

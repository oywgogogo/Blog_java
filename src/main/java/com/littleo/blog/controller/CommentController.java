package com.littleo.blog.controller;

import com.littleo.blog.service.CommentService;
import com.littleo.blog.vo.params.CommentParam;
import com.littleo.blog.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 欧阳巍
 * @Date 2022/2/15 16:16
 */
@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentService.commentsByArticleId(id);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}

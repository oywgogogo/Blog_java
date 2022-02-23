package com.littleo.blog.controller;

import com.littleo.blog.service.TagService;
import com.littleo.blog.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 欧阳巍
 * @Date 2021/12/28 14:51
 */
@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    TagService tagService;
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }


    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}

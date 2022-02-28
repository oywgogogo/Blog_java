package com.littleo.blog.controller;


import com.littleo.blog.common.aop.LogAnnotation;
import com.littleo.blog.common.cache.Cache;
import com.littleo.blog.service.ArticleService;
import com.littleo.blog.vo.params.ArticleParam;
import com.littleo.blog.vo.params.ArticleVo;
import com.littleo.blog.vo.params.Result;
import com.littleo.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    //首页，文章列表
    @Autowired
    ArticleService articleService;
    @PostMapping
    @LogAnnotation(module = "文章列表",operator = "获取文章列表")
    public Result listArtilces(@RequestBody PageParams params){
        return articleService.listArticle(params);
    }

    //最热文章
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArtilces(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    //最新文章
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000,name = "new_article")
    public Result newArtilces(){
        int limit = 5;
        return articleService.newArticle(limit);
    }

    //文章归档
    @PostMapping("listArchives")
    public Result listArchive(){
        return articleService.listArchive();
    }


    //文章详情
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long ArticleId) {
        ArticleVo articleVo = articleService.findArticleById(ArticleId);

        return Result.success(articleVo);
    }


    //发布文章
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

    //modify
    //查看需要的修改文章
    @PostMapping("{id}")
    public Result modify(@PathVariable("id") Long id){
        return articleService.modify(id);
    }


    //发修改文章
    @PostMapping("update")
    public Result update(@RequestBody ArticleParam articleParam){
        return articleService.update(articleParam);
    }

}

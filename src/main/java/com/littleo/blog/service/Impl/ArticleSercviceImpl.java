package com.littleo.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littleo.blog.dao.mapper.ArticleBodyMapper;
import com.littleo.blog.dao.mapper.ArticleMapper;
import com.littleo.blog.dao.mapper.ArticleTagMapper;
import com.littleo.blog.dos.Archives;
import com.littleo.blog.pojo.*;
import com.littleo.blog.service.*;
import com.littleo.blog.utils.UserThreadLocal;
import com.littleo.blog.vo.ArticleBodyVo;
import com.littleo.blog.vo.CategoryVo;
import com.littleo.blog.vo.params.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 欧阳巍
 * @Date 2021/12/24 17:35
 */
@Service
public class ArticleSercviceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;



    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPagesize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));

    }


   /* @Override
    public Result listArticle(PageParams params) {
        *//**
         * 分页查询article数据表
         *//*
        Page<Article> page = new Page<>(params.getPage(),params.getPagesize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        //查询文章的参数 加上分类id，判断不为空 加上分类条件
        if (params.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId,params.getCategoryId());
        }
        //是否置顶排序
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page,queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(records,true,true);

        return Result.success(articleVoList);
    }
*/

   /* @Override
    public Result listArticle(PageParams pageParams) {
        *//**
         * 1. 分页查询 article数据库表
         *//*
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }

        //是否置顶进行排序
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        //能直接返回吗？ 很明显不能
        List<ArticleVo> articleVoList = copyList(records,true,true);
        return Result.success(articleVoList);
    }*/
    /**
     * 最新文章
     * @param limit
     * @return
     */

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        //select id,title from Article order by creat_date desc limit 5;
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);

        List<Article> articleList = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articleList,false,false));
    }

    //最热文章
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        //select id,title from Article order by view_counts desc limit 5;
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);

        List<Article> articleList = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articleList,false,false));
    }
    /**
     * 文章归档
     * @return
     */
    @Override
    public Result listArchive() {

        List<Archives> archivesList = articleMapper.listArchive();
        return Result.success(archivesList);
    }

    /**
     * 查询文章详情
     *
     * */

    @Autowired
    private ThreadService threadService;
    @Override
    public ArticleVo findArticleById(Long ArticleId) {
        /**
         * 根据文章id查询文章内容
         * 根据bodyid和categoryid做关联查询
         * */

        Article article = this.articleMapper.selectById(ArticleId);
        ArticleVo articleVo  = copy(article,true,true,true,true);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        SysUser sysUser = sysUserService.findUserById(articleVo.getAuthorId());
        articleVo.setAvatar(sysUser.getAvatar());
        threadService.updateArticleViewCount(articleMapper,article);
        return articleVo;
    }


    //发布文章
   /* @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }
*/
    //发布文章
    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {


        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
//        article.setBodyId();
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }


    //查看需要的修改文章
    @Override
    public Result modify(Long id) {
        Article article = this.articleMapper.selectById(id);
        CategoryVo categoryVo = this.categoryService.findCategoryById(article.getCategoryId());
        List<TagVo> tags = this.tagService.findTagsByArticleId(article.getId());
        ArticleParam articleParam = new ArticleParam();
        ArticleBody articleBody = this.articleBodyMapper.selectById(article.getBodyId());
        ArticleBodyParam articleBodyParam = new ArticleBodyParam();
        BeanUtils.copyProperties(articleBody,articleBodyParam);
        articleParam.setId(article.getId());
        articleParam.setCategory(categoryVo);
        articleParam.setTags(tags);
        articleParam.setSummary(article.getSummary());
        articleParam.setTitle(article.getTitle());
        articleParam.setBody(articleBodyParam);
        articleParam.setBodyId(article.getBodyId());
        return Result.success(articleParam);
    }

    /**
     * 修改文章
     * @param articleParam
     * @return
     */
    @Override
    public Result update(ArticleParam articleParam) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(Article::getId,articleParam.getId());
        updateWrapper.set(Article::getSummary,articleParam.getSummary());
        updateWrapper.set(Article::getTitle,articleParam.getTitle());
        Integer rows = articleMapper.update(null,updateWrapper);
        System.out.println(rows+"-------------------------------");
        LambdaUpdateWrapper<ArticleBody> bodyUpdateWrapper = new LambdaUpdateWrapper();
        bodyUpdateWrapper.eq(ArticleBody::getId,articleParam.getBodyId());
        bodyUpdateWrapper.set(ArticleBody::getContent,articleParam.getBody().getContent());
        bodyUpdateWrapper.set(ArticleBody::getContentHtml,articleParam.getBody().getContentHtml());
        Integer body_row = articleBodyMapper.update(null,bodyUpdateWrapper);
        System.out.println(body_row+"----------------------------------");
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleParam.getId());
        int tag_row  = articleTagMapper.delete(queryWrapper);
        System.out.println(tag_row+"-----------------------------");
        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleParam.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(articleParam.getId());
        return Result.success(articleVo);
    }




    //复制list型参数
    private List<ArticleVo> copyList(List<Article> records,boolean isTag, boolean isAuthor){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }


    //重载copylist
    private List<ArticleVo> copyList(List<Article> records,boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    @Autowired
    private CategoryService categoryService;


    //复制pojo实体类到vo上
    private ArticleVo copy(Article article,boolean isTag, boolean isAuthor, boolean isBody, boolean  isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //判断是否需要标签和作者信息
        if(isTag){
            String articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            String bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }



    //查看文章
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(String bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}

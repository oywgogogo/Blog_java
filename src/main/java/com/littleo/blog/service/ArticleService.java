package com.littleo.blog.service;

import com.littleo.blog.vo.params.ArticleParam;
import com.littleo.blog.vo.params.ArticleVo;
import com.littleo.blog.vo.params.Result;
import com.littleo.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     *
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */

    Result newArticle(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchive();

    /**
    * 文章详情
    */
    ArticleVo findArticleById(Long ArticleId);

    /**
    * 发布文章
    *
    * */
    Result publish(ArticleParam articleParam);


    Result modify(Long id);

    /**
     * 修改文章
     * @param articleParam
     * @return
     */
    Result update(ArticleParam articleParam);
}

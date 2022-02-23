package com.littleo.blog.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.littleo.blog.dao.mapper.ArticleMapper;
import com.littleo.blog.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        try {
            int viewCounts = article.getViewCounts();
            Article articleupdate = new Article();
            articleupdate.setViewCounts(viewCounts+1);
            LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(Article::getViewCounts,article.getViewCounts());
            updateWrapper.eq(Article::getId,article.getId());
            articleMapper.update(articleupdate,updateWrapper);
            //updae article set viewcount = 100 where viewcount = 99 and id = ***
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
            System.out.println("已更新");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

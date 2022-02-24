package com.littleo.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littleo.blog.dos.Archives;
import com.littleo.blog.pojo.Article;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchive();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
}

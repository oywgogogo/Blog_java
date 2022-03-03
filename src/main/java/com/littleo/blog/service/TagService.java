package com.littleo.blog.service;

import com.littleo.blog.vo.params.Result;
import com.littleo.blog.vo.params.TagVo;

import java.util.List;

/**
 * @author 欧阳巍
 * @Date 2021/12/27 17:40
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(String articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}

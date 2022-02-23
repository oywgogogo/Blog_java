package com.littleo.blog.service;

import com.littleo.blog.vo.CategoryVo;
import com.littleo.blog.vo.params.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
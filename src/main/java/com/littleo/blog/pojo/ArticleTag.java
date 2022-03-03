package com.littleo.blog.pojo;

import lombok.Data;

@Data
public class ArticleTag {

    private Long id;

    private String articleId;

    private Long tagId;
}
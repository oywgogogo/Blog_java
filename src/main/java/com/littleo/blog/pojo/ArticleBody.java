package com.littleo.blog.pojo;

import lombok.Data;

@Data
public class ArticleBody {

    private String id;
    private String content;
    private String contentHtml;
    private String articleId;
}
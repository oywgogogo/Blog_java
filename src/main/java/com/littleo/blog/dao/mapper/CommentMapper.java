package com.littleo.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littleo.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 欧阳巍
 * @Date 2022/2/15 16:40
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}

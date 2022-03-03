package com.littleo.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littleo.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 欧阳巍
 * @Date 2021/12/27 17:46
 */

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(String articleId);

    /**
     * 查询最热标签
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}

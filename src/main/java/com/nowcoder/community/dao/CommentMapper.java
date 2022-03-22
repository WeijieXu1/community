package com.nowcoder.community.dao;

import com.nowcoder.community.util.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xuweijie
 * @create 2022-03-22 17:35
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(int entityId,int entityType, int offset,int limit);
    int selectCountByEntity(int entityId,int entityType);
}
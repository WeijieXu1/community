package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.util.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuweijie
 * @create 2022-03-22 18:05
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentsByEntity(int entityId, int entityType,
                                              int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityId,entityType,offset,limit);

    }

    public int findCommentCount(int entityId,int entityType){

        return commentMapper.selectCountByEntity(entityId,entityType);
    }
}

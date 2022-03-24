package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.util.Comment;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author xuweijie
 * @create 2022-03-22 18:05
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityId, int entityType,
                                              int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityId,entityType,offset,limit);

    }

    public int findCommentCount(int entityId,int entityType){

        return commentMapper.selectCountByEntity(entityId,entityType);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if(comment==null)
            throw new IllegalArgumentException("参数不能为空！");
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        //更新帖子评论数量
        if(comment.getEntityType()==ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityId(), comment.getEntityType());
            discussPostService.updateCommentCount(comment.getEntityId(),count+1);


        }


        return rows;
    }
}

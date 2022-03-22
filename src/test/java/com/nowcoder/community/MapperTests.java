package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;


/**
 * @author xuweijie
 * @create 2022-03-17 18:10
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);

    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setEmail("QQ");
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://www.nowcoder.com/101.png");

        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }
    @Test
    public void testUpdate(){
        int i = userMapper.updateHeader(150,"www.baidu.com");
        System.out.println(i);

    }
    @Test
    public void testSelectPost(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost post:discussPosts){
            System.out.println(post);
        }
        int i = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(i);


    }
    @Test
    public void testInsertLoginTicket(){
        LoginTicket lt = new LoginTicket();
        lt.setUserId(101);
        lt.setTicket("abc");
        lt.setStatus(0);
        lt.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicketMapper.insertLoginTicket(lt);

    }
    @Test
    public void testSelectLoginTicket(){
        LoginTicket abc = loginTicketMapper.selectByTicket("abc");
        System.out.println(abc);
        loginTicketMapper.updateStatus("abc",1);
    }

}

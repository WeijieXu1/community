package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuweijie
 * @create 2022-04-08 9:54
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SpringBootTest {
    @Autowired
    private DiscussPostService discussPostService;

    private DiscussPost data;

    @Before
    public void before(){
        data= new DiscussPost();
        data.setUserId(111);
        data.setTitle("Test Title");
        data.setContent("Test content");
        discussPostService.addDiscussPost(data);

    }

    @After
    public void after(){
        discussPostService.updateStatus(data.getId(),2);
    }

    @Test
    public void testFindById(){
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assert.assertNotNull(post);
        Assert.assertEquals(data.getTitle(),post.getTitle());
        Assert.assertEquals(data.getContent(),post.getContent());
    }
    @Test
    public void testUpdateScore(){
        int rows = discussPostService.updateScore(data.getId(), 2000.00);
        Assert.assertEquals(1,rows);
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assert.assertEquals(2000.00,post.getScore(),2);

    }



}

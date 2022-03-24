package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

/**
 * @author xuweijie
 * @create 2022-03-24 16:51
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey="test:count";
        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));

        System.out.println(redisTemplate.opsForValue().increment(redisKey));

    }
    //编程式事务
    @Test
    public void testTransactional(){
        Object obj=redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey="text:ts";
                operations.multi();
                operations.opsForSet().add(redisKey,"zhangsan");
                operations.opsForSet().add(redisKey,"lisi");
                operations.opsForSet().add(redisKey,"wangwu");
                System.out.println(operations.opsForSet().members(redisKey));

                return operations.exec();

            }

        });
        System.out.println(obj);
    }
}

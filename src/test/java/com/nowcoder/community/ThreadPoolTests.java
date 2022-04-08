package com.nowcoder.community;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * @author xuweijie
 * @create 2022-04-04 9:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTests {
    private static final Logger logger= LoggerFactory.getLogger(ThreadPoolTests.class);
    //JDK普通线程池
    private ExecutorService executorService= Executors.newFixedThreadPool(5);
    //JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(5);
    //spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;



    //spring可执行定时任务线程池
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //jdk普通线程池
    @Test
    public void testExecutorService(){
        Runnable task=new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");


            }
        };
        for (int i = 0; i <10 ; i++) {
            executorService.submit(task);

        }
        sleep(10000);
    }
    @Test
    public void testScheduledExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ScheduledExecutorService");


            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task,10000,1000, TimeUnit.MILLISECONDS);
        sleep(30000);
    }
    //spring普通线程池
    @Test
    public void testThreadPoolTaskExceutor(){
        Runnable task=new Runnable() {
            @Override
            public void run() {
                logger.debug("hello threadPoolTaskExecutor");

            }
        };
        for (int i = 0; i <10 ; i++) {
            threadPoolTaskExecutor.submit((task));

        }
        sleep(10000);

    }







}






































package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xuweijie
 * @create 2022-04-04 11:46
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}

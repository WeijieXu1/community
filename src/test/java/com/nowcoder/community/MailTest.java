package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author xuweijie
 * @create 2022-03-19 9:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("2544199309@qq.com","TEST","hello dog!");
    }
    @Test
    public void testHtml(){
        Context context = new Context();
        context.setVariable("username","weijie");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("1904016345@qq.com","HTML",content);
    }
}

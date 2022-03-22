package com.nowcoder.community;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.HtmlUtils;

/**
 * @author xuweijie
 * @create 2022-03-21 20:29
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text="<script>咕咕咕</script>";
        text=HtmlUtils.htmlEscape(text);
        System.out.println(text);
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }
}

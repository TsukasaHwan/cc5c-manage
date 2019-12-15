package com.cc5c;

import com.cc5c.utils.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Cc5cApplicationTests {
    @Autowired
    private MailUtil mailUtil;

    @Test
    public void contextLoads() {
        try {
            mailUtil.sendMailWithText("啦啦啦，see you 111", "你好哟1111111111111111111", "785415580@qq.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("11111");
    }

}

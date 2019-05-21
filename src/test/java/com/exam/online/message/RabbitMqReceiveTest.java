package com.exam.online.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-05-21 15:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqReceiveTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendMessage(){
        amqpTemplate.convertAndSend("myQueue", "测试发送消息");
    }

    public static void main(String[] args) {
        new RabbitMqSend().sendMessage();
    }
}
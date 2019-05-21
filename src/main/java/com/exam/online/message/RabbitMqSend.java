package com.exam.online.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-05-21 14:48
 */
public class RabbitMqSend {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(){
        amqpTemplate.convertAndSend("message", "测试发送消息");
    }

    public static void main(String[] args) {
        new RabbitMqSend().sendMessage();
    }
}

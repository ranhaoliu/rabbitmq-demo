package com.example.consumerspringboot;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*
* */
@Component
public class RabbitMQListener {

    //定义方法进行信息监听
    @RabbitListener(queues = "boot_queue")//表示监听的是哪一个队列
    public void ListenerQueue(Message message){
        System.out.println("message: "+ message);
    }
}

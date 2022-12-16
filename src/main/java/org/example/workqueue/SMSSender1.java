package org.example.workqueue;

import com.rabbitmq.client.*;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;

/*
消费者
* */
public class SMSSender1 {
    public static void main(String[] args) throws IOException {
        //获取TCP 长连接
        Connection connection = RabbitUtils.getConnection();
        //创建通信 通道，相当于 TCP 中的虚拟连接
        final Channel channel = connection.createChannel();
        //arg1: 队列名称，arg2：是否持久化，false对应否，arg3: 是否队列私有化 arg4:是否自动删除，false代表连接停止后不自动删除掉这个队列
        channel.queueDeclare(RabbitConstant.QUEUE_SMS,false,false,false,null);

        //如果不写 basicQos(1) 则自动MQ 会将所有请求平均发送给所有消费者
        //basicQos,MQ 不再对消费者一次发送多个请求，而是消费者处理完一个消息后(确认后)，从队列中获取一个新的
        channel.basicQos(1);//处理完一个取一个

        //从 mq服务器中获取数据
        //arg1:队列名称，arg2: 代表是否自动确认收到消息，false代表编程来确认消息，这是mq推荐做法
        // arg3: 传入 DefaultConsumer 的实现类
        channel.basicConsume(RabbitConstant.QUEUE_SMS,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               String jsonSMS = new String(body);
                System.out.println("SMSSend1-短信发送成功： "+jsonSMS);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //调用消费发送api
                //false 只确认签收当前消息，设置为true的时候则代表签收该消费者所有未签收的消息
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}


package org.example.routing;

import com.rabbitmq.client.*;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;

//消费者
public class BaiDu {
    public static void main(String[] args) throws IOException {
        //获取长连接
        Connection connection = RabbitUtils.getConnection();
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        //声明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_BAIDU,false,false,false,null);

        //指定队列与交换机的关系以及rounting key 之间的关系

        channel.queueBind(RabbitConstant.QUEUE_BAIDU,RabbitConstant.EXCHANGE_WEATHER_ROUTING,"china.hunan.changsha.20221120");
        channel.queueBind(RabbitConstant.QUEUE_BAIDU,RabbitConstant.EXCHANGE_WEATHER_ROUTING,"china.hubei.wuhan.20221120");
        channel.basicQos(1);

        channel.basicConsume(RabbitConstant.QUEUE_BAIDU,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("百度天气收到气象信息: "+new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });


    }
}

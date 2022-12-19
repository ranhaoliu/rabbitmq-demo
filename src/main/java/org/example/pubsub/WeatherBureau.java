package org.example.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
/*
发布者
* */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        System.out.println("请输入信息: ");
        String input = new Scanner(System.in).next();
        Channel channel = connection.createChannel();

        //arg1: 交换机的名字
        channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER,"",null,input.getBytes());

        channel.close();
        connection.close();
    }
}

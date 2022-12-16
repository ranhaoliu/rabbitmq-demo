package org.example.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {

        //获取TCP 长连接
        Connection connection = RabbitUtils.getConnection();
        //创建通信 通道，相当于 TCP 中的虚拟连接
        Channel channel = connection.createChannel();
        //arg1: 队列名称，arg2：是否持久化，false对应否，arg3: 是否队列私有化 arg4:是否自动删除，false代表连接停止后不自动删除掉这个队列
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD,false,false,false,null);
        String message = "I am helloworld";
        //arg1: 交换机名称，arg2:队列名称，arg3：额外属性, arg4: 要发送的消息
        channel.basicPublish("",RabbitConstant.QUEUE_HELLOWORLD,null,message.getBytes());
        channel.close();
        connection.close();
        System.out.println("数据发送成功....");
    }
}

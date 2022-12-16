package org.example.helloworld;

import com.rabbitmq.client.*;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {

        //获取TCP 长连接
        Connection connection = RabbitUtils.getConnection();
        //创建通信 通道，相当于 TCP 中的虚拟连接
        Channel channel = connection.createChannel();
        //arg1: 队列名称，arg2：是否持久化，false对应否，arg3: 是否队列私有化 arg4:是否自动删除，false代表连接停止后不自动删除掉这个队列
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD,false,false,false,null);

        //从 mq服务器中获取数据
        //arg1:队列名称，arg2: 代表是否自动确认收到消息，false代表编程来确认消息，这是mq推荐做法
        // arg3: 传入 DefaultConsumer 的实现类
        channel.basicConsume(RabbitConstant.QUEUE_HELLOWORLD,false,new Reciver(channel));
    }
}
class Reciver extends DefaultConsumer{
    private Channel channel;
    public Reciver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body);
        System.out.println("消费者接受到的消息: "+ message);
        System.out.println("消息的TagId:"+envelope.getDeliveryTag());
        //false 只确认签收当前消息，设置为true的时候则代表签收该消费者所有未签收的消息
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
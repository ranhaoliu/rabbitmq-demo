package org.example.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class QosListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //获取到的消息
        System.out.println(new String(message.getBody()));
        Thread.sleep(1000);
        //处理业务逻辑
        /*
        进行消息的签收
        <rabbit:listener-container connection-factory="connectionFactory" acknowledge="manual" prefetch="2">
       当不签收时，每次消费的消息与 prefetch="2" 的值有关系
         */
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);

    }

    @Override
    public void onMessage(Message message) {

    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {

    }
}

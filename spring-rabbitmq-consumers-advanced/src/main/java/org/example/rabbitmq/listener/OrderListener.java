package org.example.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            //1.接收转化消息
            System.out.println("message: "+new String(message.getBody()));
            //2.进行业务处理
            System.out.println("进行业务逻辑处理...");
            System.out.println("根据订单id查询其状态...");
            System.out.println("判断状态是否为支付成功...");
            System.out.println("取消订单，回滚库存..");

            //3.手动签收
            channel.basicAck(deliveryTag,true);
        }catch (Exception e){
            //拒绝签收
           /*第三个参数:requeue:重回队列。如果设置为true，则消息从新回到queue,broker会重新发送该消息给消费端,
           如果为false则拒绝签收
           * */
            channel.basicNack(deliveryTag,true,true);
//           channel.basicNack(deliveryTag,true,false);
        }

    }

    @Override
    public void onMessage(Message message) {

    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {

    }

}

package org.example.rabbitmq.listener;



import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class AckListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //1.获取消息id
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        int count=0;
       try{
           //2.获取消息
           //在这里打个断点
           System.out.println("message: "+new String(message.getBody()));
           //3.进行业务处理
           System.out.println("=====进行业务处理=======");
           //模拟出现异常
//           int i = 5/0;
           //4.进行消息签收
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

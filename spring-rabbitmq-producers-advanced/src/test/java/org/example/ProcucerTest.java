package org.example;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProcucerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //测试消息是否能到达到交换机里面去
    @Test
    public void testConfirm(){

        //定义回调，相当于当消息无法到达交换机时，就会进行回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm 方法被执行了...");
                if(ack){
                    //接收成功
                    System.out.println("接收成功消息: "+cause);
                }else {
                    //接收失败
                    System.out.println("接收失败消息: "+cause);
                    //做一些处理，让消息再次发送.
                }
            }
        });
        //进行消息发送
//        rabbitTemplate.convertAndSend("test_exchange_confirm111","confirm","message ....");//不存在的交换机
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message ....");//存在的交换机
        //进行睡眠操作
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //测试消息是否能够到达队列
    @Test
    public void testReturn(){

        //设置交换机处理失败消息的模式，为true的时候，消息就到达不了队列时，会重新返回给生产者
        rabbitTemplate.setMandatory(true);
        //定义回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("return 执行了...");
                System.out.println("message: "+message);
                System.out.println("replyText: "+replyText);
                System.out.println("exchange: "+exchange);
                System.out.println("rountingKey: "+routingKey);
            }
        });
        //进行消息发送
//
        //测试return，（交换机存在，队列不存在）
        rabbitTemplate.convertAndSend("test_exchange_confirm","er_queue","message ....");
       //测试return，（交换机存在，队列存在）
//        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message ....");
        //进行睡眠操作
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

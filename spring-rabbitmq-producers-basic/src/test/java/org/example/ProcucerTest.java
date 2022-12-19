package org.example;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProcucerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /*
    *简单模式
    * */
    @Test
    public void testHelloWorld(){
        //发送消息
        rabbitTemplate.convertAndSend("spring_queue","hello world spring ...");
    }

    /*
    发送fanout 消息(广播模式，不用写 路由key)
    * */
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("spring_fanout_exchange","","spring fanout ...");
    }

    /*
    * 路由模式
    * */
    @Test
    public void testDirect(){
        rabbitTemplate.convertAndSend("spring_direct_exchange","info","spring Direct ...");
    }

    /*
    topic 模式
    * */
    @Test
    public void testTopics(){
        rabbitTemplate.convertAndSend("spring_topic_exchange","baiqi.hehe.haha","spring topic ...");
    }
}

package org.example.confirm;

import com.rabbitmq.client.*;
import org.example.utils.RabbitConstant;
import org.example.utils.RabbitUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

//生产者
/*
创建 direct 交换机
https://www.cnblogs.com/dwlovelife/p/10991371.html#%E5%A6%82%E4%BD%95%E7%90%86%E8%A7%A3
* */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Map area = new LinkedHashMap<String,String>();
        area.put("china.hunan.changsha.20221120","中国湖南长沙20221120天气数据");
        area.put("china.hubei.wuhan.20221120","中国湖北武汉20221120天气数据");
        area.put("china.hunan.changsha.20221128","中国湖南长沙20221128天气数据");
        area.put("us.cal.lsj.20221120","美国加州洛杉矶20221120天气数据");

        area.put("china.hebei.shijiazhuang.20221120","中国河北石家庄20221120天气数据");
        area.put("china.henan.zhengzhou.20221120","中国河南郑州20221120天气数据");
        area.put("china.hunan.changsha.20221129","中国湖南长沙20221129天气数据");
        area.put("china.hunan.changsha.20221129","中国湖南长沙20221129天气数据");
        area.put("cn","中国湖南长沙20221129天气数据");

        Connection connection = RabbitUtils.getConnection();

        Channel channel = connection.createChannel();
        //开启confirm 监听模式
        channel.confirmSelect();
        //到达到broker中被确认
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long l, boolean b) throws IOException {
                // 第二个参数代表接收的数据是否为批量接收，一般我们用不到
                System.out.println("消息已被Broker接收,Tag: "+l);
            }

            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息已被Broker接收,Tag: "+l);
            }

        });
        //到达broker被确认后，但是找不到对应的队列投递
        channel.addReturnListener(new ReturnCallback() {
            public void handle(Return aReturn) {
                System.err.println("============================");
                System.err.println("Return 编码: "+ aReturn.getReplyCode() + "-Return 描述"+ aReturn.getReplyText());
                System.err.println("交换机: "+ aReturn.getExchange()+"-路由key: "+ aReturn.getRoutingKey());
                System.err.println("Return 主题: "+new String(aReturn.getBody()));
                System.err.println("==============================");
            }
        });

        Iterator<Map.Entry<String,String>> itr = area.entrySet().iterator();

        while(itr.hasNext()){
            Map.Entry<String,String> me = itr.next();
            //arg1: 交换机的名字,arg2: 作为消息的routing key,arg3:如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者，那么这条消息不会放入队列中
            channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER_TOPIC, me.getKey(), true,null,me.getValue().getBytes());
        }

        //这里不能关掉
//        channel.close();
        //这里不能关掉
//        connection.close();
    }
}

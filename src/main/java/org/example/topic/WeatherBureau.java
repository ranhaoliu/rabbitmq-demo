package org.example.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
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

        Connection connection = RabbitUtils.getConnection();

        Channel channel = connection.createChannel();
        Iterator<Map.Entry<String,String>> itr = area.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<String,String> me = itr.next();
            //arg1: 交换机的名字,arg2: 作为消息的routing key
            channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER_TOPIC, me.getKey(), null,me.getValue().getBytes());

        }



        channel.close();
        connection.close();
    }
}

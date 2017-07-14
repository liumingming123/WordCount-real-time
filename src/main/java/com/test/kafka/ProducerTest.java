package com.test.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class ProducerTest {
    public static void main(String[] args) throws InterruptedException {
        Properties props=new Properties();
        props.put("zk.connect","liu5:2181,liu6:2181,liu7:2181");
        props.put("metadata.broker.list","liu3:9092,liu4:9092,liu5:9092");
        props.put("serializer.class","kafka.serializer.StringEncoder");
        ProducerConfig config=new ProducerConfig(props);
        Producer producer=new Producer<String,String>(config);
        String [] words={"hello","world","ni","hao","test","lll","ls"};
        Random random=new Random();
        for(int i=1;i<100000000;i++){
           // Thread.sleep(500);
            int index=random.nextInt(words.length);
            String message=words[index];
            producer.send(new KeyedMessage<String,String>("wordcount",message));
        }
    }
}

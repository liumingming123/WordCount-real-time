package com.test.strom;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;



import java.util.Map;
import java.util.Random;

/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class SentenceSplitBolt extends BaseRichBolt{
 //   static final Logger log = LoggerFactory.getLogger(SentenceSplitBolt.class);

    private OutputCollector collector;


    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }


    public void execute(Tuple input) {
        // KafkaSpout中使用了"str"作为数据的字段名
        String sentence = input.getStringByField("str");
        String[] words = sentence.split(" ");

        if (words.length > 0) {
            for (String word : words) {
                System.out.println(word);
                collector.emit(new Values(word));
                System.out.println(word);
           //     log.info("**********单词："+word+"=============");
            }
        }

        // 确认：tuple成功处理
        collector.ack(input);
    }


    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("wordsplit"));
    }
}

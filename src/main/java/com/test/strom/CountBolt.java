package com.test.strom;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


import java.util.Hashtable;
import java.util.Map;

/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class CountBolt extends BaseRichBolt {
   // private  static final Logger log= LoggerFactory.getLogger(CountBolt.class);
    private OutputCollector collector;
    private Map<String,Integer> wordMap=new Hashtable<String, Integer>();
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    public void execute(Tuple tuple) {
        String word =tuple.getStringByField("wordsplit");
        if(!wordMap.containsKey(word)){
            wordMap.put(word,0);
        }
        int count=wordMap.get(word);
        count++;
        wordMap.put(word,count);
        collector.emit(new Values(word,String.valueOf(count)));
      //  log.info("**********计数："+count+"=============");
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word","count"));
    }
}

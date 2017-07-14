package com.test.strom;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.google.common.collect.Maps;
import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Map;
import java.util.UUID;

/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class WCTopology {
    //private  static final Logger log= LoggerFactory.getLogger(WCTopology.class);
    public static void main(String[] args) {
        //kafka配置
        TopologyBuilder builder=new TopologyBuilder();
        SpoutConfig spoutConfig=new SpoutConfig(new ZkHosts("liu5:2181,liu6:2181,liu7:2181"),"wordcount","/wordcount", UUID.randomUUID().toString());
        spoutConfig.forceFromStart=true;
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout=new KafkaSpout(spoutConfig);
        builder.setSpout("spout",new KafkaSpout(spoutConfig),5);
        builder.setBolt("split",new SentenceSplitBolt(),8).shuffleGrouping("spout");
        builder.setBolt("count",new CountBolt(),12).fieldsGrouping("split",new Fields("wordsplit"));
        SimpleHBaseMapper mapper=new SimpleHBaseMapper();
        mapper.withColumnFamily("result");
        mapper.withColumnFields(new Fields("count"));
        mapper.withRowKeyField("word");
        Map<String,Object> map= Maps.newTreeMap();
        map.put("hbase.rootdir","hdfs://liu1:9000/hbase");
        map.put("hbase.zookeeper.quorum","liu5:2181,liu6:2181,liu7:2181");
        HBaseBolt baseBolt=new HBaseBolt("wordcount",mapper).withConfigKey("hbase.conf");
        builder.setBolt("hbase",baseBolt,6).shuffleGrouping("count");
        Config conf=new Config();
        conf.setDebug(true);
        conf.put("hbase.conf",map);
        conf.setNumWorkers(4);
        conf.setNumAckers(0);
        LocalCluster cluster=new LocalCluster();
        cluster.submitTopology("wordcount",conf,builder.createTopology());
    }
}

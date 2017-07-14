# WordCount-real-time
kafka+storm+hbase



1.搭建流程
1.1集群各服务版本选择
hadoop 2.4.1 		namenode active :liu1 namenode standby:liu2   datanode:liu5,liu6,liu7
kafka 0.8.1.1		borkes:liu3,liu4,liu5
strom 0.9.2		nimbus:liu1 supervisor:liu2,liu3
hbase 0.96.2		HMaster:liu3 HA:liu4 regionservers:liu5,liu6,liu7
zookeeper 3.4.5	server:liu5,liu6,liu7
1.2启动各服务
注意先起zookeeper，再起hadoop，再起hbase，再起kafka，最后起strom



2.项目描述
使用wordcount程序，整合kafkf，storm和hbase
数据源：kafka, topic "logs"
词频统计: storm
存储：统计的结果存储到hbase

在topology中，使用KafkaSpout从kafka接收数据，接收到的数据是以行为单位的句子；
使用SentenceSplitBolt分拆出每个单词，再使用CountBolt统计每个单词出现的次数，最后使用Hbase bolt把结果存储到hbase中。
Kafka -> KafkaSpout -> SentenceSplitBolt -> CountBolt -> Hbase bolt




3.项目运行测试
先运行PrepareHbase建HBase表，然后运行ProducerTest，往topic写数据，最后运行WCTopology，开始strom计算，计算结果存在HBase中，可以用SelectHbase查询数据。
测试了1亿条数据，结果由于本集群每台的机器的CPU只有1核，内存只有1G，strom消费的太慢，所以就取了500万条做计算，为了消费的速度更快，可以增加works的数量，
还有hbase可以预分region，这样可以减少hbase的单点压力，优化方法有很多，本项目只做测试用。

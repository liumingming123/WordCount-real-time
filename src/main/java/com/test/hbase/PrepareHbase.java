package com.test.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import java.io.IOException;

/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class PrepareHbase {
    public static void main(String[] args) throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","liu5:2181,liu6:2181,liu7:2181");
        conf.set("hbase.rootdir","hdfs://liu1:9000/hbase");
        HBaseAdmin admin=new HBaseAdmin(conf);
        TableName table=TableName.valueOf("wordcount");
        HTableDescriptor htable=new HTableDescriptor(table);
        HColumnDescriptor result=new HColumnDescriptor("result");
        result.setMaxVersions(5);
        htable.addFamily(result);
        admin.createTable(htable);

    }
}

package com.test.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * Created by 小新很忙 on 2017/7/14.
 */
public class SelectHbase {
    public static void main(String[] args) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "liu5:2181,liu6:2181,liu7:2181");
        HTable table=new HTable(conf,"wordcount".getBytes());
        Scan scan=new Scan();
        scan.addFamily(Bytes.toBytes("result"));
        ResultScanner rs=table.getScanner(scan);
        for(Result r:rs){
            for(KeyValue kv:r.list()){
                String family=new String(kv.getFamily());
                System.out.print(family+"    ");
                String qualifier=new String(kv.getQualifier());
                System.out.print("    "+qualifier+"    "+new String(kv.getRow())+"    ");
                System.out.println(new String(kv.getValue()));
            }
        }
    }
}

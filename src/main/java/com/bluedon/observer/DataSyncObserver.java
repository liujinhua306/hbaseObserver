package com.bluedon.observer;


import com.bluedon.observer.bean.TSiemKmeansCenter;
import com.bluedon.observer.bean.TSiemKmeansCluster;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.phoenix.schema.types.PDataType;
import org.apache.phoenix.schema.types.PDataTypeFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.*;

public class DataSyncObserver extends BaseRegionObserver {

    private static Client client = null;
    private static final Log LOG = LogFactory.getLog(DataSyncObserver.class);

    /**
     * 读取HBase Shell的指令参数
     *
     * @param env
     */
    private void readConfiguration(CoprocessorEnvironment env) {
        Configuration conf = env.getConfiguration();
        Config.clusterName = conf.get("es_cluster");
        Config.nodeHost = conf.get("es_host");
        Config.nodePort = conf.getInt("es_port", -1);
        Config.indexName = conf.get("es_index");
        Config.typeName = conf.get("es_type");

        LOG.info("observer -- started with config: " + Config.getInfo());
    }


    @Override
    public void start(CoprocessorEnvironment env) throws IOException {
        readConfiguration(env);
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", Config.clusterName).build();
        TransportClient tstclient = TransportClient.builder().settings(settings).build();
        client = tstclient
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Config.nodeHost), Config.nodePort));
        System.out.println(client);
    }


    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        try {
            Field[] fields =null;
            if(Config.typeName.equals("assetcenter")){
                fields=TSiemKmeansCenter.class.getDeclaredFields();
            }
            if(Config.typeName.equals("assetcluster")){
                fields=TSiemKmeansCluster.class.getDeclaredFields();
            }
            String indexId = new String(put.getRow());
            NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("ROW",indexId);
                for (Cell cell : entry.getValue()) {
                    String qualiefier= new String(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                    for(Field field:fields) {
                        if (qualiefier.equals(field.getName().toUpperCase())) {
                            Object obj = getColunmData(field.getType(), cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                            System.out.println(obj);
                            if(field.getType().equals(Timestamp.class)){
                                Timestamp time=(Timestamp)obj;
                                json.put(qualiefier, time.getTime());
                            }else{
                                json.put(qualiefier, obj);
                            }

                        }
                    }
//                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
//                    String value = Bytes.toString(CellUtil.cloneValue(cell));
//                    json.put(key, value);
                }
                bulkRequest.add(client.prepareIndex(Config.indexName, Config.typeName).setSource(json));
            }
            BulkResponse bulkResponse = bulkRequest.get();
           // ElasticSearchOperator.addUpdateBuilderToBulk(client.prepareIndex(Config.indexName, Config.typeName, indexId).setSource(json));
            LOG.info("observer -- add new doc: " + indexId + " to type: " + Config.typeName);
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    @Override
    public void postDelete(final ObserverContext<RegionCoprocessorEnvironment> e, final Delete delete, final WALEdit edit, final Durability durability) throws IOException {
        try {
            String indexId = new String(delete.getRow());
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(client.prepareDelete(Config.indexName, Config.typeName, indexId));
            bulkRequest.get();
           // ElasticSearchOperator.addDeleteBuilderToBulk(client.prepareDelete(Config.indexName, Config.typeName, indexId));
            LOG.info("observer -- delete a doc: " + indexId);
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private static void testGetPutData(String rowKey, String columnFamily, String column, String value) {
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
        System.out.println(Bytes.toString(put.getRow()));
        for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
            Cell cell = entry.getValue().get(0);
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }

    private static Object getColunmData(Class<?> clazz,byte[] bytes,int offset ,int length){
        Object object = null;
        Iterator<PDataType> iterator = PDataTypeFactory.getInstance().getTypes().iterator();
        while(iterator.hasNext()){
            PDataType next = iterator.next();
            if(next.getJavaClass().equals(clazz)){
                object = next.toObject(bytes,offset,length);
                break;
            }

        }
        return object;
    }
    public static void main(String[] args) {
        //testGetPutData("111", "cf", "c1", "hello world");
        try {
          Field[]  fields=TSiemKmeansCenter.class.getDeclaredFields();
            System.out.println(fields.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

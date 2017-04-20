package com.bluedon.observer.bean;

import java.sql.Timestamp;

/**
 * Created by ljh on 2017/4/18.
 */
public class TSiemKmeansCluster {
    private String recordid;
    private String name;
    private String center_index;
    private String cluster_info;
    private String clno;
    private Double distance;
    private Timestamp cltime;
    private String demo;
    private String data_origin;

    public TSiemKmeansCluster() {
    }

    public TSiemKmeansCluster(String recordid, String name, String center_index, String cluster_info, String clno, Double distance, Timestamp cltime, String demo, String data_origin) {
        this.recordid = recordid;
        this.name = name;
        this.center_index = center_index;
        this.cluster_info = cluster_info;
        this.clno = clno;
        this.distance = distance;
        this.cltime = cltime;
        this.demo = demo;
        this.data_origin = data_origin;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter_index() {
        return center_index;
    }

    public void setCenter_index(String center_index) {
        this.center_index = center_index;
    }

    public String getCluster_info() {
        return cluster_info;
    }

    public void setCluster_info(String cluster_info) {
        this.cluster_info = cluster_info;
    }

    public String getClno() {
        return clno;
    }

    public void setClno(String clno) {
        this.clno = clno;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Timestamp getCltime() {
        return cltime;
    }

    public void setCltime(Timestamp cltime) {
        this.cltime = cltime;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getData_origin() {
        return data_origin;
    }

    public void setData_origin(String data_origin) {
        this.data_origin = data_origin;
    }
}

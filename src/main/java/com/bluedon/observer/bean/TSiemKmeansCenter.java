package com.bluedon.observer.bean;

import java.sql.Timestamp;

/**
 * Created by ljh on 2017/4/18.
 */
public class TSiemKmeansCenter {
    private String recordid;
    private String name;
    private String center_index;
    private String center_info;
    private String clno;
    private String center_index_p;
    private String clno_p;
    private Integer type;
    private Timestamp cltime;
    private String demo;
    private String data_origin;

    public TSiemKmeansCenter() {
    }

    public TSiemKmeansCenter(String recordid, String name, String center_index, String center_info, String clno, String center_index_p, String clno_p, Integer type, Timestamp cltime, String demo, String data_origin) {
        this.recordid = recordid;
        this.name = name;
        this.center_index = center_index;
        this.center_info = center_info;
        this.clno = clno;
        this.center_index_p = center_index_p;
        this.clno_p = clno_p;
        this.type = type;
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

    public String getCenter_info() {
        return center_info;
    }

    public void setCenter_info(String center_info) {
        this.center_info = center_info;
    }

    public String getClno() {
        return clno;
    }

    public void setClno(String clno) {
        this.clno = clno;
    }

    public String getCenter_index_p() {
        return center_index_p;
    }

    public void setCenter_index_p(String center_index_p) {
        this.center_index_p = center_index_p;
    }

    public String getClno_p() {
        return clno_p;
    }

    public void setClno_p(String clno_p) {
        this.clno_p = clno_p;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

package com.hgy.asus.addresschoicedemo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */

public class ShengShiquBean {


    /**
     * error : 0
     * message : u83b7u53d6u6210u529f
     * data : [{"region_id":"3410","parent_id":"3337","region_name":"u5357u95e8u5c71","region_type":"4","agency_id":"0"},{"region_id":"3412","parent_id":"3337","region_name":"u8521u5bb6u5761","region_type":"4","agency_id":"0"}]
     */

    private int error;
    private String message;
    /**
     * region_id : 3410
     * parent_id : 3337
     * region_name : u5357u95e8u5c71
     * region_type : 4
     * agency_id : 0
     */

    private List<DataBean> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int region_id;
        private int parent_id;
        private String region_name;

        public int getRegion_id() {
            return region_id;
        }

        public void setRegion_id(int region_id) {
            this.region_id = region_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }
    }
}

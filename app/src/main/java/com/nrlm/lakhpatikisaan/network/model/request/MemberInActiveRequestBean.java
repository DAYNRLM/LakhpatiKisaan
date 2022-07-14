package com.nrlm.lakhpatikisaan.network.model.request;

import java.io.Serializable;
import java.util.List;

public class MemberInActiveRequestBean implements Serializable {
    private String login_id;
    private String imei_no;
    private String device_name;
    private String location_coordinate;
    private String state_short_name;
    private String app_version;

    private List<InactiveMemSync> inactive_mem_sync;


    public static class InactiveMemSync {
        private String shg_code;
        private String shg_member_code;
        private String village_code;
        private String updated_on;

        public String getShg_code() {
            return shg_code;
        }

        public void setShg_code(String shg_code) {
            this.shg_code = shg_code;
        }

        public String getShg_member_code() {
            return shg_member_code;
        }

        public void setShg_member_code(String shg_member_code) {
            this.shg_member_code = shg_member_code;
        }

        public String getVillage_code() {
            return village_code;
        }

        public void setVillage_code(String village_code) {
            this.village_code = village_code;
        }


        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }
    }

        public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getLocation_coordinate() {
        return location_coordinate;
    }

    public void setLocation_coordinate(String location_coordinate) {
        this.location_coordinate = location_coordinate;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public List<InactiveMemSync> getNrlm_member_inactivate() {
        return inactive_mem_sync;
    }

    public void setNrlm_member_inactivate(List<InactiveMemSync> nrlm_member_inactivate) {
        this.inactive_mem_sync = nrlm_member_inactivate;
    }
}

package com.nrlm.lakhpatikisaan.network.model.request;

import java.io.Serializable;

public class CheckDuplicateRequestBean implements Serializable {


    private String state_short_name;

    private String login_id;
    private String imei_no;
    private String device_name;
    private String location_coordinate;
    private String member_data;
    private String flag;

    public CheckDuplicateRequestBean(String state_short_name, String user_id, String imei_no, String device_name, String location_coordinate, String member_data, String flag) {
        this.state_short_name = state_short_name;
        this.login_id = user_id;
        this.imei_no = imei_no;
        this.device_name = device_name;
        this.location_coordinate = location_coordinate;
        this.member_data = member_data;
        this.flag = flag;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public String getUser_id() {
        return login_id;
    }

    public void setUser_id(String user_id) {
        this.login_id = user_id;
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

    public String getMember_data() {
        return member_data;
    }

    public void setMember_data(String member_data) {
        this.member_data = member_data;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

/*http://10.197.183.105:8989/lakhpatishg/checkDuplicay

==========request=======
{
   "state_short_name":"up",
   "user_id":"UPAGASSDAD",
     "imei_no":"111",
   "device_name":"111",
   "location_coordinate":"111",
   "shg_code":"322249",
   "member_data":"322249|3712039|1|1,", ----------------------shgcode|member_code|sector_code|activity_code
   "flag":"B"------------------------------represent before or after data

}
==========response=======
{
    "member_code": "322249|3712039|1|1"
}*/
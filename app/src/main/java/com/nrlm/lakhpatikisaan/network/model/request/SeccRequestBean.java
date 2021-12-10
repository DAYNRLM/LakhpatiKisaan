package com.nrlm.lakhpatikisaan.network.model.request;

public class SeccRequestBean {
    private String  state_short_name;
    private String  login_id;
    private String  imei_no;
    private String  device_name;
    private String  location_coordinate;
    private String  lgd_village_code;

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
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

    public String getLgd_village_code() {
        return lgd_village_code;
    }

    public void setLgd_village_code(String lgd_village_code) {
        this.lgd_village_code = lgd_village_code;
    }
}
/*{   "state_short_name":"up",
   "login_id":"UPAGASSDAD",
     "imei_no":"111",
   "device_name":"111",
   "location_coordinate":"111",
   "lgd_village_code":"124838,1237"
}*/

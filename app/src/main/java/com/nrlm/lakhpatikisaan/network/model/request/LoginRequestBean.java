package com.nrlm.lakhpatikisaan.network.model.request;

import java.io.Serializable;

public class LoginRequestBean implements Serializable {

    private String login_id;
    private String password;
    private String imei_no;
    private String device_name;
    private String app_versions;
    private String date;
    private String android_version;
    private String android_api_version;
    private String location_coordinate;
    private String logout_time;
    private String app_login_time;




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getApp_versions() {
        return app_versions;
    }

    public void setApp_versions(String app_versions) {
        this.app_versions = app_versions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_api_version() {
        return android_api_version;
    }

    public void setAndroid_api_version(String android_api_version) {
        this.android_api_version = android_api_version;
    }

    public String getLocation_coordinate() {
        return location_coordinate;
    }

    public void setLocation_coordinate(String location_coordinate) {
        this.location_coordinate = location_coordinate;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getApp_login_time() {
        return app_login_time;
    }

    public void setApp_login_time(String app_login_time) {
        this.app_login_time = app_login_time;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }



       /*{
	"login_id": "HRKSVISHAKHA",
	"password": "28a8b0bdae78e9c6a2fe4c66fa573c685a751ea9d8f4f120c9cfc135db6c384b",
	"imei_no": "5d7eaa5ef9d3ebed",
	"device_name": "OPPO-OP4B79L1-CPH1933",
	"app_versions": "1.0.0",
	"date": "26-11-2021",
	"android_version": "9",
	"android_api_version": "12",
	"location_coordinate": "28.6294024,77.2189867",
	"logout_time": "2021-11-02 15:42:42",
	"app_login_time": "2021-11-09 11:09:31"
}*/
}

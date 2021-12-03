package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoginInfoEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    /*"login_id":"HRKSVISHAKHA","password":"c6024fd19953c32dc6e2b8fe91684a16a889cc8482157f1ec652616517537239"
    ,"mobile_number":"8813917982","state_code":"12","state_short_name":"hr",
    "server_date_time":"03-12-2021","language_id":1,"login_attempt":1,"logout_days":"5"}*/


    private String login_id,password,mobile_number,state_code,state_short_name,server_date_time,language_id,login_attempt,logout_days;

    public LoginInfoEntity(String login_id, String password, String mobile_number, String state_code, String state_short_name, String server_date_time, String language_id, String login_attempt, String logout_days) {
        this.login_id = login_id;
        this.password = password;
        this.mobile_number = mobile_number;
        this.state_code = state_code;
        this.state_short_name = state_short_name;
        this.server_date_time = server_date_time;
        this.language_id = language_id;
        this.login_attempt = login_attempt;
        this.logout_days = logout_days;
    }

    public int getId() {
        return id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getState_code() {
        return state_code;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public String getServer_date_time() {
        return server_date_time;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public String getLogin_attempt() {
        return login_attempt;
    }

    public String getLogout_days() {
        return logout_days;
    }
}

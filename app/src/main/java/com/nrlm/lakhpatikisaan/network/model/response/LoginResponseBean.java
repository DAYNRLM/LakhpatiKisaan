package com.nrlm.lakhpatikisaan.network.model.response;

public class LoginResponseBean {

    private int status;
    private Error error;
    private String  login_id ;
    private String password  ;
    private String  mobile_number ;
    private String  state_code ;
    private String  state_short_name ;
    private String  server_date_time ;
    private String  language_id ;
    private String  login_attempt ;
    private String  logout_days ;
    private String  mst_data ;
    private String  imei_flag ;

    public String getImei_flag() {
        return imei_flag;
    }

    public void setImei_flag(String imei_flag) {
        this.imei_flag = imei_flag;
    }

    public String getMst_data() {
        return mst_data;
    }

    public void setMst_data(String mst_data) {
        this.mst_data = mst_data;
    }





    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public String getServer_date_time() {
        return server_date_time;
    }

    public void setServer_date_time(String server_date_time) {
        this.server_date_time = server_date_time;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getLogin_attempt() {
        return login_attempt;
    }

    public void setLogin_attempt(String login_attempt) {
        this.login_attempt = login_attempt;
    }

    public String getLogout_days() {
        return logout_days;
    }

    public void setLogout_days(String logout_days) {
        this.logout_days = logout_days;
    }

    public static class Error{
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}

  /* {
        "status": 1,
            "error": {
        "code": "E200",
                "message": "Success"
    },
     "login_id": "HRKSVISHAKHA",
    "password": "c6024fd19953c32dc6e2b8fe91684a16a889cc8482157f1ec652616517537239",
    "mobile_number": "8813917982",
    "state_code": "12",
    "state_short_name": "hr",
    "pvtg_status": "N",
    "server_date_time": "02-12-2021",
    "language_id": 1,
    "login_attempt": 1,
    "logout_days": "5"
    }*/
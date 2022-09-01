
package com.nrlm.lakhpatikisaan.network.model.response;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DashboardResponse {


    public String javaToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static DashboardResponse jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, DashboardResponse.class);
    }
    public int status;
    public DashboardResponse.Error error;

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

    public ArrayList<DashboardData> getDashboarddata() {
        return dashboarddata;
    }

    public void setDashboarddata(ArrayList<DashboardData> dashboarddata) {
        this.dashboarddata = dashboarddata;
    }

    public ArrayList<DashboardResponse.DashboardData> dashboarddata;





    public static class DashboardData{
        public String shg_alloted;
        public String shg_member_alloted;
        public String shg_member_survey_completed;

        public String getShg_alloted() {
            return shg_alloted;
        }

        public void setShg_alloted(String shg_alloted) {
            this.shg_alloted = shg_alloted;
        }

        public String getShg_member_alloted() {
            return shg_member_alloted;
        }

        public void setShg_member_alloted(String shg_member_alloted) {
            this.shg_member_alloted = shg_member_alloted;
        }

        public String getShg_member_survey_completed() {
            return shg_member_survey_completed;
        }

        public void setShg_member_survey_completed(String shg_member_survey_completed) {
            this.shg_member_survey_completed = shg_member_survey_completed;
        }

        public String getShg_member_survey_pending() {
            return shg_member_survey_pending;
        }

        public void setShg_member_survey_pending(String shg_member_survey_pending) {
            this.shg_member_survey_pending = shg_member_survey_pending;
        }

        public String getShg_whose_all_mem_survy_completed() {
            return shg_whose_all_mem_survy_completed;
        }

        public void setShg_whose_all_mem_survy_completed(String shg_whose_all_mem_survy_completed) {
            this.shg_whose_all_mem_survy_completed = shg_whose_all_mem_survy_completed;
        }

        public String getShg_whose_atleast_one_mem_survy_completed() {
            return shg_whose_atleast_one_mem_survy_completed;
        }

        public void setShg_whose_atleast_one_mem_survy_completed(String shg_whose_atleast_one_mem_survy_completed) {
            this.shg_whose_atleast_one_mem_survy_completed = shg_whose_atleast_one_mem_survy_completed;
        }

        public String getCurrent_date() {
            return current_date;
        }

        public void setCurrent_date(String current_date) {
            this.current_date = current_date;
        }

        public String shg_member_survey_pending;
        public String shg_whose_all_mem_survy_completed;
        public String shg_whose_atleast_one_mem_survy_completed;
        public String current_date;


    }
    public static class Error{
        public String code;

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

        public String message;
    }
}

/*
{"status":1,"error":{"code":"E200","message":"Success"},"dashboarddata":[{"shg_alloted":"35","shg_member_alloted":"410","shg_member_survey_completed":"94","shg_member_survey_pending":"316","shg_whose_all_mem_survy_completed":"2","shg_whose_atleast_one_mem_survy_completed":"33","current_date":"2022-08-31"}]}
*/

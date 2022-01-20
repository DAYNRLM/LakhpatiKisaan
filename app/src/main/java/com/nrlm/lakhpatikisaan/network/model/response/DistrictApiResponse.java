package com.nrlm.lakhpatikisaan.network.model.response;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DistrictApiResponse{
    public static DistrictApiResponse jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, DistrictApiResponse.class);
    }

    public String javaToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public int status;
    public Error error;
    public ArrayList<DistrictResponse> districtResponse;

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

    public ArrayList<DistrictResponse> getDistrictResponse() {
        return districtResponse;
    }

    public void setDistrictResponse(ArrayList<DistrictResponse> districtResponse) {
        this.districtResponse = districtResponse;
    }



    public static class DistrictResponse{
        public String district_name;
        public String district_code;


        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getDistrict_code() {
            return district_code;
        }

        public void setDistrict_code(String district_code) {
            this.district_code = district_code;
        }

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

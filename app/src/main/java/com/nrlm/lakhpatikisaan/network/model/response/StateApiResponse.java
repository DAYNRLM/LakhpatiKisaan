package com.nrlm.lakhpatikisaan.network.model.response;

import com.google.gson.Gson;

import java.util.ArrayList;




public class StateApiResponse{

    public static StateApiResponse jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, StateApiResponse.class);
    }

    public String javaToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int status;
    public Error error;
    public ArrayList<StateResponse> stateResponse;
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

    public ArrayList<StateResponse> getStateResponse() {
        return stateResponse;
    }

    public void setStateResponse(ArrayList<StateResponse> stateResponse) {
        this.stateResponse = stateResponse;
    }
  public static   class Error{
        public String code;
        public String message;

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
  public static  class StateResponse{
        public String state_name;
      public String state_code;

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getState_code() {
            return state_code;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

    }

}
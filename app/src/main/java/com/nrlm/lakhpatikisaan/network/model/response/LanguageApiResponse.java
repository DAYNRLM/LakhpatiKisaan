package com.nrlm.lakhpatikisaan.network.model.response;

import com.google.gson.Gson;

import java.util.ArrayList;



public class LanguageApiResponse{
    public static LanguageApiResponse jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, LanguageApiResponse.class);
    }

    public String javaToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public int status;
    public Error error;
    public ArrayList<LanguageResponse> languageResponse;


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

    public ArrayList<LanguageResponse> getLanguageResponse() {
        return languageResponse;
    }

    public void setLanguageResponse(ArrayList<LanguageResponse> languageResponse) {
        this.languageResponse = languageResponse;
    }


    public static class LanguageResponse{
        public int id;
        public String language_name;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLanguage_name() {
            return language_name;
        }

        public void setLanguage_name(String language_name) {
            this.language_name = language_name;
        }

    }

    public static class Error{
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
}

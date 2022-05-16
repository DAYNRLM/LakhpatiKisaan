package com.nrlm.lakhpatikisaan.network.model.response;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CheckDuplicateResponseBean {               /*{"status":0,"error":null,"memcode":[{"member_code":"0"}]}*/

    private int status;

    private String error;

    private List<Memcode> memcode;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }

    public void setError(String error){
        this.error = error;
    }

    public String getError(){
        return this.error;
    }
    public void setMemcode(List<Memcode> memcode){
        this.memcode = memcode;
    }
    public List<Memcode> getMemcode(){
        return this.memcode;
    }
    public static class Memcode{

        private String member_code;

        public void setMember_code(String member_code){
            this.member_code = member_code;
        }
        public String getMember_code(){
            return this.member_code;
        }
    }

    /*  private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
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
    }*/

}




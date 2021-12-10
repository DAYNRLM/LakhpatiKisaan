package com.nrlm.lakhpatikisaan.network.model.request;

public class OtpRequestBean {
    private String mobileno;
    private String message;


    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
    public String getMobileno() {
        return mobileno;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

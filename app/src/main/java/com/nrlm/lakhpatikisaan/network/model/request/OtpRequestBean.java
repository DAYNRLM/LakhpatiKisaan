package com.nrlm.lakhpatikisaan.network.model.request;

public class OtpRequestBean {
   /*{
    "mobileno":"9319369737",
    "message":"1234 (Lakhpati Didi)"
}*/

    private String mobileno;
    private String message;

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

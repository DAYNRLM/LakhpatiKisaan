package com.nrlm.lakhpatikisaan.network.model.request;

public class OtpRequestBean {
    private String mobile;
    private String otpMessage;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtpMessage() {
        return otpMessage;
    }

    public void setOtpMessage(String otpMessage) {
        this.otpMessage = otpMessage;
    }
}

package com.nrlm.lakhpatikisaan.database.dbbean;

public class ShgAndMemberDataBean {

    private String shgCode,memberCode;

    public ShgAndMemberDataBean(String shgCode, String memberCode) {
        this.shgCode = shgCode;
        this.memberCode = memberCode;
    }

    public String getShgCode() {
        return shgCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }
}

package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class ShgAndMemberDataBean {

    @ColumnInfo(name = "shgCode")
    private String shgCode;
    @ColumnInfo(name = "shgMemberCode")
    private String memberCode;
    @ColumnInfo(name = "seccNumber")
    private String secc;

    public ShgAndMemberDataBean(String shgCode, String memberCode, String secc) {
        this.shgCode = shgCode;
        this.memberCode = memberCode;
        this.secc = secc;
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

    public String getSecc() {
        return secc;
    }

    public void setSecc(String secc) {
        this.secc = secc;
    }
}

package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class AadharDbBean {

    @ColumnInfo(name = "aadharNumber")
    public String aadharNumber;
    @ColumnInfo(name = "aadharName")
    public String aadharName;
    @ColumnInfo(name = "aadharVerifiedStatus")
    public String aadharVerifiedStatus;

    public AadharDbBean(String aadharNumber, String aadharName, String aadharVerifiedStatus) {
        this.aadharNumber = aadharNumber;
        this.aadharName = aadharName;
        this.aadharVerifiedStatus = aadharVerifiedStatus;
    }


    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getAadharName() {
        return aadharName;
    }

    public void setAadharName(String aadharName) {
        this.aadharName = aadharName;
    }

    public String getAadharVerifiedStatus() {
        return aadharVerifiedStatus;
    }

    public void setAadharVerifiedStatus(String aadharVerifiedStatus) {
        this.aadharVerifiedStatus = aadharVerifiedStatus;
    }
}

package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AadhaarEntity {

    @PrimaryKey(autoGenerate = true)
    public int id ;

    public String aadharNumber;
    public String aadharName;
    public String shgMemberCode;
    public String aadharSyncStatus;
    public String aadharVerifiedStatus;

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

    public String getShgMemberCode() {
        return shgMemberCode;
    }

    public void setShgMemberCode(String shgMemberCode) {
        this.shgMemberCode = shgMemberCode;
    }

    public String getAadharSyncStatus() {
        return aadharSyncStatus;
    }

    public void setAadharSyncStatus(String aadharSyncStatus) {
        this.aadharSyncStatus = aadharSyncStatus;
    }

    public String getAadharVerifiedStatus() {
        return aadharVerifiedStatus;
    }

    public void setAadharVerifiedStatus(String aadharVerifiedStatus) {
        this.aadharVerifiedStatus = aadharVerifiedStatus;
    }
}

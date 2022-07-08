package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memberInActive")
public class MemberInActiveEntity {

    public String getMemberCode() {
        return memberCode;
    }

    public String getShgCode() {
        return shgCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public String getStatus() {
        return status;
    }

    public String getTime_Stamp() {
        return time_Stamp;
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String memberCode;
    private String  shgCode ;
    private String  villageCode ;
    private String  newT ;

    public String getNewT() {
        return newT;
    }

    public void setNewT(String newT) {
        this.newT = newT;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime_Stamp(String time_Stamp) {
        this.time_Stamp = time_Stamp;
    }

    private String  status ;
    private String  time_Stamp ;


    }




package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class InActiveMember {
    @ColumnInfo(name = "shg_code")
    private String shgCode;
    @ColumnInfo(name = "member_code")
    private String memberCode;
    @ColumnInfo(name = "village_code")
    private String villageCode;


    public void ShgAndMemberDataBean(String shgCode, String memberCode, String villageCode) {
        this.shgCode= shgCode;
        this.memberCode= memberCode;
        this.villageCode= villageCode;
        
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

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }}

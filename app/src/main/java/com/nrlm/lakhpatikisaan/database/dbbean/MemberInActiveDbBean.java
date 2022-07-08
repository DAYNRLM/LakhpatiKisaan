package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class MemberInActiveDbBean {
    @ColumnInfo(name = "shg_code")
        private String shgCode;

        @ColumnInfo(name = "member_code")
        private String memberCode;

    @ColumnInfo(name = "village_code")
    private String villageCode;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "time_Stamp")
    private String timeStamp;


    @ColumnInfo(name = "newT")
    private String newT;

    public String getNewT() {
        return newT;
    }

    public String getShgCode() {
        return shgCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }






    }


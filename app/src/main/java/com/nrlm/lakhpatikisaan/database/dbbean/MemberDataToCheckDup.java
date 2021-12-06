package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class MemberDataToCheckDup {
    @ColumnInfo(name = "shgCode")
    private String shgCode;
    @ColumnInfo(name = "shgMemberCode")
    private String memberCode;
    @ColumnInfo(name = "sectorDate")
    private String sectorCode;
    @ColumnInfo(name = "activityCode")
    private String activityCode;

    public MemberDataToCheckDup(String shgCode, String memberCode, String sectorCode, String activityCode) {
        this.shgCode = shgCode;
        this.memberCode = memberCode;
        this.sectorCode = sectorCode;
        this.activityCode = activityCode;
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

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}

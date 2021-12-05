package com.nrlm.lakhpatikisaan.database.dbbean;

public class MemberDataToCheckDup {

    private String shgCode,memberCode,sectorCode,activityCode;

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

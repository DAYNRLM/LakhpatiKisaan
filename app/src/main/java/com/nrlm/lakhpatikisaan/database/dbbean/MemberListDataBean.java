package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class MemberListDataBean {
    @ColumnInfo(name = "member_code")
    private String memberCode;
    @ColumnInfo(name = "shg_code")
    private String shgCode;
    @ColumnInfo(name = "member_name")
    private String memberName;
    @ColumnInfo(name = "last_entry_before_nrlm")
    private String lastFilledBeforeNrlmEntry;
    @ColumnInfo(name = "last_entry_after_nrlm")
    private String lastFilledAfterNrlmEntry;
    @ColumnInfo(name = "aadhaar_verified_status")
    private String aadhaar_verified_status;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "belonging_name")
    private String belonging_name;

    public String getBelonging_name() {
        return belonging_name;
    }

    public void setBelonging_name(String belonging_name) {
        this.belonging_name = belonging_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/* @ColumnInfo(name = "status_active")
    private String status_active;

    public String getStatus_active() {
        return status_active;
    }

    public void setStatus_active(String status_active) {
        this.status_active = status_active;
    }*/

    public String getAadhaar_verified_status() {

        return aadhaar_verified_status;
    }

    public void setAadhaar_verified_status(String aadhaar_verified_status) {
        this.aadhaar_verified_status = aadhaar_verified_status;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLastFilledBeforeNrlmEntry() {
        return lastFilledBeforeNrlmEntry;
    }

    public void setLastFilledBeforeNrlmEntry(String lastFilledBeforeNrlmEntry) {
        this.lastFilledBeforeNrlmEntry = lastFilledBeforeNrlmEntry;
    }

    public String getLastFilledAfterNrlmEntry() {
        return lastFilledAfterNrlmEntry;
    }

    public void setLastFilledAfterNrlmEntry(String lastFilledAfterNrlmEntry) {
        this.lastFilledAfterNrlmEntry = lastFilledAfterNrlmEntry;
    }
}

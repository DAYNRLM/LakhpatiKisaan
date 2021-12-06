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

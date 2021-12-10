package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class ShgDataBean {
    @ColumnInfo(name = "shg_code")
    private String shgCode;
    @ColumnInfo(name = "shg_name")
    private String shgName;

    public String getShgCode() {
        return shgCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }

    public String getShgName() {
        return shgName;
    }

    public void setShgName(String shgName) {
        this.shgName = shgName;
    }
}

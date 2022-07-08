package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class VoDataBean {
    @ColumnInfo(name = "vo_code")
    private String vo_code;
    @ColumnInfo(name = "vo_name")
    private String vo_name;


    public String getVo_code() {
        return vo_code;
    }

    public void setVo_code(String vo_code) {
        this.vo_code = vo_code;
    }

    public String getVo_name() {
        return vo_name;
    }

    public void setVo_name(String vo_name) {
        this.vo_name = vo_name;
    }
}

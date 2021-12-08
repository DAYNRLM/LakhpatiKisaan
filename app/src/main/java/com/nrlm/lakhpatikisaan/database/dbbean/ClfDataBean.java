package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class ClfDataBean {
    @ColumnInfo(name = "clf_code")
    private String clf_code;
    @ColumnInfo(name = "clf_name")
    private String clf_name;

    public String getClf_code() {
        return clf_code;
    }

    public void setClf_code(String clf_code) {
        this.clf_code = clf_code;
    }

    public String getClf_name() {
        return clf_name;
    }

    public void setClf_name(String clf_name) {
        this.clf_name = clf_name;
    }
}

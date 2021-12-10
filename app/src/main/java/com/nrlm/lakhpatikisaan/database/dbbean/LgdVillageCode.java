package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class LgdVillageCode {
    @ColumnInfo(name = "lgd_village_code")
    private String lgd_village_code;

    public LgdVillageCode(String lgd_village_code) {
        this.lgd_village_code = lgd_village_code;
    }

    public String getLgd_village_code() {
        return lgd_village_code;
    }

    public void setLgd_village_code(String lgd_village_code) {
        this.lgd_village_code = lgd_village_code;
    }
}

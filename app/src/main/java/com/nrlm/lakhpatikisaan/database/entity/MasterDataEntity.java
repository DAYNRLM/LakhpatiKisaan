package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MasterDataEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String block_name,gp_name,village_code,village_name,shg_name,shg_code,member_code,member_name
            ,clf_code,clf_name,vo_code,vo_name,member_joining_date,last_entry_after_nrlm,last_entry_before_nrlm;

    public MasterDataEntity(String block_name, String gp_name, String village_code, String village_name, String shg_name,
                            String shg_code, String member_code, String member_name, String clf_code, String clf_name,
                            String vo_code, String vo_name, String member_joining_date,
                            String last_entry_after_nrlm, String last_entry_before_nrlm) {
        this.block_name = block_name;
        this.gp_name = gp_name;
        this.village_code = village_code;
        this.village_name = village_name;
        this.shg_name = shg_name;
        this.shg_code = shg_code;
        this.member_code = member_code;
        this.member_name = member_name;
        this.clf_code = clf_code;
        this.clf_name = clf_name;
        this.vo_code = vo_code;
        this.vo_name = vo_name;
        this.member_joining_date = member_joining_date;
        this.last_entry_after_nrlm = last_entry_after_nrlm;
        this.last_entry_before_nrlm = last_entry_before_nrlm;
    }


    public int getId() {
        return id;
    }

    public String getBlock_name() {
        return block_name;
    }

    public String getGp_name() {
        return gp_name;
    }

    public String getVillage_code() {
        return village_code;
    }

    public String getVillage_name() {
        return village_name;
    }

    public String getShg_name() {
        return shg_name;
    }

    public String getShg_code() {
        return shg_code;
    }

    public String getMember_code() {
        return member_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getClf_code() {
        return clf_code;
    }

    public String getClf_name() {
        return clf_name;
    }

    public String getVo_code() {
        return vo_code;
    }

    public String getVo_name() {
        return vo_name;
    }

    public String getMember_joining_date() {
        return member_joining_date;
    }

    public String getLast_entry_after_nrlm() {
        return last_entry_after_nrlm;
    }

    public String getLast_entry_before_nrlm() {
        return last_entry_before_nrlm;
    }
}

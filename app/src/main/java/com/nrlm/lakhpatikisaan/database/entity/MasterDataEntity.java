package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MasterDataEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String block_name,block_code,gp_code,gp_name,village_code,village_name,shg_name,shg_code,member_code,member_name
            ,clf_code,clf_name,vo_code,vo_name,member_joining_date,last_entry_after_nrlm,last_entry_before_nrlm,secc_no_flag,lgd_village_code,aadhaar_verified_status,gender;

    public MasterDataEntity(String block_name, String block_code, String gp_code, String gp_name,
                            String village_code, String village_name, String shg_name, String shg_code,
                            String member_code, String member_name, String clf_code, String clf_name,
                            String vo_code, String vo_name, String member_joining_date, String last_entry_after_nrlm,
                            String last_entry_before_nrlm, String secc_no_flag, String lgd_village_code,
                            String aadhaar_verified_status, String gender) {
        this.block_name = block_name;
        this.block_code = block_code;
        this.gp_code = gp_code;
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
        this.secc_no_flag = secc_no_flag;
        this.lgd_village_code = lgd_village_code;
        this.aadhaar_verified_status = aadhaar_verified_status;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public String getGp_code() {
        return gp_code;
    }

    public void setGp_code(String gp_code) {
        this.gp_code = gp_code;
    }

    public String getGp_name() {
        return gp_name;
    }

    public void setGp_name(String gp_name) {
        this.gp_name = gp_name;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getShg_name() {
        return shg_name;
    }

    public void setShg_name(String shg_name) {
        this.shg_name = shg_name;
    }

    public String getShg_code() {
        return shg_code;
    }

    public void setShg_code(String shg_code) {
        this.shg_code = shg_code;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

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

    public String getMember_joining_date() {
        return member_joining_date;
    }

    public void setMember_joining_date(String member_joining_date) {
        this.member_joining_date = member_joining_date;
    }

    public String getLast_entry_after_nrlm() {
        return last_entry_after_nrlm;
    }

    public void setLast_entry_after_nrlm(String last_entry_after_nrlm) {
        this.last_entry_after_nrlm = last_entry_after_nrlm;
    }

    public String getLast_entry_before_nrlm() {
        return last_entry_before_nrlm;
    }

    public void setLast_entry_before_nrlm(String last_entry_before_nrlm) {
        this.last_entry_before_nrlm = last_entry_before_nrlm;
    }

    public String getSecc_no_flag() {
        return secc_no_flag;
    }

    public void setSecc_no_flag(String secc_no_flag) {
        this.secc_no_flag = secc_no_flag;
    }

    public String getLgd_village_code() {
        return lgd_village_code;
    }

    public void setLgd_village_code(String lgd_village_code) {
        this.lgd_village_code = lgd_village_code;
    }

    public String getAadhaar_verified_status() {
        return aadhaar_verified_status;
    }

    public void setAadhaar_verified_status(String aadhaar_verified_status) {
        this.aadhaar_verified_status = aadhaar_verified_status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

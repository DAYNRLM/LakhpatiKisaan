package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SeccEntity {
 @PrimaryKey(autoGenerate = true)
    private int id;

    public String secc_no;
    public String member_name;
    public String father_name;
    public String shg_member_code;

    public SeccEntity(String secc_no, String member_name, String father_name, String shg_member_code) {
        this.secc_no = secc_no;
        this.member_name = member_name;
        this.father_name = father_name;
        this.shg_member_code = shg_member_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecc_no() {
        return secc_no;
    }

    public void setSecc_no(String secc_no) {
        this.secc_no = secc_no;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getShg_member_code() {
        return shg_member_code;
    }

    public void setShg_member_code(String shg_member_code) {
        this.shg_member_code = shg_member_code;
    }
}
/*secc_no : "27091500301390000056700101006"
member_name : "ABHISHEK"
father_name : "PAPPU"
shg_member_code : "5748071"*/
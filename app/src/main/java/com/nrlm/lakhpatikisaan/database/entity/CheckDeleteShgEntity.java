package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CheckDeleteShgEntity {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String shgCode;

    public CheckDeleteShgEntity(String shgCode) {
        this.shgCode = shgCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShgCode() {
        return shgCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }
}

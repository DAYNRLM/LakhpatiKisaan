package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ActivityEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String activity_name;
    private int sector_code,activity_code;

    public ActivityEntity(String activity_name, int sector_code, int activity_code) {
        this.activity_name = activity_name;
        this.sector_code = sector_code;
        this.activity_code = activity_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getSector_code() {
        return sector_code;
    }

    public void setSector_code(int sector_code) {
        this.sector_code = sector_code;
    }

    public int getActivity_code() {
        return activity_code;
    }

    public void setActivity_code(int activity_code) {
        this.activity_code = activity_code;
    }
}

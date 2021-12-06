package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SectorEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String sector_name;
    private int sector_code ;

    public SectorEntity(String sector_name, int sector_code) {
        this.sector_name = sector_name;
        this.sector_code = sector_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public int getSector_code() {
        return sector_code;
    }

    public void setSector_code(int sector_code) {
        this.sector_code = sector_code;
    }
}

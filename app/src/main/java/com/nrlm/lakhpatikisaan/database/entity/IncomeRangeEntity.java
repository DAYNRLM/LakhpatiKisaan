package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class IncomeRangeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int frequency_id,range_id;
    private String range_name ;

    public IncomeRangeEntity(int frequency_id, int range_id, String range_name) {
        this.frequency_id = frequency_id;
        this.range_id = range_id;
        this.range_name = range_name;
    }

    public int getFrequency_id() {
        return frequency_id;
    }

    public void setFrequency_id(int frequency_id) {
        this.frequency_id = frequency_id;
    }

    public int getRange_id() {
        return range_id;
    }

    public void setRange_id(int range_id) {
        this.range_id = range_id;
    }

    public String getRange_name() {
        return range_name;
    }

    public void setRange_name(String range_name) {
        this.range_name = range_name;
    }
}

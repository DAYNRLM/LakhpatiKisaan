package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FrequencyEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int frequency_id ;
    private String frequency_name;

    public FrequencyEntity(int frequency_id, String frequency_name) {
        this.frequency_id = frequency_id;
        this.frequency_name = frequency_name;
    }

    public int getFrequency_id() {
        return frequency_id;
    }

    public void setFrequency_id(int frequency_id) {
        this.frequency_id = frequency_id;
    }

    public String getFrequency_name() {
        return frequency_name;
    }

    public void setFrequency_name(String frequency_name) {
        this.frequency_name = frequency_name;
    }
}

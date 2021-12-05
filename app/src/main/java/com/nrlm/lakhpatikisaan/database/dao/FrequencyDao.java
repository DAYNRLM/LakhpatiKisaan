package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;

import java.util.List;

@Dao
public interface FrequencyDao {

    @Insert
    void insert(FrequencyEntity frequencyEntity);
}

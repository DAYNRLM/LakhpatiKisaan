package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;

import java.util.List;

@Dao
public interface FrequencyDao {

    @Insert
    void insert(FrequencyEntity frequencyEntity);

    @Query("select * from FrequencyEntity")
    List<FrequencyEntity> getAllFrequency();
}

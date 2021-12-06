package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    void insert(ActivityEntity activityEntity);
}
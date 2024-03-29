package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    void insert(ActivityEntity activityEntity);

    @Query("select * from ActivityEntity where ActivityEntity.sector_code =:sectorId ")
    List<ActivityEntity> getAllActivity(int sectorId);

    @Query("select * from ActivityEntity")
    List<ActivityEntity> getAllActivityWithoutSec();

    @Query("delete from activityentity")
    void deleteAll();
}

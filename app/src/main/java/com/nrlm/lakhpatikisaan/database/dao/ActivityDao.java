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

    @Query("select * from ActivityEntity where ActivityEntity.sector_code =:sectorCode ")
    List<ActivityEntity> getAllActivity(String sectorCode);
}

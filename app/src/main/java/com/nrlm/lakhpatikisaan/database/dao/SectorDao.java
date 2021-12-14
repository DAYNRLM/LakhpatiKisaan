package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;

import java.util.List;
@Dao
public interface SectorDao {
    @Insert
    void insert(SectorEntity sectorEntity);

    @Query("select * from SectorEntity ")
    List<SectorEntity> getAllSector();

    @Query("delete from sectorentity")
    void deleteAll();

}

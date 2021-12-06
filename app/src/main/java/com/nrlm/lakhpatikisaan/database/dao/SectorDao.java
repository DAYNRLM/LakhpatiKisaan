package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;

import java.util.List;
@Dao
public interface SectorDao {
    @Insert
    void insert(SectorEntity sectorEntity);
}

package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.SeccEntity;

import java.util.List;

@Dao
public interface SeccDao {

    @Insert
    void insertAll(List<SeccEntity> seccEntityList);
}

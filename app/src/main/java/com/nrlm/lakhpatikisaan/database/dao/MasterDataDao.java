package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;

import java.util.List;

@Dao
public abstract class MasterDataDao {

    @Insert()
    public abstract void insertAll(List<MasterDataEntity> masterDataEntities);
}

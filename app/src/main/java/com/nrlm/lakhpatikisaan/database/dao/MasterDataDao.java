package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.BlockBean;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;


import java.util.List;

@Dao
public interface MasterDataDao {

    @Insert()
    void insertAll(List<MasterDataEntity> masterDataEntities);


    @Query("select DISTINCT MasterDataEntity.block_code ,MasterDataEntity.block_name  from MasterDataEntity ")
    List<BlockBean> getAllBlock();
}


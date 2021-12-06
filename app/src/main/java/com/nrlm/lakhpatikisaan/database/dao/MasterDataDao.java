package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;

import java.util.List;

@Dao
public interface MasterDataDao {

    @Insert()
    void insertAll(List<MasterDataEntity> masterDataEntities);


}

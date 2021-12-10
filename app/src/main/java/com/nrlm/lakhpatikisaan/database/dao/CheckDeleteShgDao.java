package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.CheckDeleteShgEntity;

import java.util.List;

@Dao
public interface CheckDeleteShgDao {

    @Insert
    void insertAll(List<CheckDeleteShgEntity> checkDeleteShgEntityList);

    @Query("select * from checkdeleteshgentity")
    List<CheckDeleteShgEntity> getShgToDelete();

}

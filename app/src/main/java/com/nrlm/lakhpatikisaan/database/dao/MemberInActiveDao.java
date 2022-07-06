package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.MemberInActiveEntity;

import java.util.List;
@Dao
public interface MemberInActiveDao {

    @Insert
    void insertAll(List<MemberInActiveEntity> memberInActiveEntities);

    @Query("delete from MemberInActiveEntity")
    void deleteAll();

}


package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.MemberActiveEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;

@Dao
public interface MemberActiveDao {
    @Insert
    void insertAll(MemberActiveEntity memberActiveEntity);

}

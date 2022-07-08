package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.MemberActiveEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;

import java.util.List;

@Dao
public interface MemberActiveDao {
    @Insert
    void insertAll(List<MemberActiveEntity> memberActiveEntity);
/*
    @Insert
    void insertAll(MemberActiveEntity arg0);*/
}

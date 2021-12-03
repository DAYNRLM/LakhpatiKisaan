package com.nrlm.lakhpatikisaan.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;

import java.util.List;

@Dao
public interface MemberEntryDao {

    @Insert
    void insertAll(MemberEntryEntity memberEntryEntity);

    @Query("select * from MemberEntryEntity")
    List<MemberEntryEntity> getAllData();

    @Query("select * from MemberEntryEntity where MemberEntryEntity.flagBeforeAfterNrlm =:flag")
    List<MemberEntryEntity> getAllDataWithFlag(String flag);

    @Query("DELETE FROM MemberEntryEntity")
    void deleteTable();

    @Query("DELETE FROM MemberEntryEntity where MemberEntryEntity.flagBeforeAfterNrlm =:flag")
    void deleteWithEntryFlag(String flag);
}

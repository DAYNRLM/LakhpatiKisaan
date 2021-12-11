package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.SeccEntity;

import java.util.List;

@Dao
public interface SeccDao {

    @Insert
    void insertAll(List<SeccEntity> seccEntityList);

    @Query("select * from SeccEntity where shg_member_code=:memberCode")
    List<SeccEntity> getSeccDetail(String memberCode);
}

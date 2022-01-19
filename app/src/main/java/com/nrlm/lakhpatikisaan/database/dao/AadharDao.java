package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.AadhaarEntity;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;

import java.util.List;

@Dao
public interface AadharDao {
    @Insert
    void insert(AadhaarEntity aadhaarEntity);

    /****get aadhar data based on aadhar number*****/
    @Query("select * from AadhaarEntity where AadhaarEntity.aadharNumber =:aadharNumber ")
    List<AadhaarEntity> getAadharData(String aadharNumber);

    /****get aadhar data based on Member Code number*****/
    @Query("select * from AadhaarEntity where AadhaarEntity.shgMemberCode =:shgMemberCode ")
    List<AadhaarEntity> getAadharBasedOnMember(String shgMemberCode);

}

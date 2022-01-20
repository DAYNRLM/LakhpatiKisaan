package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.AadharDbBean;
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

    @Query("select aadharName,aadharNumber,aadharVerifiedStatus from aadhaarentity" +
            " where aadhaarentity.shgMemberCode=:memberCode and aadhaarentity.aadharSyncStatus='0'")
    AadharDbBean getAadharDetails(String memberCode);

    @Query("update aadhaarentity set aadharSyncStatus='1' where aadharSyncStatus='0'")
    void updateAadharSyncStatus();

}

package com.nrlm.lakhpatikisaan.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.ActivityDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberDataToCheckDup;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgAndMemberDataBean;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;

import java.util.List;

@Dao
public interface MemberEntryDao {

   @Insert
    void insertAll(MemberEntryEntity memberEntryEntity);

    @Query("select * from MemberEntryEntity")
    List<MemberEntryEntity> getAllData();



    @Query("select * from MemberEntryEntity where MemberEntryEntity.flagBeforeAfterNrlm = :flag")
    List<MemberEntryEntity> getAllDataWithFlag(String flag);

    @Query("DELETE FROM MemberEntryEntity")
    void deleteTable();

    @Query("DELETE FROM MemberEntryEntity where MemberEntryEntity.flagBeforeAfterNrlm =:flag")
    void deleteWithEntryFlag(String flag);

    @Query("select shgCode,shgMemberCode,sectorDate,activityCode,flagBeforeAfterNrlm  from MemberEntryEntity " +
            "where MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag")
   List<MemberDataToCheckDup> getDataToCheckDuplicate(String entryCompleteConfirmation, String syncFlag);

    /*@Query("select distinct shgCode,shgMemberCode,seccNumber from MemberEntryEntity " +
            "where MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag ")
    List<ShgAndMemberDataBean> getDistinctShgAndMemberToSync(String entryCompleteConfirmation, String syncFlag);*/
@Query("select distinct shgCode,shgMemberCode,seccNumber from MemberEntryEntity where length(seccNumber)>0 and MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag " +
        " union all select distinct shgCode,shgMemberCode,seccNumber from MemberEntryEntity where MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag" +
        " and shgMemberCode in  (select  shgMemberCode from MemberEntryEntity where MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag  " +
        "  except  select   shgMemberCode from MemberEntryEntity where  MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation and MemberEntryEntity.flagSyncStatus=:syncFlag and length(seccNumber)>0)")
List<ShgAndMemberDataBean> getDistinctShgAndMemberToSync(String entryCompleteConfirmation, String syncFlag);




    @Query("select entryYearCode,entryMonthCode,entryCreatedDate,activityCode," +
            "incomeFrequencyCode,incomeRangCode,sectorDate,flagBeforeAfterNrlm" +
            " from MemberEntryEntity where MemberEntryEntity.flagSyncStatus =:syncFlag " +
            "and (MemberEntryEntity.shgCode=:shgCode and (MemberEntryEntity.shgMemberCode=:memberCode" +
            " and MemberEntryEntity.entryCompleteConfirmation =:entryCompleteConfirmation )) ")
    List<ActivityDataBean> getActivityData(String shgCode, String memberCode,String entryCompleteConfirmation, String syncFlag);

    @Query("update memberentryentity set flagSyncStatus='1' where flagSyncStatus='0'")
     void updateSyncStatus();

    @Query("select seccNumber from memberentryentity where shgMemberCode=:memberCode limit 1")
    String checkSeccNumberInDb(String memberCode);



    @Query("delete from memberentryentity  where shgCode=:shgCode and (shgMemberCode=:memberCode" +
            " and (flagBeforeAfterNrlm=:entryType and (flagSyncStatus='0'" +
            " and (sectorDate=:sectorCode and activityCode=:activityCode) )))")
    void deleteDuplicateEntries(String shgCode, String memberCode, String sectorCode, String activityCode, String entryType);

    @Query("select * from MemberEntryEntity where shgMemberCode =:memberCode and MemberEntryEntity.flagBeforeAfterNrlm=:entryStatus")
    List<MemberEntryEntity> getAllSelectedMember(String memberCode ,String entryStatus);


    @Query("DELETE  from MemberEntryEntity where MemberEntryEntity.shgMemberCode = :memberCode and MemberEntryEntity.activityCode=:activityCode ")
    void deleteSelectedActivity(String memberCode,String activityCode);


    @Query("select * from MemberEntryEntity where MemberEntryEntity.shgMemberCode = :memberCode and MemberEntryEntity.flagBeforeAfterNrlm=:entryStatus")
    List<MemberEntryEntity> getAllDataWithEntryStatus(String memberCode, String entryStatus);

    @Query("update MemberEntryEntity set entryCompleteConfirmation='1' where shgMemberCode =:shgMemberCode and flagBeforeAfterNrlm=:entryFlag")
    void updateConfirmationStatus(String shgMemberCode, String entryFlag);

}

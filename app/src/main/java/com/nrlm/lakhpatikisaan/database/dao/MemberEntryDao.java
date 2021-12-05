package com.nrlm.lakhpatikisaan.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.ActivityDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberDataToCheckDup;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgAndMemberDataBean;
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

    @Query("select shgCode,shgMemberCode,sectorDate,activityCode  from MemberEntryEntity " +
            "where MemberEntryEntity.flagBeforeAfterNrlm =:entryFlag and MemberEntryEntity.flagSyncStatus=:syncFlag")
    List<MemberDataToCheckDup> getDataToCheckDuplicate(String entryFlag, String syncFlag);

    @Query("select distinct shgCode,shgMemberCode from MemberEntryEntity " +
            "where MemberEntryEntity.flagBeforeAfterNrlm =:entryFlag and MemberEntryEntity.flagSyncStatus=:syncFlag ")
    List<ShgAndMemberDataBean> getDistinctShgAndMemberToSync(String entryFlag, String syncFlag);

    @Query("select entryYearCode,entryMonthCode,entryCreatedDate,activityCode," +
            "incomeFrequencyCode,incomeRangCode,sectorDate,flagBeforeAfterNrlm" +
            " from MemberEntryEntity where MemberEntryEntity.flagSyncStatus =:syncFlag " +
            "and (MemberEntryEntity.shgCode=:shgCode and (MemberEntryEntity.shgMemberCode=:memberCode" +
            " and MemberEntryEntity.flagBeforeAfterNrlm =:entryFlag )) ")
    List<ActivityDataBean> getActivityData(String shgCode, String memberCode,String entryFlag, String syncFlag);
}
/*{
                        "entry_year": "2021",
                        "entry_month": "12",
                        "created_on_android": "20/12/2021 00:12:32.786765",
                        "activity_code": "1",
                        "frequency_code": "1",
                        "range_code": "1",
                        "sector_code": "1",
                        "flag_before_after_nrlm": "B"

}*/
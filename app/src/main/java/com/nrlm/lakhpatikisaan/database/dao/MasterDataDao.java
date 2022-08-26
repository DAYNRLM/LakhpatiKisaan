package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.BlockDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ClfDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.InActiveMember;
import com.nrlm.lakhpatikisaan.database.dbbean.LgdVillageCode;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VillageDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VoDataBean;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;


import java.util.List;

@Dao
public interface MasterDataDao {

    @Insert()
    void insertAll(List<MasterDataEntity> masterDataEntities);


    @Query("select DISTINCT MasterDataEntity.block_code ,MasterDataEntity.block_name  from MasterDataEntity ")
    List<BlockDataBean> getAllBlock();

    /*322249*/

    @Query("select  shg_code,member_code,member_name,last_entry_before_nrlm,last_entry_after_nrlm,aadhaar_verified_status,status from MasterDataEntity where shg_code=:shgCode   order by last_entry_before_nrlm is not null , last_entry_after_nrlm is not null")
    List<MemberListDataBean> getMemberListData(String shgCode);

    /*3120002*/
    @Query("select distinct gp_code,gp_name from MasterDataEntity where block_code=:blockCode")
    List<GpDataBean> getGpListData(String blockCode);

    /*3120002002*/
    @Query("select distinct village_code,village_name from MasterDataEntity where gp_code=:gpCode")
    List<VillageDataBean> getVillageListData(String gpCode);

    /*3120002002002*/
    @Query("select distinct shg_code,shg_name from MasterDataEntity where village_code=:villageCode")
    List<ShgDataBean> getShgListData(String villageCode);

    @Query("select distinct member_name from MasterDataEntity where member_code=:memberCode")
    String getMemberNameDB(String memberCode);

    @Query("select distinct shg_name from MasterDataEntity where shg_code=:shgCode")
    String getShgNameDB(String shgCode);


    @Query("select distinct member_joining_date from MasterDataEntity where member_code=:memberCode")
    String getMemberJoiningDate(String memberCode);
    @Query("select distinct belonging_name from MasterDataEntity where member_code=:memberCode")
    String getMemberBelonging(String memberCode);


    @Query("select count( member_code) from MasterDataEntity where shg_code=:shgCode")
    Integer getMemberCount(String shgCode);

    /*change the querry of below two */
    @Query("select count( member_code) from MasterDataEntity where shg_code=:shgCode and last_entry_before_nrlm IS NOT NULL")
    Integer getBeforeEntryMemberCount(String shgCode);

    @Query("select count( member_code) from MasterDataEntity where shg_code=:shgCode and last_entry_after_nrlm IS NOT NULL")
    Integer getAfterEntryMemberCount(String shgCode);

    @Query("select distinct clf_code,clf_name from MasterDataEntity")
    List<ClfDataBean> getUniqueClf();

    @Query("select distinct vo_code,vo_name from MasterDataEntity where clf_code=:clfCode")
    List<VoDataBean> getUniqueVo(String clfCode);

    @Query("select distinct shg_code,shg_name from MasterDataEntity where vo_code=:voCode")
    List<ShgDataBean> getShgDataWithVo(String voCode);

    @Query("select distinct lgd_village_code from masterdataentity")
    List<LgdVillageCode> getLgdVillageCodes();


    @Query("select member_joining_date from MasterDataEntity where member_code=:memberCode")
    String getMemberDOJ(String memberCode);




    /* delete queries-----*/

    @Query("delete from masterdataentity")
    void deleteAll();


    @Query("select distinct secc_no_flag from MasterDataEntity where MasterDataEntity.member_code=:memberCode ")
    String getSeccStatus(String memberCode);


    @Query("UPDATE masterdataentity SET last_entry_before_nrlm = :date WHERE member_code =:memberCode ")
    void updateBeforeEntryDateInLocal(String memberCode, String date);

    @Query("UPDATE masterdataentity SET last_entry_after_nrlm = :date WHERE member_code =:memberCode ")
    void updateAfterEntryDateInLocal(String memberCode, String date);


    @Query("select last_entry_before_nrlm from MasterDataEntity where member_code=:memberCode ")
    String getBeforeEntryDate(String memberCode);

    @Query("select last_entry_after_nrlm from MasterDataEntity where member_code=:memberCode ")
    String getAfterEntryDate(String memberCode);


    @Query("select distinct aadhaar_verified_status from MasterDataEntity where member_code=:memberCode")
    String getAadharStatus(String memberCode);

    @Query("UPDATE masterdataentity SET aadhaar_verified_status = :status WHERE member_code =:memberCode ")
    void updateAadharStatus(String memberCode, String status);

    @Query("select count(*) from (select distinct shg_code from MasterDataEntity)")
    String getaTotalShg();

    @Query("select count(*) from (select distinct member_code from MasterDataEntity)")
    String getTotalMember();

    @Query("select count(*) from (select  distinct shgMemberCode from MemberEntryEntity where flagSyncStatus is 0 and flagBeforeAfterNrlm='A')")
    String getLocalBeforeEntry();
    @Query(" select count(*) from (select distinct shgCode from MemberEntryEntity where flagBeforeAfterNrlm ='A' and flagSyncStatus='0')")
    String getLocalAfterEntry();

     @Query("select count(*) from(Select * from MasterDataEntity where last_entry_before_nrlm not null )")
     String getServerBeforeEntry();

    @Query("select count(*) from(Select * from MasterDataEntity where last_entry_after_nrlm not null )")
    String getServerAfterEntry();
    @Query("update masterdataentity set status = :status where member_code = :memberCode")
    void setStatus(String status ,String memberCode);


    @Query("select count(*) from (select distinct  shg_Code member_code from masterdataentity where  last_entry_after_nrlm not null and last_entry_before_nrlm not null)")
    String getWhoseAllMemberCompleted();

    @Query("select count(*) from( select distinct member_code from MasterDataEntity where member_code =:memberCode and  (clf_code is   null or vo_code is  null))")
    String getMemberIsNotInClfAndVo(String memberCode);


    @Query("select count(*) from (select distinct  shg_Code from masterdataentity where  last_entry_after_nrlm is  null and last_entry_before_nrlm is null)")
    String getWhoseAtleastOneMemberLeft();


    @Query("select count(*) from(Select distinct member_code from MasterDataEntity where last_entry_before_nrlm not null and last_entry_after_nrlm not null )")
    String getSurveyCompleted();

    @Query("select count(*) from(Select distinct member_code from MasterDataEntity where last_entry_after_nrlm is NULL )")
    String getSurveyPending();

    @Query("select shg_code, member_code, village_code from masterdataentity where status is'InActive'")
    List<InActiveMember> getDataOfInactiveMember();

    @Query("delete from masterdataentity where status is 'InActive'")
    void deleteInActivityMember();

}


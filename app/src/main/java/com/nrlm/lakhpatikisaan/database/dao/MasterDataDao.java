package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.dbbean.BlockDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;


import java.util.List;

@Dao
public interface MasterDataDao {

    @Insert()
    void insertAll(List<MasterDataEntity> masterDataEntities);


    @Query("select DISTINCT MasterDataEntity.block_code ,MasterDataEntity.block_name  from MasterDataEntity ")
    List<BlockDataBean> getAllBlock();

    /*322249*/

    @Query("select shg_code,member_code,member_name,last_entry_before_nrlm,last_entry_after_nrlm from MasterDataEntity where shg_code=:shgCode")
    List<MemberListDataBean> getMemberListData(String shgCode);

    @Query("select gp_code,gp_name from MasterDataEntity where block_code=:blockCode")
    List<GpDataBean> getGpListData(String blockCode);
}


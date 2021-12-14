package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;

import java.util.List;

@Dao
public interface IncomeRangeDao {
    @Insert
    void insert(IncomeRangeEntity incomeRangeEntity);

    @Query("select * from IncomeRangeEntity where IncomeRangeEntity.frequency_id =:freqId")
    List<IncomeRangeEntity> getAllIncomeRange(int freqId);

    @Query("delete from incomerangeentity")
    void deleteAll();
}

package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;

@Dao
public interface IncomeRangeDao {
    @Insert
    void insert(IncomeRangeEntity incomeRangeEntity);
}

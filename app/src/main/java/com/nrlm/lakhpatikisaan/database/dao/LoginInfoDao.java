package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.nrlm.lakhpatikisaan.database.entity.LoginInfoEntity;


@Dao
public interface LoginInfoDao {
    @Insert
    void insert(LoginInfoEntity loginInfoEntity);

}

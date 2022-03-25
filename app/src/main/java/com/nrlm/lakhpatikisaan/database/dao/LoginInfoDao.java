package com.nrlm.lakhpatikisaan.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.lakhpatikisaan.database.entity.LoginInfoEntity;


@Dao
public interface LoginInfoDao {

    @Insert
    void insert(LoginInfoEntity loginInfoEntity);

    @Query("select distinct  state_short_name from logininfoentity")
    String getStateNameDB();
    @Query("select distinct language_id from loginInfoEntity")
    String getLanguageCode();

    @Query("select distinct server_date_time from logininfoentity")
    String getServerDateTime();

    @Query("select distinct logout_days from logininfoentity")
    String getLogoutDays();

    @Query("delete from logininfoentity")
    void deleteAll();

}

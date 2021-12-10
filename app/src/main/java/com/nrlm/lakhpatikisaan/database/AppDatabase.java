package com.nrlm.lakhpatikisaan.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.nrlm.lakhpatikisaan.database.dao.ActivityDao;
import com.nrlm.lakhpatikisaan.database.dao.CheckDeleteShgDao;
import com.nrlm.lakhpatikisaan.database.dao.FrequencyDao;
import com.nrlm.lakhpatikisaan.database.dao.IncomeRangeDao;
import com.nrlm.lakhpatikisaan.database.dao.LoginInfoDao;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dao.MemberEntryDao;
import com.nrlm.lakhpatikisaan.database.dao.SectorDao;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.CheckDeleteShgEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;
import com.nrlm.lakhpatikisaan.database.entity.LoginInfoEntity;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;
import com.nrlm.lakhpatikisaan.database.entity.TempEntryBeforeNrlmEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {MemberEntryEntity.class,
        TempEntryBeforeNrlmEntity.class, LoginInfoEntity.class, MasterDataEntity.class, SectorEntity.class,
        ActivityEntity.class, FrequencyEntity.class, IncomeRangeEntity.class, CheckDeleteShgEntity.class}
        , version = 5, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {



    public static final String DATABASE_NAME = "lakhpatiShg.db";
    public static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 5;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract MemberEntryDao memberEntryDao();
    public abstract LoginInfoDao getLoginInfoDao();
    public abstract MasterDataDao getMasterDataDao();
    public abstract SectorDao getSectorDao();
    public abstract ActivityDao getActivityDao();
    public abstract FrequencyDao getFrequencyDao();
    public abstract IncomeRangeDao getIncomeRangeDao();
    public abstract CheckDeleteShgDao getCheckDeleteShgDao();

    public static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}

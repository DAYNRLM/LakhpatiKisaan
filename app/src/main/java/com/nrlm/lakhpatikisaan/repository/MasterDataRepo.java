package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.ActivityDao;
import com.nrlm.lakhpatikisaan.database.dao.FrequencyDao;
import com.nrlm.lakhpatikisaan.database.dao.IncomeRangeDao;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dao.SectorDao;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SupportiveMastersResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataRepo {

    private final ExecutorService executor;
    private static MasterDataRepo instance=null;
    private Context context;
    private MasterDataDao masterDataDao;
    private SectorDao sectorDao;
    private ActivityDao activityDao;
    private FrequencyDao frequencyDao;
    private IncomeRangeDao incomeRangeDao;

    private MasterDataRepo(ExecutorService executor,Context context) {
        this.executor = executor;
        this.context=context;
        AppDatabase appDatabase=AppDatabase.getDatabase(context);
        masterDataDao=appDatabase.getMasterDataDao();
        sectorDao=appDatabase.getSectorDao();
        activityDao=appDatabase.getActivityDao();
        frequencyDao=appDatabase.getFrequencyDao();
        incomeRangeDao=appDatabase.getIncomeRangeDao();
    }

    public static MasterDataRepo getInstance(ExecutorService executor,Context context){
        if (instance==null){
            instance=new MasterDataRepo(executor,context);
        }
        return instance;
    }



    public synchronized void makeMasterDataRequest(final LogRequestBean logRequestObject,
                                 final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callMasterDataApi(logRequestObject, new ServiceCallback<Result>() {
                        @Override

                        public void success(Result<Result> successResponse) {
                            /*fill data into db*/
                            if (successResponse instanceof Result.Success){
                                MasterDataResponseBean masterDataResponseBean= (MasterDataResponseBean) ((Result.Success) successResponse).data;
                                List<MasterDataEntity> masterDataEntityList=new ArrayList<>();
                                for (MasterDataResponseBean.MasterData masterData: masterDataResponseBean.getLocation_master()){
                                    MasterDataEntity masterDataEntity=new MasterDataEntity(masterData.getBlock_name(),masterData.getBlock_code(),masterData.getGp_code(),masterData.getGp_name(),masterData.getVillage_code(),
                                            masterData.getVillage_name(),masterData.getShg_name(),masterData.getShg_code(),masterData.getMember_code(),masterData.getMember_name(),masterData.getClf_code(),
                                            masterData.getClf_name(),masterData.getVo_code(),masterData.getVo_name(),masterData.getMember_joining_date(),
                                            masterData.getLast_entry_after_nrlm(),masterData.getLast_entry_before_nrlm());
                                    masterDataEntityList.add(masterDataEntity);
                                }
                                insertAllMasterData(masterDataEntityList);
                            }
                            repositoryCallback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<Result> errorResponse) {
                            repositoryCallback.onComplete(errorResponse);
                        }
                    });

                }catch (Exception e){
                    Result<Result> errorResult = new Result.Error(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });

    }



    public synchronized void makeSupportiveMasterDataRequest(final LogRequestBean logRequestObject,
                                                final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callSupportiveMasterDataApi(logRequestObject, new ServiceCallback<Result>() {
                        @Override
                        public void success(Result<Result> successResponse) {
                            /*fill data into db*/
                            if (successResponse instanceof Result.Success){
                                SupportiveMastersResponseBean supportiveMastersResponseBean= (SupportiveMastersResponseBean) ((Result.Success) successResponse).data;

                                for (SupportiveMastersResponseBean.Sector sector:supportiveMastersResponseBean.getSectors()){
                                    SectorEntity sectorEntity=new SectorEntity(sector.getSector_name(),sector.getSector_code());
                                    insertSector(sectorEntity);
                                    for (SupportiveMastersResponseBean.Activity activity:sector.getActivities()){
                                          ActivityEntity activityEntity=new ActivityEntity(activity.getActivity_name(),sector.getSector_code()
                                                  ,activity.getActivity_code());
                                          insertActivity(activityEntity);
                                    }
                                }

                                for (SupportiveMastersResponseBean.IncomeFrequency incomeFrequency:supportiveMastersResponseBean.getIncome_frequencies()){
                                       FrequencyEntity frequencyEntity=new FrequencyEntity(incomeFrequency.getFrequency_id(),incomeFrequency.getFrequency_name());
                                       insertFrequency(frequencyEntity);
                                       for (SupportiveMastersResponseBean.IncomeRange incomeRange:incomeFrequency.getIncome_range()){
                                               IncomeRangeEntity incomeRangeEntity=new IncomeRangeEntity(incomeFrequency.getFrequency_id(),
                                                       incomeRange.getRange_id(),incomeRange.getRange_name());
                                               insertIncomeRange(incomeRangeEntity);
                                       }
                                }

                            }

                            repositoryCallback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<Result> errorResponse) {
                            repositoryCallback.onComplete(errorResponse);
                        }
                    });
                }catch (Exception e){
                    Result<Result> errorResult = new Result.Error(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });

    }


    private void callMasterDataApi(final LogRequestBean logRequestObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.masterDataApi(logRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("MasterDataResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                       MasterDataResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), MasterDataResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        MasterDataResponseBean masterDataResponseBean = new Gson().fromJson(response.body(), MasterDataResponseBean.class);
                        serviceCallback.success( new Result.Success(masterDataResponseBean));
                    }

                }else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code()+" "+response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("FailureFromServer"+t.toString(),MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    private void callSupportiveMasterDataApi(final LogRequestBean logRequestObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.supportiveMasterDataApi(logRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("SupportiveMasterDataResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        SupportiveMastersResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), SupportiveMastersResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        SupportiveMastersResponseBean supportiveMastersResponseBean = new Gson().fromJson(response.body(), SupportiveMastersResponseBean.class);
                        serviceCallback.success( new Result.Success(supportiveMastersResponseBean));
                    }

                }else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code()+" "+response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("SupportiveFailureFromServer"+t.toString(),MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    private void insertAllMasterData(List<MasterDataEntity> masterDataEntityList) {

        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                masterDataDao.insertAll(masterDataEntityList);
            }
        });
    }

    private void insertSector(SectorEntity sectorEntity){
     AppDatabase.databaseWriteExecutor.execute(new Runnable() {
         @Override
         public void run() {
             sectorDao.insert(sectorEntity);
         }
     });
    }

    private void insertActivity(ActivityEntity activityEntity){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDao.insert(activityEntity);
            }
        });
    }

    private void insertFrequency(FrequencyEntity frequencyEntity){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                frequencyDao.insert(frequencyEntity);
            }
        });
    }

    private void insertIncomeRange(IncomeRangeEntity incomeRangeEntity){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                incomeRangeDao.insert(incomeRangeEntity);
            }
        });
    }



    /*********added by lincon***********/

    public List<SectorEntity> getAllSector(){
        List<SectorEntity> sectorData=null;
        try {
            Callable<List<SectorEntity>> listCallable = new Callable<List<SectorEntity>>() {
                @Override
                public List<SectorEntity> call() throws Exception {
                    return sectorDao.getAllSector();
                }
            };
            Future<List<SectorEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            sectorData = future.get();

        }catch (Exception e){

        }
        return sectorData;
    }



    public List<String> getSectorName(){

        List<String> sectorName= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sectorName = getAllSector().stream().map(SectorEntity::getSector_name).collect(Collectors.toList());
        }

        return sectorName;
    }
}

/*{

                                }*/

package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
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
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataRepo {

    private final ExecutorService executor;
    private static MasterDataRepo instance=null;
    private Context context;
    private MasterDataDao masterDataDao;

    private MasterDataRepo(ExecutorService executor,Context context) {
        this.executor = executor;
        this.context=context;
        masterDataDao=AppDatabase.getDatabase(context).getMasterDataDao();
    }

    public static MasterDataRepo getInstance(ExecutorService executor,Context context){
        if (instance==null){
            instance=new MasterDataRepo(executor,context);
        }
        return instance;
    }



    public void makeMasterDataRequest(final LogRequestBean logRequestObject,
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
                                    MasterDataEntity masterDataEntity=new MasterDataEntity(masterData.getBlock_name(),masterData.getGp_name(),masterData.getVillage_code(),
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



    public void makeSupportiveMasterDataRequest(final LogRequestBean logRequestObject,
                                                final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callSupportiveMasterDataApi(logRequestObject, new ServiceCallback<Result>() {
                        @Override
                        public void success(Result<Result> successResponse) {
                            /*fill data into db*/
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
                AppUtils.getInstance().showLog("FailureFromServer"+t.toString(),MasterDataRepo.class);
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
}

package com.nrlm.lakhpatikisaan.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDuplicateResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataRepo {

    private final ExecutorService executor;
    private static SyncDataRepo instance=null;

    private SyncDataRepo(ExecutorService executor) {
        this.executor = executor;
    }

    public static SyncDataRepo getInstance(ExecutorService executor){
        if (instance==null){
            instance=new SyncDataRepo(executor);
        }
        return instance;
    }

    public void makeCheckDuplicateRequest(final CheckDuplicateRequestBean checkDuplicateRequestBean,
                                          final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callCheckDuplicateDataApi(checkDuplicateRequestBean, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
                        /*call sync data api*/
                        repositoryCallback.onComplete(successResponse);
                    }

                    @Override
                    public void error(Result<Result> errorResponse) {
                        repositoryCallback.onComplete(errorResponse);
                    }
                });
            }
        });
    }


    public void makeSyncEntriesRequest(final SyncEntriesRequestBean syncEntriesRequestBean,
                                       final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callSyncEntriesDataApi(syncEntriesRequestBean, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
                        /*update the db*/
                        repositoryCallback.onComplete(successResponse);
                    }

                    @Override
                    public void error(Result<Result> errorResponse) {
                        repositoryCallback.onComplete(errorResponse);
                    }
                });
            }
        });
    }


    private void callCheckDuplicateDataApi(final CheckDuplicateRequestBean checkDuplicateRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.checkDuplicateDataApi(checkDuplicateRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("checkDuplicateDataResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        CheckDuplicateResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), CheckDuplicateResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        CheckDuplicateResponseBean checkDuplicateResponseBean = new Gson().fromJson(response.body(), CheckDuplicateResponseBean.class);
                        serviceCallback.success( new Result.Success(checkDuplicateResponseBean));
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

    private void callSyncEntriesDataApi(final SyncEntriesRequestBean syncEntriesRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.syncEntriesDataApi(syncEntriesRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("SyncEntriesDataResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        SimpleResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), SimpleResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        SimpleResponseBean simpleResponseBean = new Gson().fromJson(response.body(), SimpleResponseBean.class);
                        serviceCallback.success( new Result.Success(simpleResponseBean));
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


}

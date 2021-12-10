package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.CheckDeleteShgDao;
import com.nrlm.lakhpatikisaan.database.dao.LoginInfoDao;
import com.nrlm.lakhpatikisaan.database.entity.CheckDeleteShgEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.DeleteShgRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDeleteShgResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckAndDeleteShgRepo {

    private final ExecutorService executor;
    private static CheckAndDeleteShgRepo instance=null;
    private Context context;
    private CheckDeleteShgDao checkDeleteShgDao;


    private CheckAndDeleteShgRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        this.context=context;
        checkDeleteShgDao=AppDatabase.getDatabase(context).getCheckDeleteShgDao();
    }

    public static CheckAndDeleteShgRepo getInstance(ExecutorService executor,Context context){
        if (instance==null){
            instance=new CheckAndDeleteShgRepo(executor,context);
        }
        return instance;
    }

    public void makeCheckDeleteShgApiRequest(final LogRequestBean logRequestBean, final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callCheckDeleteShgApi(logRequestBean, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
                        CheckDeleteShgResponseBean checkDeleteShgResponseBean=(CheckDeleteShgResponseBean) ((Result.Success) successResponse).data;
                        List<CheckDeleteShgEntity> checkDeleteShgEntityList=new ArrayList<>();
                        for (CheckDeleteShgResponseBean.DeletedShgData deletedShgData:checkDeleteShgResponseBean.getShg_data()){
                            checkDeleteShgEntityList.add(new CheckDeleteShgEntity(deletedShgData.getShg_code()));
                        }
                        checkDeleteShgDao.insertAll(checkDeleteShgEntityList);

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

    public void makeDeleteShgApiRequest(final DeleteShgRequestBean deleteShgRequestBean,final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callDeleteShgApi(deleteShgRequestBean, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
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

    private void callCheckDeleteShgApi(final LogRequestBean logRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.checkDeleteShgApi(logRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("checkDeleteShgApiResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        CheckDeleteShgResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), CheckDeleteShgResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        CheckDeleteShgResponseBean checkDeleteShgResponseBean = new Gson().fromJson(response.body(), CheckDeleteShgResponseBean.class);
                        serviceCallback.success( new Result.Success(checkDeleteShgResponseBean));
                    }

                }else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code()+" "+response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("ServerFailureInLoginApi"+t.toString(),MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }



    private void callDeleteShgApi(final DeleteShgRequestBean deleteShgRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.deleteShgApi(deleteShgRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("DeleteShgApiResponse"+response.toString(), MasterDataRepo.class);
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
                AppUtils.getInstance().showLog("ServerFailureInLoginApi"+t.toString(),MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    public List<CheckDeleteShgEntity> getShgToDelete(){
        return checkDeleteShgDao.getShgToDelete();
    }

}

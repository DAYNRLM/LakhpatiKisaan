package com.nrlm.lakhpatikisaan.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataRepo {

    private final ExecutorService executor;
    private static MasterDataRepo instance=null;

    private MasterDataRepo(ExecutorService executor) {
        this.executor = executor;
    }

    public static MasterDataRepo getInstance(ExecutorService executor){
        if (instance==null){
            instance=new MasterDataRepo(executor);
        }
        return instance;
    }



    public void makeMasterDataRequest(final LogRequestBean logRequestObject,
                                 final RepositoryCallback<MasterDataResponseBean> repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    loginRequest(logRequestObject, new ServiceCallback<MasterDataResponseBean>() {
                        @Override
                        public void success(Result<MasterDataResponseBean> successResponse) {
                            repositoryCallback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<MasterDataResponseBean> errorResponse) {
                            repositoryCallback.onComplete(errorResponse);
                        }
                    });

                }catch (Exception e){
                    Result<MasterDataResponseBean> errorResult = new Result.Error<>(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });

    }


    public void loginRequest(final LogRequestBean logRequestObject, final ServiceCallback<MasterDataResponseBean> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.masterDataRequest(logRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    MasterDataResponseBean masterDataResponseBean = new Gson().fromJson(response.body(), MasterDataResponseBean.class);
                    Log.d("DataResponse", masterDataResponseBean.toString());
                    serviceCallback.success( new Result.Success<MasterDataResponseBean>(masterDataResponseBean));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Failure", t.toString());
                serviceCallback.error(new Result.Error<>(t));
            }
        });
    }


}

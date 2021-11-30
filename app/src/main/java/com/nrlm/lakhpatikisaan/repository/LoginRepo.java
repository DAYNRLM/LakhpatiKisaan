package com.nrlm.lakhpatikisaan.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.ContactsResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {

    private final ExecutorService executor;
    private static LoginRepo instance=null;

    private LoginRepo(ExecutorService executor) {
        this.executor = executor;
    }

    public static LoginRepo getInstance(ExecutorService executor){
        if (instance==null){
            instance=new LoginRepo(executor);
        }
        return instance;
    }



    public void makeLoginRequest(final LoginRequestBean loginRequestObject,
                                 final RepositoryCallback<LoginResponseBean> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    loginRequest(loginRequestObject, new ServiceCallback<LoginResponseBean>() {
                        @Override
                        public void success(Result<LoginResponseBean> successResponse) {
                            callback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<LoginResponseBean> errorResponse) {
                            callback.onComplete(errorResponse);
                        }
                    });

                }catch (Exception e){
                    Result<LoginResponseBean> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });

    }


    private void loginRequest(final LoginRequestBean loginRequestObject, final ServiceCallback<LoginResponseBean> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.loginRequest(loginRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    LoginResponseBean loginResponseBean = new Gson().fromJson(response.body(), LoginResponseBean.class);
                    Log.d("DataResponse", loginResponseBean.toString());
                    serviceCallback.success( new Result.Success<LoginResponseBean>(loginResponseBean));
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

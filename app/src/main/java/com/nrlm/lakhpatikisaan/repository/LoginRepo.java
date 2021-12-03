package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.LoginInfoDao;
import com.nrlm.lakhpatikisaan.database.entity.LoginInfoEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {

    private final ExecutorService executor;
    private static LoginRepo instance=null;
    private Context context;
    private  LoginInfoDao loginInfoDao;

    private LoginRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        this.context=context;
        loginInfoDao=AppDatabase.getDatabase(context).getLoginInfoDao();
    }

    public static LoginRepo getInstance(ExecutorService executor,Context context){
        if (instance==null){
            instance=new LoginRepo(executor,context);
        }
        return instance;
    }


    public void makeLoginRequest(final LoginRequestBean loginRequestObject,
                                 final RepositoryCallback repositoryCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                  loginRequest(loginRequestObject, new ServiceCallback<Result>() {
                      @Override
                      public void success(Result<Result> successResponse) {
                          /*fill data into db*/
                          if (successResponse instanceof Result.Success){
                              LoginResponseBean loginResponseBean= (LoginResponseBean) ((Result.Success) successResponse).data;
                              LoginInfoEntity loginInfoEntity=new LoginInfoEntity(loginResponseBean.getLogin_id(),loginResponseBean.getPassword(),
                                      loginResponseBean.getMobile_number(),loginResponseBean.getState_code(),loginResponseBean.getState_short_name(),
                                      loginResponseBean.getServer_date_time(),loginResponseBean.getLanguage_id(),loginResponseBean.getLogin_attempt(),
                                      loginResponseBean.getLogout_days());

                              insertLoginInfo(loginInfoEntity);


                          }
                          repositoryCallback.onComplete(successResponse);
                      }

                      @Override
                      public void error(Result<Result> errorResponse) {
                          repositoryCallback.onComplete(errorResponse);
                      }
                  });

                }catch (Exception e){
                    Result<LoginResponseBean> errorResult = new Result.Error(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });

    }


    private void loginRequest(final LoginRequestBean loginRequestObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.loginApi(loginRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("LoginDataResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        LoginResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), LoginResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        LoginResponseBean loginResponseBean = new Gson().fromJson(response.body(), LoginResponseBean.class);
                        serviceCallback.success( new Result.Success(loginResponseBean));
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

    private void insertLoginInfo(LoginInfoEntity loginInfoEntity){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                loginInfoDao.insert(loginInfoEntity);
            }
        });
    }

}

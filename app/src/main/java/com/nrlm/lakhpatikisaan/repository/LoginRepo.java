package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.LoginInfoDao;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dbbean.LgdVillageCode;
import com.nrlm.lakhpatikisaan.database.entity.LoginInfoEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.OtpRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.ResetPasswordBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {

    private final ExecutorService executor;
    private static LoginRepo instance = null;
    private Context context;
    private LoginInfoDao loginInfoDao;
    private MasterDataDao masterDataDao;

    private LoginRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        this.context = context;
        loginInfoDao = AppDatabase.getDatabase(context).getLoginInfoDao();
        masterDataDao = AppDatabase.getDatabase(context).getMasterDataDao();
    }

    public static LoginRepo getInstance(ExecutorService executor, Context context) {
        if (instance == null) {
            instance = new LoginRepo(executor, context);
        }
        return instance;
    }

    public void resetPasswordRequestLog(final ResetPasswordBean resetPasswordBean, final RepositoryCallback repositoryCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                resetpassRequest(resetPasswordBean, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
                        if (successResponse instanceof Result.Success) {
                            SimpleResponseBean simpleResponseBean = (SimpleResponseBean) ((Result.Success) successResponse).data;
                            AppUtils.getInstance().showLog("LoginRepo " + simpleResponseBean.toString(), LoginRepo.class);
                        }

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

    public void makeLoginRequest(final LoginRequestBean loginRequestObject,
                                 final RepositoryCallback repositoryCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callLoginRequest(loginRequestObject, new ServiceCallback<Result>() {
                        @Override
                        public void success(Result<Result> successResponse) {
                            /*fill data into db*/
                            if (successResponse instanceof Result.Success) {
                                LoginResponseBean loginResponseBean = (LoginResponseBean) ((Result.Success) successResponse).data;
                                LoginInfoEntity loginInfoEntity = new LoginInfoEntity(loginResponseBean.getLogin_id(), loginResponseBean.getPassword(),
                                        loginResponseBean.getMobile_number(), loginResponseBean.getState_code(), loginResponseBean.getState_short_name(),
                                        loginResponseBean.getServer_date_time(), loginResponseBean.getLanguage_id(), loginResponseBean.getLogin_attempt(),
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

                } catch (Exception e) {
                    Result<LoginResponseBean> errorResult = new Result.Error(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });
    }

    private void resetpassRequest(final ResetPasswordBean resetPasswordBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.resetPasswordApi(resetPasswordBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("reset Password response" + response.toString(), LoginRepo.class);
                if (response.isSuccessful()) {
                    SimpleResponseBean simpleResponseBean = new Gson().fromJson(response.body(), SimpleResponseBean.class);
                    serviceCallback.success(new Result.Success(simpleResponseBean));

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("resetpassRequest" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }


    private void callLoginRequest(final LoginRequestBean loginRequestObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.loginApi(loginRequestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("LoginDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")) {
                        LoginResponseBean.Error error = new Gson().fromJson(response.body().getAsJsonObject("error"), LoginResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        LoginResponseBean loginResponseBean = new Gson().fromJson(response.body(), LoginResponseBean.class);
                        serviceCallback.success(new Result.Success(loginResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("ServerFailureInLoginApi" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    public void callOtpServices(final OtpRequestBean otpRequestBean, final RepositoryCallback repositoryCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                otpService(otpRequestBean, new ServiceCallback<Result>() {

                    @Override
                    public void success(Result<Result> successResponse) {

                    }

                    @Override
                    public void error(Result<Result> errorResponse) {

                    }
                });
            }
        });

    }

    private void otpService(final OtpRequestBean otpRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.otpApi(otpRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("Otp Response" + response.toString(), LoginRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")) {
                        /*LoginResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), LoginResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));*/
                    } else {
                       /* LoginResponseBean loginResponseBean = new Gson().fromJson(response.body(), LoginResponseBean.class);
                        serviceCallback.success( new Result.Success(loginResponseBean));*/
                        //REsult
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("ServerFailureInLoginApi" + t.toString(), LoginRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }


    private void insertLoginInfo(LoginInfoEntity loginInfoEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                loginInfoDao.insert(loginInfoEntity);
            }
        });

    }

    public List<LgdVillageCode> getLgdVillageCodes() throws ExecutionException, InterruptedException {
        Callable<List<LgdVillageCode>> callable = new Callable<List<LgdVillageCode>>() {
            @Override
            public List<LgdVillageCode> call() throws Exception {
                return masterDataDao.getLgdVillageCodes();
            }
        };

        Future<List<LgdVillageCode>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public String getStateNameDB() throws ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loginInfoDao.getStateNameDB();
            }
        };

        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


}
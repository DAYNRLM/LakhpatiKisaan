package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.ActivityDao;
import com.nrlm.lakhpatikisaan.database.dao.CheckDeleteShgDao;
import com.nrlm.lakhpatikisaan.database.dao.FrequencyDao;
import com.nrlm.lakhpatikisaan.database.dao.IncomeRangeDao;
import com.nrlm.lakhpatikisaan.database.dao.LoginInfoDao;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dao.SeccDao;
import com.nrlm.lakhpatikisaan.database.dao.SectorDao;
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
import com.nrlm.lakhpatikisaan.network.model.response.OtpResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.view.auth.SignUpFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {

    private final ExecutorService executor;
    private static LoginRepo instance = null;
    private Context context;
    private LoginInfoDao loginInfoDao;
    private MasterDataDao masterDataDao;
    private SeccDao seccDao;
    private SectorDao sectorDao;
    private ActivityDao activityDao;
    private IncomeRangeDao incomeRangeDao;
    private FrequencyDao frequencyDao;
    private CheckDeleteShgDao checkDeleteShgDao;

    private LoginRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        this.context = context;
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        loginInfoDao = appDatabase.getLoginInfoDao();
        masterDataDao = appDatabase.getMasterDataDao();
        seccDao = appDatabase.getSeccDao();
        sectorDao = appDatabase.getSectorDao();
        activityDao = appDatabase.getActivityDao();
        incomeRangeDao = appDatabase.getIncomeRangeDao();
        frequencyDao = appDatabase.getFrequencyDao();
        checkDeleteShgDao = appDatabase.getCheckDeleteShgDao();
    }

    public static LoginRepo getInstance(ExecutorService executor, Context context) {
        if (instance == null) {
            instance = new LoginRepo(executor, context);
        }
        return instance;
    }

    public void resetPasswordRequestLog(final ResetPasswordBean resetPasswordBean, final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(resetPasswordBean);

            encryptedObject.addProperty("data",cryptography.encrypt(logreq));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                resetpassRequest(encryptedObject, new ServiceCallback<Result>() {
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
    public void callOtpServices(final OtpRequestBean otpRequestBean, final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(otpRequestBean);

            encryptedObject.addProperty("data",cryptography.encrypt(logreq.toString()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                otpService(encryptedObject, new ServiceCallback<Result>() {

                    @Override
                    public void success(Result<Result> successResponse) {
                        if (successResponse instanceof Result.Success) {
                            OtpResponseBean otpRequestBean1 = (OtpResponseBean) ((Result.Success) successResponse).data;
                            AppUtils.getInstance().showLog("LoginRepo " + otpRequestBean1.toString(), LoginRepo.class);
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

    public void makeLoginRequest(final JsonObject encryptedObject,
                                 final RepositoryCallback repositoryCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callLoginRequest(encryptedObject, new ServiceCallback<Result>() {
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

    private void resetpassRequest(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.resetPasswordApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("reset Password response" + response.toString(), LoginRepo.class);
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();





                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                            AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), LoginRepo.class);
                        } catch (Exception e) {

                            AppUtils.getInstance().showLog("DecryptEx" + e, LoginRepo.class);
                        }
                    }

                    SimpleResponseBean simpleResponseBean = new Gson().fromJson(jsonObject.toString(), SimpleResponseBean.class);
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


    private void callLoginRequest(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

       /*     HashMap<String, String> headers = new HashMap<String, String>();

            headers.put(AppConstant.st, AppConstant.s);*/


        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.loginApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                AppUtils.getInstance().showLog("LoginDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();


                    try {
                        Cryptography cryptography = new Cryptography();
                        jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                        AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                    } catch (Exception e) {

                        AppUtils.getInstance().showLog("DecryptEx" + e, LoginRepo.class);
                    }
                    String code="";
                    JSONObject errorObj=null;
                    try {
                        code = jsonObject.getJSONObject("error").getString("code");
                        errorObj=jsonObject.getJSONObject("error");

                    }catch (JSONException e)
                    {
                        AppUtils.getInstance().showLog(""+e,LoginRepo.class);
                    }
                    if (response.body() == null || response.code() == 204) { // 204 is empty response

                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));

                    } else if (!code.equalsIgnoreCase("E200")) {
                        LoginResponseBean.Error error = new Gson().fromJson(errorObj.toString(), LoginResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        LoginResponseBean loginResponseBean = new Gson().fromJson(jsonObject.toString(), LoginResponseBean.class);
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



    private void otpService(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.otpApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                AppUtils.getInstance().showLog("Otp Response" + response.toString(), LoginRepo.class);
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();





                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                            AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                        } catch (Exception e) {

                            AppUtils.getInstance().showLog("DecryptEx" + e, LoginRepo.class);
                        }
                    }
                    String code="";
                    JSONObject errorObj=null;
                    try {
                        code = jsonObject.getJSONObject("error").getString("code");
                        errorObj=jsonObject.getJSONObject("error");

                    }catch (JSONException e)
                    {
                        AppUtils.getInstance().showLog(""+e,LoginRepo.class);
                    }
                    if (response.body() == null || response.code() == 204||response.code()==404) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!code.equalsIgnoreCase("E200")) {
                        OtpResponseBean.Error error = new Gson().fromJson(errorObj.toString(), OtpResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        OtpResponseBean otpResponseBean  = new Gson().fromJson(jsonObject.toString(), OtpResponseBean.class);
                        serviceCallback.success( new Result.Success(otpResponseBean));
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

    public String getServerDateTime() throws ExecutionException, InterruptedException

    {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loginInfoDao.getServerDateTime();
            }
        };

        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public String getLogoutDays() throws ExecutionException, InterruptedException

    {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loginInfoDao.getLogoutDays();
            }
        };

        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public String getLanguageCodeDB() throws ExecutionException, InterruptedException

    {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loginInfoDao.getLanguageCode();
            }
        };

        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    public void deleteAllMaster() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                loginInfoDao.deleteAll();
                seccDao.deleteAll();
                checkDeleteShgDao.deleteAll();
                masterDataDao.deleteAll();
                activityDao.deleteAll();
                frequencyDao.deleteAll();
                sectorDao.deleteAll();
                incomeRangeDao.deleteAll();
            }
        });
    }





}
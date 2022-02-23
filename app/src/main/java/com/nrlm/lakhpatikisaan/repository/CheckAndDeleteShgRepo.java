package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.view.auth.SignUpFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreqBean=gson.toJson(logRequestBean);
            encryptedObject.addProperty("data",cryptography.encrypt(logreqBean));
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
                callCheckDeleteShgApi(encryptedObject, new ServiceCallback<Result>() {
                    @Override
                    public void success(Result<Result> successResponse) {
                        CheckDeleteShgResponseBean checkDeleteShgResponseBean=(CheckDeleteShgResponseBean) ((Result.Success) successResponse).data;
                        List<CheckDeleteShgEntity> checkDeleteShgEntityList=new ArrayList<>();
                        for (CheckDeleteShgResponseBean.DeletedShgData deletedShgData:checkDeleteShgResponseBean.getShg_data()){
                            checkDeleteShgEntityList.add(new CheckDeleteShgEntity(deletedShgData.getShg_code()));
                        }

                        insertAllCheckDeleteShg(checkDeleteShgEntityList);
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
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreqBean=gson.toJson(deleteShgRequestBean);
            encryptedObject.addProperty("data",cryptography.encrypt(logreqBean));
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
                callDeleteShgApi(encryptedObject, new ServiceCallback<Result>() {
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

    private void callCheckDeleteShgApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("securityToken", "n{j5Y[<!Ps*HWCWg");
        Call<JsonObject> call = (Call<JsonObject>) apiServices.checkDeleteShgApi(headers,encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JSONObject jsonObject = null;
                Response<JsonObject> response1=response;
                String objectResponse="";
                JSONObject encryptedData=null;
                String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();





                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        Cryptography cryptography = new Cryptography();
                        jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                        AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                    } catch (Exception e) {
                        Toast.makeText(context, "Data not found!", Toast.LENGTH_SHORT).show();
                        AppUtils.getInstance().showLog("DecryptEx" + e, CheckAndDeleteShgRepo.class);
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
                AppUtils.getInstance().showLog("checkDeleteShgApiResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!code.equalsIgnoreCase("E200")){
                        CheckDeleteShgResponseBean.Error error=  new Gson().fromJson(errorObj.toString(), CheckDeleteShgResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        CheckDeleteShgResponseBean checkDeleteShgResponseBean = new Gson().fromJson(jsonObject.toString(), CheckDeleteShgResponseBean.class);
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

    private void callDeleteShgApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("securityToken", "n{j5Y[<!Ps*HWCWg");
        Call<JsonObject> call = (Call<JsonObject>) apiServices.deleteShgApi(headers,encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject jsonObject = null;

                String objectResponse="";
                JSONObject encryptedData=null;
                String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();





                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        Cryptography cryptography = new Cryptography();
                        jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                        AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), CheckAndDeleteShgRepo.class);
                    } catch (Exception e) {
                        Toast.makeText(context, "Data not found!", Toast.LENGTH_SHORT).show();
                        AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                    }
                }
                String code="";
                JSONObject errorObj=null;
                try {
                    code = jsonObject.getJSONObject("error").getString("code");
                    errorObj=jsonObject.getJSONObject("error");

                }catch (JSONException e)
                {
                    AppUtils.getInstance().showLog(""+e,MasterDataRepo.class);
                }


                AppUtils.getInstance().showLog("SeccDataResponse" + response.toString(), CheckAndDeleteShgRepo.class);

                AppUtils.getInstance().showLog("DeleteShgApiResponse"+response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if(response.body() == null || response.code() == 204){ // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }else if (!code.equalsIgnoreCase("E200")){
                        SimpleResponseBean.Error error=  new Gson().fromJson(errorObj.toString(), SimpleResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }
                    else{
                        SimpleResponseBean simpleResponseBean = new Gson().fromJson(jsonObject.toString(), SimpleResponseBean.class);
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

    public List<CheckDeleteShgEntity> getShgToDelete() throws ExecutionException, InterruptedException {

       Callable<List<CheckDeleteShgEntity>> callable=new Callable<List<CheckDeleteShgEntity>>() {
           @Override
           public List<CheckDeleteShgEntity> call() throws Exception {
               return checkDeleteShgDao.getShgToDelete();
           }
       };
        Future<List<CheckDeleteShgEntity>> future= Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    private void insertAllCheckDeleteShg(List<CheckDeleteShgEntity> checkDeleteShgEntityList){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                checkDeleteShgDao.insertAll(checkDeleteShgEntityList);
            }
        });
    }


}

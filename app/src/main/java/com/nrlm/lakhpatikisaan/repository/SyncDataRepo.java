package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.AadharDao;
import com.nrlm.lakhpatikisaan.database.dao.MemberEntryDao;
import com.nrlm.lakhpatikisaan.database.dao.MemberInActiveDao;
import com.nrlm.lakhpatikisaan.database.dbbean.AadharDbBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ActivityDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberDataToCheckDup;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgAndMemberDataBean;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberInActiveEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.MemberInActiveRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDuplicateResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
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

public class SyncDataRepo {

    private final ExecutorService executor;
    private static SyncDataRepo instance = null;
    private MemberEntryDao memberEntryDao;
    private AadharDao aadharDao;
    private MemberInActiveDao memberInActiveDao;
    Context context;

    private SyncDataRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        memberEntryDao = appDatabase.memberEntryDao();
        aadharDao= appDatabase.getAadharDao();

    }

    public static SyncDataRepo getInstance(ExecutorService executor, Context context) {
        if (instance == null) {
            instance = new SyncDataRepo(executor, context);
        }
        return instance;
    }

    public void makeCheckDuplicateRequest(final CheckDuplicateRequestBean checkDuplicateRequestBean,
                                          final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(checkDuplicateRequestBean);

            encryptedObject.addProperty("data",cryptography.encrypt(logreq));
            AppUtils.getInstance().showLog("DuplicateRequestFinal"+encryptedObject.toString(),SyncDataRepo.class);
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
                callCheckDuplicateDataApi(encryptedObject, new ServiceCallback<Result>() {
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


    public void makeSyncEntriesRequest(final SyncEntriesRequestBean syncEntriesRequestBean,
                                       final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(syncEntriesRequestBean);
            AppUtils.getInstance().showLog("Sinal Data"+logreq,SyncDataRepo.class);

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
                callSyncEntriesDataApi(encryptedObject, new ServiceCallback<Result>() {
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
    public void makeMemberInActiveRequest(final MemberInActiveRequestBean memberInActiveRequestBean,
                                          final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(memberInActiveRequestBean );
            AppUtils.getInstance().showLog("Sinal Data"+logreq,SyncDataRepo.class);

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
                callInactiveMemberApi(encryptedObject, new ServiceCallback<Result>() {
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




    private void callCheckDuplicateDataApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        /*HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(AppConstant.st, AppConstant.s);*/
        Call<JsonObject> call = (Call<JsonObject>) apiServices.checkDuplicateDataApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("checkDuplicateDataResponse" + response.toString(), SyncDataRepo.class);
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted="";
                    try {
                        getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();
                    }catch (JsonParseException e)
                    {
                        AppUtils.getInstance().showLog("errorInSyncDataApi"+e,SyncDataRepo.class);
                    }






                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                            AppUtils.getInstance().showLog("responseJSONFinal" + jsonObject.toString(), SyncDataRepo.class);
                        } catch (Exception e) {
                            AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                        }
                    }

                    AppUtils.getInstance().showLog("SyncEntriesDataResponseFinal" + response.body().toString(), SyncDataRepo.class);
                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }/*else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        CheckDuplicateResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), CheckDuplicateResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }*/ else {
                        CheckDuplicateResponseBean checkDuplicateResponseBean = new Gson().fromJson(jsonObject.toString(), CheckDuplicateResponseBean.class);
                        AppUtils.getInstance().showLog("FinalDuplicate"+new Gson().toJson(checkDuplicateResponseBean).toString(),SyncDataRepo.class);
                        serviceCallback.success(new Result.Success(checkDuplicateResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("FailureFromServer" + t.toString(), SyncDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    private void callSyncEntriesDataApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.syncEntriesDataApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted="";
                    try {
                        getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();
                    }catch (JsonParseException e)
                    {
                        AppUtils.getInstance().showLog("errorInSyncDataApi"+e,SyncDataRepo.class);
                    }






                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                            AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                        } catch (Exception e) {
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
                        AppUtils.getInstance().showLog(""+e,LoginRepo.class);
                    }
                    AppUtils.getInstance().showLog("SyncEntriesDataResponse" + response.toString(), SyncDataRepo.class);

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!code.equalsIgnoreCase("E200")) {
                        SimpleResponseBean.Error error = new Gson().fromJson(errorObj.toString(), SimpleResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        SimpleResponseBean simpleResponseBean = new Gson().fromJson(jsonObject.toString(), SimpleResponseBean.class);
                        serviceCallback.success(new Result.Success(simpleResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("FailureFromServer" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    private void callInactiveMemberApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.inActiveApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    JSONObject encryptedData=null;
                    String getEncrypted="";
                    try {
                        getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();
                    }catch (JsonParseException e)
                    {
                        AppUtils.getInstance().showLog("errorInSyncDataApi"+e,SyncDataRepo.class);
                    }






                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                            AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                        } catch (Exception e) {
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
                        AppUtils.getInstance().showLog(""+e,LoginRepo.class);
                    }
                    AppUtils.getInstance().showLog("SyncEntriesDataResponse" + response.toString(), SyncDataRepo.class);

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!code.equalsIgnoreCase("E200")) {
                        SimpleResponseBean.Error error = new Gson().fromJson(errorObj.toString(), SimpleResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        SimpleResponseBean simpleResponseBean = new Gson().fromJson(jsonObject.toString(), SimpleResponseBean.class);
                        serviceCallback.success(new Result.Success(simpleResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("FailureFromServer" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }
    public List<MemberDataToCheckDup> getDataToCheckDuplicate(String entryCompleteConfirmation) throws ExecutionException, InterruptedException {

        Callable<List<MemberDataToCheckDup>> callable = new Callable<List<MemberDataToCheckDup>>() {
            @Override
            public List<MemberDataToCheckDup> call() throws Exception {
                return memberEntryDao.getDataToCheckDuplicate(entryCompleteConfirmation, "0");
            }
        };
        Future<List<MemberDataToCheckDup>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }


    public List<ShgAndMemberDataBean> getDistinctShgAndMemberToSync(String entryCompleteConfirmation) throws ExecutionException, InterruptedException {

        Callable<List<ShgAndMemberDataBean>> callable = new Callable<List<ShgAndMemberDataBean>>() {
            @Override
            public List<ShgAndMemberDataBean> call() throws Exception {
                return memberEntryDao.getDistinctShgAndMemberToSync(entryCompleteConfirmation, "0");
            }
        };
        Future<List<ShgAndMemberDataBean>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

    public List<ActivityDataBean> getActivityData(String shgCode, String memberCode, String entryCompleteConfirmation) throws ExecutionException, InterruptedException {

        Callable<List<ActivityDataBean>> callable = new Callable<List<ActivityDataBean>>() {
            @Override
            public List<ActivityDataBean> call() throws Exception {
                return memberEntryDao.getActivityData(shgCode, memberCode, entryCompleteConfirmation, "0");
            }
        };
        Future<List<ActivityDataBean>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

    public CheckDuplicateRequestBean getCheckDuplicateRequest(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryCompleteConfirmation) {
        String memberData = "";
        try {
            List<MemberDataToCheckDup> memberDataToCheckDupList = getDataToCheckDuplicate(entryCompleteConfirmation);
            for (MemberDataToCheckDup memberDataToCheckDup : memberDataToCheckDupList) {
                memberData += memberDataToCheckDup.getShgCode() + "|" + memberDataToCheckDup.getMemberCode() +
                        "|" + memberDataToCheckDup.getSectorCode() + "|" + memberDataToCheckDup.getActivityCode() + "|" + memberDataToCheckDup.getFlagBeforeAfterNrlm() + ",";
            }
        } catch (Exception e) {
            AppUtils.getInstance().showLog("Exception while getting duplicate data from db ::" + e, SyncDataRepo.class);
        }
        return new CheckDuplicateRequestBean(stateShortName, loginId, imeiNo, deviceName
                , locationCoordinates, AppUtils.getInstance().removeComma(memberData), "B");
    }

    public SyncEntriesRequestBean getSyncEntriesRequest(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryCompleteConfirmation) {
        SyncEntriesRequestBean syncEntriesRequestBean = new SyncEntriesRequestBean();
        try {
            List<ShgAndMemberDataBean> shgAndMemberDataBeanList = getDistinctShgAndMemberToSync(entryCompleteConfirmation);
            List<SyncEntriesRequestBean.SyncEntry> syncEntryList = new ArrayList<>();

            syncEntriesRequestBean.setLogin_id(loginId);
            syncEntriesRequestBean.setDevice_name(deviceName);
            syncEntriesRequestBean.setImei_no(imeiNo);
            syncEntriesRequestBean.setLocation_coordinate(locationCoordinates);
            syncEntriesRequestBean.setState_short_name(stateShortName);
            syncEntriesRequestBean.setApp_version(BuildConfig.VERSION_NAME);


            for (ShgAndMemberDataBean shgAndMemberDataBean : shgAndMemberDataBeanList) {
                List<ActivityDataBean> activityDataBeanList = null ;
                AadharDbBean aadharDbBean=null;

                try {
                    activityDataBeanList = getActivityData(shgAndMemberDataBean.getShgCode(), shgAndMemberDataBean.getMemberCode(), entryCompleteConfirmation);
                    aadharDbBean=getAadharDetails(shgAndMemberDataBean.getMemberCode());

                }  catch (Exception e) {
                    AppUtils.getInstance().showLog("ExcpGetActivityDataSync" + e, SyncDataRepo.class);
                }
                SyncEntriesRequestBean.SyncEntry syncEntry = new SyncEntriesRequestBean.SyncEntry();
                syncEntry.setShg_code(shgAndMemberDataBean.getShgCode());
                syncEntry.setShg_member_code(shgAndMemberDataBean.getMemberCode());
               if (shgAndMemberDataBean.getSecc().equalsIgnoreCase("")) {

                   syncEntry.setSecc("0");

              }
               else
                 syncEntry.setSecc(shgAndMemberDataBean.getSecc());

                if (aadharDbBean==null || activityDataBeanList!=null &&
                        activityDataBeanList.get(0).getFlag_before_after_nrlm().equalsIgnoreCase("A") ){
                    syncEntry.setName_as_per_aadhaar("");
                    syncEntry.setEncrypted_aadhaar("");
                    syncEntry.setAadhaar_verified_status("");

                }else {
                    syncEntry.setName_as_per_aadhaar(aadharDbBean.getAadharName());
                    syncEntry.setEncrypted_aadhaar(aadharDbBean.getAadharNumber());
                    syncEntry.setAadhaar_verified_status(aadharDbBean.getAadharVerifiedStatus());
                }


                List<SyncEntriesRequestBean.ActivityData> activityDataList = new ArrayList<>();

                for (ActivityDataBean activityDataBean : activityDataBeanList) {
                    String incomeFrequencyCode = activityDataBean.getFrequency_code();
                    String incomeRangCode = activityDataBean.getRange_code();
                    if (!incomeFrequencyCode.isEmpty()){
                    if (incomeFrequencyCode.equalsIgnoreCase("1"))
                    {
                        if (incomeRangCode.equalsIgnoreCase("1")||incomeRangCode.equalsIgnoreCase("2")||incomeRangCode.equalsIgnoreCase("3")||incomeRangCode.equalsIgnoreCase("4")){
                            SyncEntriesRequestBean.ActivityData activityData = new SyncEntriesRequestBean.ActivityData();
                            activityData.setActivity_code(activityDataBean.getActivity_code());
                            activityData.setCreated_on_android(activityDataBean.getCreated_on_android());
                            activityData.setEntry_month(activityDataBean.getEntry_month());
                            activityData.setEntry_year(activityDataBean.getEntry_year());
                            activityData.setFlag_before_after_nrlm(activityDataBean.getFlag_before_after_nrlm());
                            activityData.setFrequency_code(activityDataBean.getFrequency_code());
                            activityData.setRange_code(activityDataBean.getRange_code());

                            activityData.setSector_code(activityDataBean.getSector_code());

                            activityDataList.add(activityData);
                        }

                        else  DialogFactory.getInstance().showAlertDialog(context, 1, "Alert", "Please Uninstall the app download from Playstore"
                                ,"ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );
                    }

                     if (incomeFrequencyCode.equalsIgnoreCase("2")){
                        if (incomeRangCode.equalsIgnoreCase("5")||incomeRangCode.equalsIgnoreCase("6")||incomeRangCode.equalsIgnoreCase("7")||incomeRangCode.equalsIgnoreCase("8")){
                            SyncEntriesRequestBean.ActivityData activityData = new SyncEntriesRequestBean.ActivityData();
                            activityData.setActivity_code(activityDataBean.getActivity_code());
                            activityData.setCreated_on_android(activityDataBean.getCreated_on_android());
                            activityData.setEntry_month(activityDataBean.getEntry_month());
                            activityData.setEntry_year(activityDataBean.getEntry_year());
                            activityData.setFlag_before_after_nrlm(activityDataBean.getFlag_before_after_nrlm());
                            activityData.setFrequency_code(activityDataBean.getFrequency_code());
                            activityData.setRange_code(activityDataBean.getRange_code());

                            activityData.setSector_code(activityDataBean.getSector_code());

                            activityDataList.add(activityData);
                        }
                        else  DialogFactory.getInstance().showAlertDialog(context, 1, "Alert", "Please Uninstall the app download from Playstore"
                                ,"ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );
                    }

                    if (incomeFrequencyCode.equalsIgnoreCase("3")){
                        if (incomeRangCode.equalsIgnoreCase("9")||incomeRangCode.equalsIgnoreCase("10")||incomeRangCode.equalsIgnoreCase("11")||incomeRangCode.equalsIgnoreCase("12")){
                            SyncEntriesRequestBean.ActivityData activityData = new SyncEntriesRequestBean.ActivityData();
                            activityData.setActivity_code(activityDataBean.getActivity_code());
                            activityData.setCreated_on_android(activityDataBean.getCreated_on_android());
                            activityData.setEntry_month(activityDataBean.getEntry_month());
                            activityData.setEntry_year(activityDataBean.getEntry_year());
                            activityData.setFlag_before_after_nrlm(activityDataBean.getFlag_before_after_nrlm());
                            activityData.setFrequency_code(activityDataBean.getFrequency_code());
                            activityData.setRange_code(activityDataBean.getRange_code());

                            activityData.setSector_code(activityDataBean.getSector_code());

                            activityDataList.add(activityData);
                        }
                        else  DialogFactory.getInstance().showAlertDialog(context, 1, "Alert", "Please Uninstall the app download from Playstore"
                                ,"ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );
                    }

                     if (incomeFrequencyCode.equalsIgnoreCase("4")){
                        if (incomeRangCode.equalsIgnoreCase("13")||incomeRangCode.equalsIgnoreCase("14")||incomeRangCode.equalsIgnoreCase("15")||incomeRangCode.equalsIgnoreCase("16")){
                            SyncEntriesRequestBean.ActivityData activityData = new SyncEntriesRequestBean.ActivityData();
                            activityData.setActivity_code(activityDataBean.getActivity_code());
                            activityData.setCreated_on_android(activityDataBean.getCreated_on_android());
                            activityData.setEntry_month(activityDataBean.getEntry_month());
                            activityData.setEntry_year(activityDataBean.getEntry_year());
                            activityData.setFlag_before_after_nrlm(activityDataBean.getFlag_before_after_nrlm());
                            activityData.setFrequency_code(activityDataBean.getFrequency_code());
                            activityData.setRange_code(activityDataBean.getRange_code());

                            activityData.setSector_code(activityDataBean.getSector_code());

                            activityDataList.add(activityData);
                        }
                        else  DialogFactory.getInstance().showAlertDialog(context, 1, "Alert", "Please Uninstall the app download from Playstore"
                                ,"ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );
                    }




                }

                }
                syncEntry.setActivities_data_sync(activityDataList);

                syncEntryList.add(syncEntry);
            }
            syncEntriesRequestBean.setNrlm_entry_sync(syncEntryList);
          String data=  new Gson().toJson(syncEntryList).toString();
          AppUtils.getInstance().showLog(data,SyncDataRepo.class);

        } catch (Exception e) {
            AppUtils.getInstance().showLog("ExcpgetDistinctShgAndMemberToSync" + e, SyncDataRepo.class);
        }
        return syncEntriesRequestBean;
    }




    public void updateSyncStatus() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberEntryDao.updateSyncStatus();

            }
        });

    }
    public void updateAadharSyncStatus() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                aadharDao.updateAadharSyncStatus();

            }
        });

    }
// For MemberInActive Table
private void insertMemberInActiveData(List<MemberInActiveEntity> memberInActiveEntityList) {

    AppDatabase.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() {
            memberInActiveDao.insertAll(memberInActiveEntityList);
        }
    });
}


    public String checkSeccNumberInDb(String memberCode) throws ExecutionException, InterruptedException {

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return memberEntryDao.checkSeccNumberInDb(memberCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }



    public void deleteDuplicateEntries(String shgCode, String memberCode, String sectorCode, String activityCode, String entryType) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberEntryDao.deleteDuplicateEntries(shgCode, memberCode, sectorCode, activityCode, entryType);

            }
        });
    }

    private AadharDbBean getAadharDetails(String memberCode) throws ExecutionException, InterruptedException {

        Callable<AadharDbBean> callable = new Callable<AadharDbBean>() {
            @Override
            public AadharDbBean call() throws Exception {
                return aadharDao.getAadharDetails(memberCode);
            }
        };
        Future<AadharDbBean> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

}

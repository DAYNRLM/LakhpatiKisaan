package com.nrlm.lakhpatikisaan.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.MemberEntryDao;
import com.nrlm.lakhpatikisaan.database.dbbean.ActivityDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberDataToCheckDup;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgAndMemberDataBean;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDuplicateResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataRepo {

    private final ExecutorService executor;
    private static SyncDataRepo instance = null;
    private MemberEntryDao memberEntryDao;

    private SyncDataRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        memberEntryDao = appDatabase.memberEntryDao();

    }

    public static SyncDataRepo getInstance(ExecutorService executor, Context context) {
        if (instance == null) {
            instance = new SyncDataRepo(executor, context);
        }
        return instance;
    }

    public void makeCheckDuplicateRequest(final CheckDuplicateRequestBean checkDuplicateRequestBean,
                                          final RepositoryCallback repositoryCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callCheckDuplicateDataApi(checkDuplicateRequestBean, new ServiceCallback<Result>() {
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
                AppUtils.getInstance().showLog("checkDuplicateDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    }/*else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")){
                        CheckDuplicateResponseBean.Error error=  new Gson().fromJson(response.body().getAsJsonObject("error"), CheckDuplicateResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    }*/ else {
                        CheckDuplicateResponseBean checkDuplicateResponseBean = new Gson().fromJson(response.body(), CheckDuplicateResponseBean.class);
                        serviceCallback.success(new Result.Success(checkDuplicateResponseBean));
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

    private void callSyncEntriesDataApi(final SyncEntriesRequestBean syncEntriesRequestBean, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.syncEntriesDataApi(syncEntriesRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppUtils.getInstance().showLog("SyncEntriesDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!response.body().getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase("E200")) {
                        SimpleResponseBean.Error error = new Gson().fromJson(response.body().getAsJsonObject("error"), SimpleResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        SimpleResponseBean simpleResponseBean = new Gson().fromJson(response.body(), SimpleResponseBean.class);
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

    public List<MemberDataToCheckDup> getDataToCheckDuplicate(String entryFlag) throws ExecutionException, InterruptedException {

        Callable<List<MemberDataToCheckDup>> callable = new Callable<List<MemberDataToCheckDup>>() {
            @Override
            public List<MemberDataToCheckDup> call() throws Exception {
                return memberEntryDao.getDataToCheckDuplicate(entryFlag, "0");
            }
        };
        Future<List<MemberDataToCheckDup>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }


    public List<ShgAndMemberDataBean> getDistinctShgAndMemberToSync(String entryFlag) throws ExecutionException, InterruptedException {

        Callable<List<ShgAndMemberDataBean>> callable = new Callable<List<ShgAndMemberDataBean>>() {
            @Override
            public List<ShgAndMemberDataBean> call() throws Exception {
                return memberEntryDao.getDistinctShgAndMemberToSync(entryFlag, "0");
            }
        };
        Future<List<ShgAndMemberDataBean>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

    public List<ActivityDataBean> getActivityData (String shgCode, String memberCode, String entryFlag) throws ExecutionException, InterruptedException {

        Callable<List<ActivityDataBean>> callable = new Callable<List<ActivityDataBean>>() {
            @Override
            public List<ActivityDataBean> call() throws Exception {
                return memberEntryDao.getActivityData(shgCode,memberCode, entryFlag, "0");
            }
        };
        Future<List<ActivityDataBean>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

    public CheckDuplicateRequestBean getCheckDuplicateRequest(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryFlag) {
        String memberData = "";
        try {
            List<MemberDataToCheckDup> memberDataToCheckDupList = getDataToCheckDuplicate(entryFlag);
            for (MemberDataToCheckDup memberDataToCheckDup : memberDataToCheckDupList) {
                memberData += memberDataToCheckDup.getShgCode() + "|" + memberDataToCheckDup.getMemberCode() +
                        "|" + memberDataToCheckDup.getSectorCode() + "|" + memberDataToCheckDup.getActivityCode() + ",";
            }
        } catch (Exception e) {
                AppUtils.getInstance().showLog("Exception while getting duplicate data from db ::"+e,SyncDataRepo.class);
        }
        return new CheckDuplicateRequestBean(stateShortName, loginId, imeiNo, deviceName
                , locationCoordinates, memberData, entryFlag);
    }

    public SyncEntriesRequestBean getSyncEntriesRequest(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryFlag) {
        SyncEntriesRequestBean syncEntriesRequestBean = new SyncEntriesRequestBean();
        try {
        List<SyncEntriesRequestBean.SyncEntry> syncEntryList = new ArrayList<>();

        syncEntriesRequestBean.setLogin_id(loginId);
        syncEntriesRequestBean.setDevice_name(deviceName);
        syncEntriesRequestBean.setImei_no(imeiNo);
        syncEntriesRequestBean.setLocation_coordinate(locationCoordinates);
        syncEntriesRequestBean.setState_short_name(stateShortName);

        for (ShgAndMemberDataBean shgAndMemberDataBean : getDistinctShgAndMemberToSync(entryFlag)) {
            SyncEntriesRequestBean.SyncEntry syncEntry = new SyncEntriesRequestBean.SyncEntry();
            syncEntry.setShg_code(shgAndMemberDataBean.getShgCode());
            syncEntry.setShg_member_code(shgAndMemberDataBean.getMemberCode());
            List<ActivityDataBean> activityDataBeanList = getActivityData(shgAndMemberDataBean.getShgCode(),shgAndMemberDataBean.getMemberCode(),entryFlag);
            List<SyncEntriesRequestBean.ActivityData> activityDataList = new ArrayList<>();
            for (ActivityDataBean activityDataBean : activityDataBeanList) {
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
            syncEntry.setActivities_data_sync(activityDataList);

            syncEntryList.add(syncEntry);
        }
        }catch (Exception e){
            AppUtils.getInstance().showLog("ExcpgetDistinctShgAndMemberToSync"+e,SyncDataRepo.class);
        }
        return syncEntriesRequestBean;
    }


}
/*{ }*/
package com.nrlm.lakhpatikisaan.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.AadharDao;
import com.nrlm.lakhpatikisaan.database.dao.ActivityDao;
import com.nrlm.lakhpatikisaan.database.dao.FrequencyDao;
import com.nrlm.lakhpatikisaan.database.dao.IncomeRangeDao;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dao.MemberActiveDao;
import com.nrlm.lakhpatikisaan.database.dao.MemberEntryDao;

import com.nrlm.lakhpatikisaan.database.dao.MemberInActiveDao;
import com.nrlm.lakhpatikisaan.database.dao.SeccDao;
import com.nrlm.lakhpatikisaan.database.dao.SectorDao;
import com.nrlm.lakhpatikisaan.database.dbbean.BlockDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ClfDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.InActiveMember;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VillageDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VoDataBean;
import com.nrlm.lakhpatikisaan.database.entity.AadhaarEntity;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;
import com.nrlm.lakhpatikisaan.database.entity.MasterDataEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberActiveEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.database.entity.SeccEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.client.ServiceCallback;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.MemberInActiveRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.OtpRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SeccRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SeccResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SupportiveMastersResponseBean;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataRepo {

    private final ExecutorService executor;
    private static MasterDataRepo instance = null;
    private Context context;
    private MasterDataDao masterDataDao;
    private MemberActiveDao memberActiveDao;
    private SectorDao sectorDao;
    private ActivityDao activityDao;
    private FrequencyDao frequencyDao;
    private IncomeRangeDao incomeRangeDao;
    private MemberInActiveDao memberInActiveDao;
    private MemberEntryDao memberEntryDao;
    private SeccDao seccDao;
    private AadharDao aadharDao;

    private MasterDataRepo(ExecutorService executor, Context context) {
        this.executor = executor;
        this.context = context;
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        masterDataDao = appDatabase.getMasterDataDao();
        sectorDao = appDatabase.getSectorDao();
        activityDao = appDatabase.getActivityDao();
        frequencyDao = appDatabase.getFrequencyDao();
        incomeRangeDao = appDatabase.getIncomeRangeDao();
        memberEntryDao = appDatabase.memberEntryDao();
        seccDao = appDatabase.getSeccDao();
        aadharDao =appDatabase.getAadharDao();
    }

    public static MasterDataRepo getInstance(ExecutorService executor, Context context) {
        if (instance == null) {
            instance = new MasterDataRepo(executor, context);
        }
        return instance;
    }


    public synchronized void makeMasterDataRequest(final JsonObject encryptedObject,
                                                   final RepositoryCallback repositoryCallback) {
        executor.execute(() -> {
            try {
                callMasterDataApi(encryptedObject, new ServiceCallback<Result>() {
                    @Override

                    public void success(Result<Result> successResponse) {
                        /*fill data into db*/
                        if (successResponse instanceof Result.Success) {
                            MasterDataResponseBean masterDataResponseBean = (MasterDataResponseBean) ((Result.Success) successResponse).data;
                            List<MasterDataEntity> masterDataEntityList = new ArrayList<>();
                            for (MasterDataResponseBean.MasterData masterData : masterDataResponseBean.getLocation_master()) {

                                MasterDataEntity masterDataEntity = new MasterDataEntity(masterData.getBlock_name(), masterData.getBlock_code(), masterData.getGp_code()
                                        , masterData.getGp_name(), masterData.getVillage_code(), masterData.getVillage_name(), masterData.getShg_name(), masterData.getShg_code(),
                                        masterData.getMember_code(), masterData.getMember_name(), masterData.getClf_code(), masterData.getClf_name(), masterData.getVo_code(),
                                        masterData.getVo_name(), masterData.getMember_joining_date(), masterData.getLast_entry_after_nrlm(), masterData.getLast_entry_before_nrlm(),
                                        masterData.getSecc_no_flag(), masterData.getLgd_village_code(),masterData.getAadhaar_verified_status(),masterData.getGender(),"Active",masterData.getBelonging_name());
                                masterDataEntityList.add(masterDataEntity);
                            }
                            insertAllMasterData(masterDataEntityList);
                        }
                        repositoryCallback.onComplete(successResponse);
                    }

                    @Override
                    public void error(Result<Result> errorResponse) {
                        repositoryCallback.onComplete(errorResponse);
                    }
                });

            } catch (Exception e) {
                Result<Result> errorResult = new Result.Error(e);
                repositoryCallback.onComplete(errorResult);
            }
        });

    }


    public synchronized void makeSupportiveMasterDataRequest(final LogRequestBean logRequestBean,
                                                             final RepositoryCallback repositoryCallback) {
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
                try {
                    callSupportiveMasterDataApi(encryptedObject, new ServiceCallback<Result>() {
                        @Override
                        public void success(Result<Result> successResponse) {
                            /*fill data into db*/
                            if (successResponse instanceof Result.Success) {
                                SupportiveMastersResponseBean supportiveMastersResponseBean = (SupportiveMastersResponseBean) ((Result.Success) successResponse).data;

                                for (SupportiveMastersResponseBean.Sector sector : supportiveMastersResponseBean.getSectors()) {
                                    SectorEntity sectorEntity = new SectorEntity(sector.getSector_name(), sector.getSector_code());
                                    insertSector(sectorEntity);
                                    for (SupportiveMastersResponseBean.Activity activity : sector.getActivities()) {
                                        ActivityEntity activityEntity = new ActivityEntity(activity.getActivity_name(), sector.getSector_code()
                                                , activity.getActivity_code());
                                        insertActivity(activityEntity);
                                    }
                                }

                                for (SupportiveMastersResponseBean.IncomeFrequency incomeFrequency : supportiveMastersResponseBean.getIncome_frequencies()) {
                                    FrequencyEntity frequencyEntity = new FrequencyEntity(incomeFrequency.getFrequency_id(), incomeFrequency.getFrequency_name());
                                    insertFrequency(frequencyEntity);
                                    for (SupportiveMastersResponseBean.IncomeRange incomeRange : incomeFrequency.getIncome_range()) {
                                        IncomeRangeEntity incomeRangeEntity = new IncomeRangeEntity(incomeFrequency.getFrequency_id(),
                                                incomeRange.getRange_id(), incomeRange.getRange_name());
                                        insertIncomeRange(incomeRangeEntity);
                                    }
                                }

                            }

                            repositoryCallback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<Result> errorResponse) {
                            repositoryCallback.onComplete(errorResponse);
                        }
                    });
                } catch (Exception e) {
                    Result<Result> errorResult = new Result.Error(e);
                    repositoryCallback.onComplete(errorResult);
                }
            }
        });

    }

    public synchronized void makeSeccDataRequest(final SeccRequestBean seccRequestBean, final RepositoryCallback repositoryCallback) {
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreqBean=gson.toJson(seccRequestBean);
            encryptedObject.addProperty("data",cryptography.encrypt(logreqBean.toString()));
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
                try {
                    callSeccDataApi(encryptedObject, new ServiceCallback<Result>() {
                        @Override
                        public void success(Result<Result> successResponse) {
                            if (successResponse instanceof Result.Success) {
                                SeccResponseBean seccResponseBean = (SeccResponseBean) ((Result.Success) successResponse).data;
                                List<SeccEntity> seccEntityList= new ArrayList<>();
                                if (seccResponseBean.getSecc_data().size() > 0) {
                                    for (SeccResponseBean.SeccData seccData : seccResponseBean.getSecc_data()) {
                                        seccEntityList.add(new SeccEntity(seccData.getSecc_no(),seccData.getMember_name()
                                                ,seccData.getFather_name(),seccData.getShg_member_code()));
                                    }
                                    insertAllSecc(seccEntityList);
                                }
                            }
                            repositoryCallback.onComplete(successResponse);
                        }

                        @Override
                        public void error(Result<Result> errorResponse) {
                            repositoryCallback.onComplete(errorResponse);
                        }
                    });
                } catch (Exception e) {
                    AppUtils.getInstance().showLog("SeccDataApimakeRequestExp" + e.getMessage(), MasterDataRepo.class);
                }

            }
        });

    }


    private void callMasterDataApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
       /* HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(AppConstant.st, AppConstant.s);*/
        Call<JsonObject> call = (Call<JsonObject>) apiServices.masterDataApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JSONObject jsonObject = null;

                String objectResponse="";
                JSONObject encryptedData=null;
                String getEncrypted="";
                try {
                   getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();
                    AppUtils.getInstance().showLog("encryptmasterdata" + jsonObject.toString(), SignUpFragment.class);

                }catch (Exception e)
                {

                    AppUtils.getInstance().showLog("exception" + e, SignUpFragment.class);
                }
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        Cryptography cryptography = new Cryptography();
                        jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                        AppUtils.getInstance().showLog("masterdata" + jsonObject.toString(), SignUpFragment.class);
                    } catch (Exception e) {
                        DialogFactory.getInstance().showAlertDialog(context, 1, "Alert","Kindly contact BPM in order to reset your device info & please login after 24 hours after resetting device info"
                                , "ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );
                       // Toast.makeText(context,"data not found",Toast.LENGTH_LONG).show();

                        AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                    }
                }
                String code="";
                JSONObject errorObj=null;
                try {
                    code = jsonObject.getJSONObject("error").getString("code");
                    errorObj=jsonObject.getJSONObject("error");

                }catch (Exception e)
                {
                    AppUtils.getInstance().showLog(""+e,MasterDataRepo.class);
                }

                AppUtils.getInstance().showLog("MasterDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!code.equalsIgnoreCase("E200" )&& !code.equalsIgnoreCase(null)) {
                        MasterDataResponseBean.Error error = new Gson().fromJson(errorObj.toString(), MasterDataResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        MasterDataResponseBean masterDataResponseBean = new Gson().fromJson(jsonObject.toString(), MasterDataResponseBean.class);
                        serviceCallback.success(new Result.Success(masterDataResponseBean));
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

    private void callSupportiveMasterDataApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
        /*HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(AppConstant.st, AppConstant.s);*/
        Call<JsonObject> call = (Call<JsonObject>) apiServices.supportiveMasterDataApi(encryptedObject);
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

                        AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                    } catch (Exception e) {
                       /* DialogFactory.getInstance().showAlertDialog(context, 1, "Alert","Kindly contact BPM in order to reset your device info & please login after 24 hours after resetting device info"
                                , "ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        ); */
                        Toast.makeText(context,"data not found",Toast.LENGTH_LONG).show();

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

                AppUtils.getInstance().showLog("MasterDataResponse" + response.toString(), MasterDataRepo.class);

                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!code.equalsIgnoreCase("E200")) {
                        SupportiveMastersResponseBean.Error error = new Gson().fromJson(errorObj.toString(), SupportiveMastersResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        SupportiveMastersResponseBean supportiveMastersResponseBean = new Gson().fromJson(jsonObject.toString(), SupportiveMastersResponseBean.class);
                        serviceCallback.success(new Result.Success(supportiveMastersResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("SupportiveFailureFromServer" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }

    private void callSeccDataApi(final JsonObject encryptedObject, final ServiceCallback<Result> serviceCallback) {

        ApiServices apiServices = RetrofitClient.getApiServices();
 /*       HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(AppConstant.st, AppConstant.s);*/
        Call<JsonObject> call = (Call<JsonObject>) apiServices.seccDataApi(encryptedObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject jsonObject = null;

                String objectResponse="";
                JSONObject encryptedData=null;

                String getEncrypted=  response.body().getAsJsonObject().getAsJsonPrimitive("data").getAsString();
                AppUtils.getInstance().showLog("responseJSON" + getEncrypted, SignUpFragment.class);




                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        Cryptography cryptography = new Cryptography();
                        jsonObject = new JSONObject(cryptography.decrypt(getEncrypted)); //Main data of state

                        AppUtils.getInstance().showLog("responseJSON1" + jsonObject.toString(), SignUpFragment.class);
                    } catch (Exception e) {
                        Toast.makeText(context,"data not found",Toast.LENGTH_LONG).show();
                       /* DialogFactory.getInstance().showAlertDialog(context, 1, "Alert","Kindly contact BPM in order to reset your device info & please login after 24 hours after resetting device info"
                                , "ok", (dialog, which) -> dialog.dismiss(), null, null, true
                        );  */                           AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
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


                AppUtils.getInstance().showLog("SeccDataResponse" + response.toString(), MasterDataRepo.class);
                if (response.isSuccessful()) {

                    if (response.body() == null || response.code() == 204) { // 204 is empty response
                        serviceCallback.error(new Result.Error(new Throwable("Getting NULL response")));
                    } else if (!code.equalsIgnoreCase("E200")) {
                        SeccResponseBean.Error error = new Gson().fromJson(errorObj.toString(), SeccResponseBean.Error.class);
                        serviceCallback.error(new Result.Error(error));
                    } else {
                        SeccResponseBean seccResponseBean = new Gson().fromJson(jsonObject.toString(), SeccResponseBean.class);
                        serviceCallback.success(new Result.Success(seccResponseBean));
                    }

                } else {
                    serviceCallback.error(new Result.Error(new Throwable(response.code() + " " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppUtils.getInstance().showLog("SeccApiFailureFromServer" + t.toString(), MasterDataRepo.class);
                serviceCallback.error(new Result.Error(t));
            }
        });
    }


    private void insertAllSecc(List<SeccEntity> seccEntityList) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                seccDao.insertAll(seccEntityList);
            }
        });
    }


    private void insertAllMasterData(List<MasterDataEntity> masterDataEntityList) {

        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                masterDataDao.insertAll(masterDataEntityList);
            }
        });
    }
    private void insertActivity(List<MemberActiveEntity> memberActiveEntityList) {

        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberActiveDao.insertAll(memberActiveEntityList);
            }
        });
    }

    private void insertSector(SectorEntity sectorEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sectorDao.insert(sectorEntity);
            }
        });
    }

    private void insertActivity(ActivityEntity activityEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDao.insert(activityEntity);
            }
        });
    }

    private void insertFrequency(FrequencyEntity frequencyEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                frequencyDao.insert(frequencyEntity);
            }
        });
    }

    private void insertIncomeRange(IncomeRangeEntity incomeRangeEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                incomeRangeDao.insert(incomeRangeEntity);
            }
        });
    }

    public List<MemberListDataBean> memberListMasterData(String shgCode) throws ExecutionException, InterruptedException {

        Callable<List<MemberListDataBean>> listCallable = new Callable<List<MemberListDataBean>>() {
            @Override
            public List<MemberListDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("memberListMasterData" + masterDataDao.getMemberListData(shgCode).size(), MasterDataRepo.class);
                return masterDataDao.getMemberListData(shgCode);

            }
        };
        Future<List<MemberListDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }


    public List<GpDataBean> getGpListData(String blockCode) throws ExecutionException, InterruptedException {

        Callable<List<GpDataBean>> listCallable = new Callable<List<GpDataBean>>() {
            @Override
            public List<GpDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getGpListData" + masterDataDao.getGpListData(blockCode).size(), MasterDataRepo.class);
                return masterDataDao.getGpListData(blockCode);
            }
        };
        Future<List<GpDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public List<VillageDataBean> getVillageListData(String gpCode) throws ExecutionException, InterruptedException {

        Callable<List<VillageDataBean>> listCallable = new Callable<List<VillageDataBean>>() {
            @Override
            public List<VillageDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getVillageListData" + masterDataDao.getGpListData(gpCode).size() + "---" + gpCode, MasterDataRepo.class);
                return masterDataDao.getVillageListData(gpCode);
            }
        };
        Future<List<VillageDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public List<ShgDataBean> getShgListData(String villageCode) throws ExecutionException, InterruptedException {

        Callable<List<ShgDataBean>> listCallable = new Callable<List<ShgDataBean>>() {
            @Override
            public List<ShgDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getShgListData" + masterDataDao.getGpListData(villageCode).size(), MasterDataRepo.class);
                return masterDataDao.getShgListData(villageCode);
            }
        };
        Future<List<ShgDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public String getMemberNameDB(String memBerCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("ShgListData" + masterDataDao.getGpListData(memBerCode).size(), MasterDataRepo.class);
                return masterDataDao.getMemberNameDB(memBerCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public String getShgNameDB(String shgCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("ShgNameDB" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                return masterDataDao.getShgNameDB(shgCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public String getMemberJoiningDate(String memberCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("ShgNameDB" + masterDataDao.getGpListData(memberCode).size(), MasterDataRepo.class);
                return masterDataDao.getMemberJoiningDate(memberCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }


    public String getMemberBelongingName(String memberCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return masterDataDao.getMemberBelonging(memberCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public void updateBeforeEntryDateInLocal(String memberCode,String date) throws ExecutionException, InterruptedException {
       AppDatabase.databaseWriteExecutor.execute(new Runnable() {
           @Override
           public void run() {
               masterDataDao.updateBeforeEntryDateInLocal(memberCode,date);
           }
       });
    }

    public void updateAfterEntryDateInLocal(String memberCode,String date) throws ExecutionException, InterruptedException {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                masterDataDao.updateAfterEntryDateInLocal(memberCode,date);
            }
        });
    }


    public String getBeforeLastDate(String memberCode){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getBeforeEntryDate(memberCode);
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getTotalShg(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getaTotalShg();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getTotalMember(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getTotalMember();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getBeforeMemberSyncLocally(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getLocalBeforeEntry();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }
    public String getAfterDataFromServer(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getServerAfterEntry();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }
    public void setStatus(String status,String memberCode){
       AppDatabase.databaseWriteExecutor.execute(new Runnable() {
           @Override
           public void run() {
               masterDataDao.setStatus(status,memberCode);
           }
       });
    }

    public String getShgWhoseAllMemberComplted(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getWhoseAllMemberCompleted();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }


    public String getMemberIsNotInClfAndVo(String memberCode){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getMemberIsNotInClfAndVo(memberCode);
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }



    public String getShgWhoseAtleastOneMemberLeft(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getWhoseAtleastOneMemberLeft();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getMemberSurveyCompeted(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getSurveyCompleted();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getMemberSurveyPending(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getSurveyPending();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }



    public String getBeforeDataFromServer(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getServerBeforeEntry();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }

    public String getAfterMemberSyncLOcally(){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getLocalAfterEntry();
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }
    public String getAfterLastDate(String memberCode){
        String str=null;

        try{
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                    return masterDataDao.getAfterEntryDate(memberCode);
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            str =future.get();

        }catch (Exception e){

        }
        return str;
    }



    public String getMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("getMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                return String.valueOf(masterDataDao.getMemberCount(shgCode));
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public String getBeforeEntryMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("getBeforeEntryMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                return String.valueOf(masterDataDao.getBeforeEntryMemberCount(shgCode));
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }



    public String getAfterEntryMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("getAfterEntryMemberCount" + masterDataDao.getGpListData(shgCode).size(), MasterDataRepo.class);
                return String.valueOf(masterDataDao.getAfterEntryMemberCount(shgCode));
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public List<ClfDataBean> getUniqueClf() throws ExecutionException, InterruptedException {
        Callable<List<ClfDataBean>> listCallable = new Callable<List<ClfDataBean>>() {
            @Override
            public List<ClfDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getAfterEntryMemberCount" + masterDataDao.getUniqueClf().size(), MasterDataRepo.class);
                return masterDataDao.getUniqueClf();
            }
        };
        Future<List<ClfDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }


    public List<VoDataBean> getUniqueVo(String clfCode) throws ExecutionException, InterruptedException {
        Callable<List<VoDataBean>> listCallable = new Callable<List<VoDataBean>>() {
            @Override
            public List<VoDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getAfterEntryMemberCount" + masterDataDao.getUniqueVo(clfCode).size(), MasterDataRepo.class);
                return masterDataDao.getUniqueVo(clfCode);
            }
        };
        Future<List<VoDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }

    public List<ShgDataBean> getShgDataWithVo(String voCode) throws ExecutionException, InterruptedException {
        Callable<List<ShgDataBean>> listCallable = new Callable<List<ShgDataBean>>() {
            @Override
            public List<ShgDataBean> call() throws Exception {
                AppUtils.getInstance().showLog("getShgDataWithVo" + masterDataDao.getShgDataWithVo(voCode).size(), MasterDataRepo.class);
                return masterDataDao.getShgDataWithVo(voCode);
            }
        };
        Future<List<ShgDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }


    /*********added by lincon***********/

    public String getMemberDoj(String memberCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("***MEMBER DOJ***"+masterDataDao.getMemberDOJ(memberCode),MasterDataRepo.class);
                return masterDataDao.getMemberDOJ(memberCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }
    public void deleteInactiveMainMember(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                masterDataDao.deleteInActivityMember();
            }
        });
    }



    public List<SectorEntity> getAllSector(){
        List<SectorEntity> sectorData=null;
        try {
            Callable<List<SectorEntity>> listCallable = new Callable<List<SectorEntity>>() {
                @Override
                public List<SectorEntity> call() throws Exception {
                    return sectorDao.getAllSector();
                }
            };
            Future<List<SectorEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            sectorData = future.get();

        } catch (Exception e) {

        }
        return sectorData;
    }
    public List<InActiveMember> getAllInactiveData(){
        List<InActiveMember> inactiveDataMember=null;
        try {
            Callable<List<InActiveMember>> listCallable = new Callable<List<InActiveMember>>() {
                @Override
                public List<InActiveMember> call() throws Exception {
                    return masterDataDao.getDataOfInactiveMember();
                }
            };
            Future<List<InActiveMember>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            inactiveDataMember = future.get();

        } catch (Exception e) {

        }
        return inactiveDataMember;
    }

    public List<String> getSectorName() {
        List<String> sectorName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sectorName = getAllSector().stream().map(SectorEntity::getSector_name).collect(Collectors.toList());
        }
        return sectorName;
    }



    public String SectorName(int id){

        String sectorName=null;
        try {
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return sectorDao.SectorName(id);
                }
            };
            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            sectorName = future.get();

        } catch (Exception e) {

        }
        return sectorName;

    }

    public List<ActivityEntity> getAllActivity(int id) {
        List<ActivityEntity> activityData = null;
        try {
            Callable<List<ActivityEntity>> listCallable = new Callable<List<ActivityEntity>>() {
                @Override
                public List<ActivityEntity> call() throws Exception {
                    return activityDao.getAllActivity(id);
                }
            };
            Future<List<ActivityEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            activityData = future.get();

        } catch (Exception e) {

        }
        return activityData;
    }

    public List<ActivityEntity> getAllActivityWithoutSector() {
        List<ActivityEntity> activityData = null;
        try {
            Callable<List<ActivityEntity>> listCallable = new Callable<List<ActivityEntity>>() {
                @Override
                public List<ActivityEntity> call() throws Exception {
                    return activityDao.getAllActivityWithoutSec();
                }
            };
            Future<List<ActivityEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            activityData = future.get();

        } catch (Exception e) {

        }
        return activityData;
    }


    public List<String> getActivityName(int id,String memberCode) {
        List<String> activityName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityName = getAllActivity(id).stream().map(ActivityEntity::getActivity_name).collect(Collectors.toList());
        }
        return activityName;
    }


    public List<FrequencyEntity> getAllFrequency() {
        List<FrequencyEntity> frequencyData = null;
        try {
            Callable<List<FrequencyEntity>> listCallable = new Callable<List<FrequencyEntity>>() {
                @Override
                public List<FrequencyEntity> call() throws Exception {
                    return frequencyDao.getAllFrequency();
                }
            };
            Future<List<FrequencyEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            frequencyData = future.get();

        } catch (Exception e) {

        }
        return frequencyData;
    }

    public List<String> getFrequencyName() {
        List<String> frequencyName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            frequencyName = getAllFrequency().stream().map(FrequencyEntity::getFrequency_name).collect(Collectors.toList());
        }
        return frequencyName;
    }


    public List<IncomeRangeEntity> getAllIncome(int freqId) {
        List<IncomeRangeEntity> incomeData = null;
        try {
            Callable<List<IncomeRangeEntity>> listCallable = new Callable<List<IncomeRangeEntity>>() {
                @Override
                public List<IncomeRangeEntity> call() throws Exception {
                    return incomeRangeDao.getAllIncomeRange(freqId);
                }
            };
            Future<List<IncomeRangeEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            incomeData = future.get();

        } catch (Exception e) {

        }
        return incomeData;
    }

    public List<String> getIncomeName(int freqId) {
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getAllIncome(freqId).stream().map(IncomeRangeEntity::getRange_name).collect(Collectors.toList());
        }
        return incomeName;
    }


    public List<BlockDataBean> getAllBlock() {
        List<BlockDataBean> masterBlockData = null;
        try {
            Callable<List<BlockDataBean>> listCallable = new Callable<List<BlockDataBean>>() {
                @Override
                public List<BlockDataBean> call() throws Exception {
                    AppUtils.getInstance().showLog("masterDataDao.getAllBlock()" + masterDataDao.getAllBlock().size(), MasterDataRepo.class);
                    return masterDataDao.getAllBlock();
                }
            };
            Future<List<BlockDataBean>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            masterBlockData = future.get();

        } catch (Exception e) {
            AppUtils.getInstance().showLog("getAllBlockExp" + e.toString(), MasterDataRepo.class);
        }
        return masterBlockData;
    }

    public List<String> getBlockName() {
        List<BlockDataBean> blockDataBeans =getAllBlock();
        List<String> incomeName = null;
        if (blockDataBeans!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName =blockDataBeans.stream().map(BlockDataBean::getBlockName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName" + incomeName.size(), MasterDataDao.class);
        return incomeName;
    }


    public void insertBeforeNrlmEntry(MemberEntryEntity memberEntryDataItem) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberEntryDao.insertAll(memberEntryDataItem);
            }
        });
    }

    public List<SeccEntity> getSeccData(String memberCode) {
        List<SeccEntity> seccData = null;
        try {
            Callable<List<SeccEntity>> listCallable = new Callable<List<SeccEntity>>() {
                @Override
                public List<SeccEntity> call() throws Exception {
                    return seccDao.getSeccDetail(memberCode);
                }
            };
            Future<List<SeccEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            seccData = future.get();

            SeccEntity seccEntity =  new SeccEntity("0","Match Not Found","MNF","0");
            seccData.add(seccEntity);

        } catch (Exception e) {

        }
        return seccData;
    }

    public List<String> getSeccNameData(String memberCode){
        List<String> finalList =new ArrayList<>();


        List<SeccEntity> seccData = getSeccData(memberCode);
        for(SeccEntity seccEntityObject:seccData){
            finalList.add(seccEntityObject.getMember_name() + " ( "+seccEntityObject.getFather_name()+" )");
        }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<String> memberName =  getSeccData(memberCode).stream().map(SeccEntity::getMember_name).collect(Collectors.toList());
            List<String> fatherName =  getSeccData(memberCode).stream().map(SeccEntity::getFather_name).collect(Collectors.toList());
            Stream<String> combinStream = Stream.concat(memberName.stream(),fatherName.stream());
            finalList =combinStream.collect(Collectors.toList());

             finalList =  getSeccData(memberCode).stream().map(SeccEntity::getFather_name).collect(Collectors.toList());


        }*/
        return finalList;

    }

    public List<MemberEntryEntity> getAllMemberForActivity(String membercode,String entryFlag){

        List<MemberEntryEntity> memberDataEntry = null;
        try {
            Callable<List<MemberEntryEntity>> listCallable = new Callable<List<MemberEntryEntity>>() {
                @Override
                public List<MemberEntryEntity> call() throws Exception {
                    AppUtils.getInstance().showLog("masterDataDao.getAllBlock()" + masterDataDao.getAllBlock().size(), MasterDataRepo.class);
                    return memberEntryDao.getAllSelectedMember(membercode,entryFlag);
                }
            };
            Future<List<MemberEntryEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            memberDataEntry = future.get();

        } catch (Exception e) {
            AppUtils.getInstance().showLog("getAllBlockExp" + e.toString(), MasterDataRepo.class);
        }
        return memberDataEntry;
    }

    public void deleteActivity(String memberCode,String activityCode){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberEntryDao.deleteSelectedActivity(memberCode,activityCode);
            }
        });


    }

    public List<MemberEntryEntity> getAllMemberDataWithEntryStatus(String memberCode,String entryStatus ){
        List<MemberEntryEntity> memberDataEntry = null;
        try {
            Callable<List<MemberEntryEntity>> listCallable = new Callable<List<MemberEntryEntity>>() {
                @Override
                public List<MemberEntryEntity> call() throws Exception {
                    AppUtils.getInstance().showLog("masterDataDao.getAllBlock()" + masterDataDao.getAllBlock().size(), MasterDataRepo.class);
                    return memberEntryDao.getAllDataWithEntryStatus(memberCode,entryStatus);
                }
            };
            Future<List<MemberEntryEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            memberDataEntry = future.get();

        } catch (Exception e) {
            AppUtils.getInstance().showLog("getAllBlockExp" + e.toString(), MasterDataRepo.class);
        }
        return memberDataEntry;
    }

    public String getSeccStatus(String memberCode) {
        String seccData = null;
        try {
            Callable<String> listCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return masterDataDao.getSeccStatus(memberCode);
                }
            };
            Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
            seccData = future.get();

        } catch (Exception e) {

        }
        return seccData;
    }

    public void updateConfirmationStatus(String memberCode, String entryStatus){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                memberEntryDao.updateConfirmationStatus(memberCode,entryStatus);
            }
        });

    }

    /*****  for aadhar curd operarions************/
    public void insertAadharData(AadhaarEntity aadhaarEntity){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                aadharDao.insert(aadhaarEntity);
            }
        });
    }


    public List<AadhaarEntity> getAadharData(String aadharNumber){
        List<AadhaarEntity> aadharDataItem = null;
        try {
            Callable<List<AadhaarEntity>> listCallable = new Callable<List<AadhaarEntity>>() {
                @Override
                public List<AadhaarEntity> call() throws Exception {
                    return aadharDao.getAadharData(aadharNumber);
                }
            };
            Future<List<AadhaarEntity>> future = Executors.newSingleThreadExecutor().submit(listCallable);
            aadharDataItem = future.get();

        } catch (Exception e) {
            AppUtils.getInstance().showLog("GET AADHAR EXPECTION" + e.toString(), MasterDataRepo.class);
        }
        return aadharDataItem;

    }


    public String getAadharStatus(String memBerCode) throws ExecutionException, InterruptedException {
        Callable<String> listCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppUtils.getInstance().showLog("ShgListData" + masterDataDao.getGpListData(memBerCode).size(), MasterDataRepo.class);
                return masterDataDao.getAadharStatus(memBerCode);
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(listCallable);
        return future.get();
    }


    public void updateAadharStatus(String memberCode,String status)  {
        try{
            AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    masterDataDao.updateAadharStatus(memberCode,status);
                }
            });
        }catch (Exception e){

        }

    }




}
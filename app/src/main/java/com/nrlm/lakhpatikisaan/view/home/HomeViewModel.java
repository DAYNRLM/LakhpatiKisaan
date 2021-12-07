package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dbbean.BlockDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VillageDataBean;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDuplicateResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.repository.SyncDataRepo;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private MasterDataRepo masterDataRepo;
    private SyncDataRepo syncDataRepo;

    public HomeViewModel() {
    }


    public void getHomeViewModelRepos(Context context){
        masterDataRepo=MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(),context);

    }
    // common alert dialog
    public MutableLiveData<String> commonAleartDialog(Context context) {
        MutableLiveData<String> resetData = new MutableLiveData<>();

        new MaterialAlertDialogBuilder(context).setTitle("Test").setIcon(R.drawable.ic_baseline_add_circle_outline_24)
                .setMessage("check")
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetData.setValue("ok");
                }).setNegativeButton("cancel", (dialogInterface, i) -> {

            dialogInterface.dismiss();
            resetData.setValue("cancel");
        }).show();
        return resetData;
    }

    public void checkDuplicateAtServer(Context context, String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryFlag) {
        syncDataRepo = SyncDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        syncDataRepo.makeCheckDuplicateRequest(syncDataRepo.getCheckDuplicateRequest(loginId, stateShortName, imeiNo,
                deviceName, locationCoordinates, entryFlag), new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                try {
                    AppUtils.getInstance().showLog("checkDuplicateDataResult" + result.toString(), AuthViewModel.class);
                    if (result instanceof Result.Success) {
                        CheckDuplicateResponseBean checkDuplicateResponseBean = (CheckDuplicateResponseBean) ((Result.Success) result).data;
                        AppUtils.getInstance().showLog("checkDuplicateSuccessResult" + checkDuplicateResponseBean.getMember_code(), HomeViewModel.class);
                        if (!checkDuplicateResponseBean.getMember_code().equalsIgnoreCase("0")) {
                            /*delete  duplicate entries and hit sync api*/

                            makeSyncMemberEntry(loginId,stateShortName, imeiNo, deviceName, locationCoordinates,entryFlag);

                        } else {
                            makeSyncMemberEntry(loginId,stateShortName, imeiNo, deviceName, locationCoordinates,entryFlag);
                        }
                    } else {
                        Object errorObject = ((Result.Error) result).exception;
                        if (errorObject != null) {
                               /* if (errorObject instanceof MasterDataResponseBean.Error){
                                    MasterDataResponseBean.Error responseError= (MasterDataResponseBean.Error) errorObject;
                                    AppUtils.getInstance().showLog(responseError.getCode()+" MasterApiErrorObj"
                                            +responseError.getMessage(),AuthViewModel.class);
                                } else*/
                            if (errorObject instanceof Throwable) {
                                Throwable exception = (Throwable) errorObject;
                                AppUtils.getInstance().showLog("CheckDuplicateRetrofitErrors:-------" + exception.getMessage()
                                        , AuthViewModel.class);
                            }
                        }
                    }
                } catch (Exception e) {
                    AppUtils.getInstance().showLog("checkDuplicateDataResultExp" + e.toString(), AuthViewModel.class);
                }
            }
        });

    }


    private void makeSyncMemberEntry(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryFlag) {
       SyncEntriesRequestBean syncEntriesRequestBean= syncDataRepo.getSyncEntriesRequest(loginId, stateShortName , imeiNo, deviceName, locationCoordinates, entryFlag);
       if (syncEntriesRequestBean.getNrlm_entry_sync()!=null && syncEntriesRequestBean.getNrlm_entry_sync().size()!=0 ){
           syncDataRepo.makeSyncEntriesRequest(syncEntriesRequestBean, new RepositoryCallback() {
               @Override
               public void onComplete(Result result) {
                   try {
                       if (result instanceof Result.Success) {
                           SimpleResponseBean simpleResponseBean = (SimpleResponseBean) ((Result.Success) result).data;
                           AppUtils.getInstance().showLog(simpleResponseBean.getError().getCode() + "---" +
                                   simpleResponseBean.getError().getMessage(), HomeViewModel.class);
                       } else {
                           Object errorObject = ((Result.Error) result).exception;
                           if (errorObject != null) {
                               if (errorObject instanceof SimpleResponseBean.Error) {
                                   SimpleResponseBean.Error responseError = (SimpleResponseBean.Error) errorObject;
                                   AppUtils.getInstance().showLog(responseError.getCode() + "SyncEntriesApiErrorObj"
                                           + responseError.getMessage(), AuthViewModel.class);
                               } else if (errorObject instanceof Throwable) {
                                   Throwable exception = (Throwable) errorObject;
                                   AppUtils.getInstance().showLog("SyncEntriesRetrofitErrors:-------" + exception.getMessage()
                                           , AuthViewModel.class);
                               }
                           }
                       }
                   } catch (Exception e) {
                       AppUtils.getInstance().showLog("SyncEntriesResultExp" + e.toString(), AuthViewModel.class);

                   }
               }
           });
       }

    }




    /********* add method by lincon**********/


    public List<String> loadSectorData(){
        return masterDataRepo.getSectorName();
    }

    public List<SectorEntity> getAllSectorData(){
        return masterDataRepo.getAllSector();
    }

    public List<String> loadActivityData(int id){
        return masterDataRepo.getActivityName(id);
    }

    public List<ActivityEntity> getAllActivityData(int id){
        return masterDataRepo.getAllActivity(id);
    }

    public List<String> loadFrequencyData(){
        return masterDataRepo.getFrequencyName();
    }

    public List<FrequencyEntity> getAllFrequencyData(){
        return masterDataRepo.getAllFrequency();
    }

    public List<String> loadIncomeData(int freqId){
        return masterDataRepo.getIncomeName(freqId);
    }

    public List<IncomeRangeEntity> getAllIncomeData(int freqId){
        return masterDataRepo.getAllIncome(freqId);
    }

    public List<String> getLocationFrom(){
        List<String> location = new ArrayList<>();
        location.add("Geography");
        location.add("CBO");
        return location;
    }

    public List<String> loadBlockName(){
        return masterDataRepo.getBlockName();
    }

    public List<BlockDataBean> getAllBlockData(){
        return masterDataRepo.getAllBlock();
    }


    public List<MemberListDataBean> memberListMasterData(String shgCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.memberListMasterData(shgCode);
    }

    public List<GpDataBean> getGpListData(String blockCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getGpListData(blockCode);
    }

    public List<VillageDataBean> getVillageListData(String gpCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getVillageListData(gpCode);
    }
    public List<ShgDataBean> getShgListData(String villageCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getShgListData(villageCode);
    }

    public List<String> getGpListName(String blockCode) throws ExecutionException, InterruptedException {
        List<String> incomeName= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getGpListData(blockCode).stream().map(GpDataBean::getGpName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName"+incomeName.size(), MasterDataDao.class);
        return incomeName;

    }

    public List<String> getVillageListName(String gpCode) throws ExecutionException, InterruptedException {
        List<String> incomeName= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getVillageListData(gpCode).stream().map(VillageDataBean::getVillageName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName"+incomeName.size(), MasterDataDao.class);
        return incomeName;
    }


    public List<String> getShgListName(String villageCode) throws ExecutionException, InterruptedException {
        List<String> incomeName= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getShgListData(villageCode).stream().map(ShgDataBean::getShgName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName"+incomeName.size(), MasterDataDao.class);
        return incomeName;
    }




   public void insertBeforeNrlmEntryData(List<MemberEntryEntity> memberEntryDataItem){
        masterDataRepo.insertBeforeNrlmEntry(memberEntryDataItem);
    }



}

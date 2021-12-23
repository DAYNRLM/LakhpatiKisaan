package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dao.MasterDataDao;
import com.nrlm.lakhpatikisaan.database.dbbean.BlockDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ClfDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VillageDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VoDataBean;
import com.nrlm.lakhpatikisaan.database.entity.ActivityEntity;
import com.nrlm.lakhpatikisaan.database.entity.FrequencyEntity;
import com.nrlm.lakhpatikisaan.database.entity.IncomeRangeEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.database.entity.SeccEntity;
import com.nrlm.lakhpatikisaan.database.entity.SectorEntity;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private MasterDataRepo masterDataRepo;
    private SyncDataRepo syncDataRepo;
    private String syncApiStatus;

    public HomeViewModel() {
    }


    public void getHomeViewModelRepos(Context context) {
        masterDataRepo = MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);

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
            , String deviceName, String locationCoordinates, String entryCompleteConfirmation) {

        syncDataRepo = SyncDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);

        CheckDuplicateRequestBean checkDuplicateRequestBean= syncDataRepo.getCheckDuplicateRequest(loginId, stateShortName, imeiNo,deviceName, locationCoordinates, entryCompleteConfirmation);
        if (checkDuplicateRequestBean.getMember_data().equalsIgnoreCase("")){
            AppUtils.getInstance().showLog("NoDataToCheckDuplicate", AuthViewModel.class);
            return;
        }else {
            syncDataRepo.makeCheckDuplicateRequest(checkDuplicateRequestBean, new RepositoryCallback() {
                @Override
                public void onComplete(Result result) {
                    try {
                        AppUtils.getInstance().showLog("checkDuplicateDataResult" + result.toString(), AuthViewModel.class);
                        if (result instanceof Result.Success) {
                            CheckDuplicateResponseBean checkDuplicateResponseBean = (CheckDuplicateResponseBean) ((Result.Success) result).data;
                            AppUtils.getInstance().showLog("checkDuplicateSuccessResult" + checkDuplicateResponseBean.getMember_code(), HomeViewModel.class);
                            if (!checkDuplicateResponseBean.getMember_code().equalsIgnoreCase("0")) {
                                /*delete  duplicate entries and hit sync api*/
                                deleteDuplicateEntries(checkDuplicateResponseBean.getMember_code());
                            }
                            makeSyncMemberEntry(loginId, stateShortName, imeiNo, deviceName, locationCoordinates, entryCompleteConfirmation);
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
                                    syncApiStatus=exception.getMessage();
                                    AppUtils.getInstance().showLog("CheckDuplicateRetrofitErrors:-------" + exception.getMessage()
                                            , AuthViewModel.class);
                                }
                            }
                        }
                    } catch (Exception e) {
                        syncApiStatus=e.getMessage();
                        AppUtils.getInstance().showLog("checkDuplicateDataResultExp" + e.toString(), AuthViewModel.class);
                    }
                }
            });
        }



    }


    private void makeSyncMemberEntry(String loginId, String stateShortName, String imeiNo
            , String deviceName, String locationCoordinates, String entryCompleteConfirmation) {
        SyncEntriesRequestBean syncEntriesRequestBean = syncDataRepo.getSyncEntriesRequest(loginId, stateShortName, imeiNo, deviceName, locationCoordinates, entryCompleteConfirmation);
        if (syncEntriesRequestBean.getNrlm_entry_sync() != null && syncEntriesRequestBean.getNrlm_entry_sync().size() != 0) {
            syncDataRepo.makeSyncEntriesRequest(syncEntriesRequestBean, new RepositoryCallback() {
                @Override
                public void onComplete(Result result) {
                    try {
                        if (result instanceof Result.Success) {
                            SimpleResponseBean simpleResponseBean = (SimpleResponseBean) ((Result.Success) result).data;

                            /*update sync status in member entry table*/
                            syncApiStatus="E200";

                            updateSyncStatus();

                            AppUtils.getInstance().showLog(simpleResponseBean.getError().getCode() + "---" +
                                    simpleResponseBean.getError().getMessage(), HomeViewModel.class);
                        } else {
                            Object errorObject = ((Result.Error) result).exception;
                            if (errorObject != null) {
                                if (errorObject instanceof SimpleResponseBean.Error) {
                                    SimpleResponseBean.Error responseError = (SimpleResponseBean.Error) errorObject;
                                    syncApiStatus=responseError.getCode();
                                    AppUtils.getInstance().showLog(responseError.getCode() + "SyncEntriesApiErrorObj"
                                            + responseError.getMessage(), AuthViewModel.class);
                                } else if (errorObject instanceof Throwable) {
                                    Throwable exception = (Throwable) errorObject;
                                    syncApiStatus=exception.getMessage();
                                    AppUtils.getInstance().showLog("SyncEntriesRetrofitErrors:-------" + exception.getMessage()
                                            , AuthViewModel.class);
                                }
                            }
                        }
                    } catch (Exception e) {
                        syncApiStatus=e.getMessage();
                        AppUtils.getInstance().showLog("SyncEntriesResultExp" + e.toString(), AuthViewModel.class);

                    }
                }
            });
        }

    }

    public String getSyncApiStatus(){
        return syncApiStatus;
    }


    private void updateSyncStatus() {
        syncDataRepo.updateSyncStatus();
    }


    /********* add method by lincon**********/


    public List<String> loadSectorData() {
        return masterDataRepo.getSectorName();
    }

    public String SectorName(int id) {
        return masterDataRepo.SectorName(id);
    }

    public List<SectorEntity> getAllSectorData() {
        return masterDataRepo.getAllSector();
    }

    public List<String> loadActivityData(int id,String memberCode) {
        return  masterDataRepo.getActivityName(id,memberCode);
    }

    public List<ActivityEntity> getAllActivityData(int id) {
        return masterDataRepo.getAllActivity(id);
    }

    public List<ActivityEntity> getSelectedActivity(int id,String memberCode,String entryFlag){
        List<ActivityEntity> activityData =masterDataRepo.getAllActivity(id);
        List<MemberEntryEntity> entryData = masterDataRepo.getAllMemberForActivity(memberCode,entryFlag);

        if(!entryData.isEmpty()){

            for(int i=0; i<entryData.size();i++){
                for(int j=0;j<activityData.size();j++){
                    if(entryData.get(i).getActivityCode().equalsIgnoreCase(String.valueOf(activityData.get(j).getActivity_code()))){
                        activityData.remove(j);
                    }
                }
            }
           /* for(MemberEntryEntity entryObject:entryData){
                for(ActivityEntity activityObject:activityData){
                    if(entryObject.getActivityCode().equalsIgnoreCase(String.valueOf(activityObject.getActivity_code()))){
                        activityData.remove(activityObject);
                    }
                }
            }*/

        }else {
            activityData =masterDataRepo.getAllActivity(id);
        }
        return activityData;
    }

    public List<ActivityEntity> getAllSelectedActivity(String memberCode,String entryFlag){
        List<ActivityEntity> activityData =masterDataRepo.getAllActivityWithoutSector();
        List<MemberEntryEntity> entryData = masterDataRepo.getAllMemberForActivity(memberCode,entryFlag);

        if(!entryData.isEmpty()){

            for(int i=0; i<entryData.size();i++){
                for(int j=0;j<activityData.size();j++){
                    if(entryData.get(i).getActivityCode().equalsIgnoreCase(String.valueOf(activityData.get(j).getActivity_code()))){
                        activityData.remove(j);
                    }
                }
            }
           /* for(MemberEntryEntity entryObject:entryData){
                for(ActivityEntity activityObject:activityData){
                    if(entryObject.getActivityCode().equalsIgnoreCase(String.valueOf(activityObject.getActivity_code()))){
                        activityData.remove(activityObject);
                    }
                }
            }*/

        }else {
            activityData =masterDataRepo.getAllActivityWithoutSector();
        }
        return activityData;
    }

    public List<String> getSelectedActivityName(int id,String memberCode,String entryFlag){
        List<String> activityName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityName = getSelectedActivity(id,memberCode,entryFlag).stream().map(ActivityEntity::getActivity_name).collect(Collectors.toList());
        }
        return activityName;
    }

    public List<String> getAllSelectedActivityName(String memberCode,String entryFlag){
        List<String> activityName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityName = getAllSelectedActivity(memberCode,entryFlag).stream().map(ActivityEntity::getActivity_name).collect(Collectors.toList());
        }
        return activityName;
    }

    public List<String> loadFrequencyData() {
        return masterDataRepo.getFrequencyName();
    }

    public List<FrequencyEntity> getAllFrequencyData() {
        return masterDataRepo.getAllFrequency();
    }

    public List<String> loadIncomeData(int freqId) {
        return masterDataRepo.getIncomeName(freqId);
    }

    public List<IncomeRangeEntity> getAllIncomeData(int freqId) {
        return masterDataRepo.getAllIncome(freqId);
    }

    public List<String> getLocationFrom() {
        List<String> location = new ArrayList<>();
        location.add("Geography");
        location.add("CBO");
        return location;
    }

    public List<String> loadBlockName() {
        return masterDataRepo.getBlockName();
    }

    public List<BlockDataBean> getAllBlockData() {
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
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getGpListData(blockCode).stream().map(GpDataBean::getGpName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName" + incomeName.size(), MasterDataDao.class);
        return incomeName;

    }

    public List<String> getVillageListName(String gpCode) throws ExecutionException, InterruptedException {
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getVillageListData(gpCode).stream().map(VillageDataBean::getVillageName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName" + incomeName.size(), MasterDataDao.class);
        return incomeName;
    }


    public List<String> getShgListName(String villageCode) throws ExecutionException, InterruptedException {
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getShgListData(villageCode).stream().map(ShgDataBean::getShgName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("incomeName" + incomeName.size(), MasterDataDao.class);
        return incomeName;
    }


    public void insertBeforeNrlmEntryData(MemberEntryEntity memberEntryDataItem) {
        masterDataRepo.insertBeforeNrlmEntry(memberEntryDataItem);
    }


    String getMemberNameDB(String memberCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getMemberNameDB(memberCode);
    }

    String getShgNameDB(String memberCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getShgNameDB(memberCode);
    }

    String getMemberJoiningDate(String memberCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getMemberJoiningDate(memberCode);
    }

    void updateBeforeEntryDateInLocal(String memberCode,String date) throws ExecutionException, InterruptedException {
         masterDataRepo.updateBeforeEntryDateInLocal(memberCode,date);
    }

    void updateAfterEntryDateInLocal(String memberCode,String date) throws ExecutionException, InterruptedException {
        masterDataRepo.updateAfterEntryDateInLocal(memberCode,date);
    }

    public  String getBeforeDate(String memberCode){
       return masterDataRepo.getBeforeLastDate(memberCode);
    }

    public  String getAfterDate(String memberCode){
        return masterDataRepo.getAfterLastDate(memberCode);
    }





    String getMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getMemberCount(shgCode);
    }

    String getBeforeEntryMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getBeforeEntryMemberCount(shgCode);
    }

    String getAfterEntryMemberCount(String shgCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getAfterEntryMemberCount(shgCode);
    }


    public List<String> getUniqueClfName() throws ExecutionException, InterruptedException {
        List<String> incomeName = null;
        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                incomeName = getUniqueClf().stream().map(ClfDataBean::getClf_name).collect(Collectors.toList());
                incomeName.removeIf(Objects::isNull);
            }
            AppUtils.getInstance().showLog("getClf_name" + incomeName.size(), MasterDataDao.class);

        }catch (Exception e){

        }

        return incomeName;
    }

    public List<ClfDataBean> getUniqueClf() throws ExecutionException, InterruptedException {
        return masterDataRepo.getUniqueClf();
    }

    public List<VoDataBean> getUniqueVo(String clfCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getUniqueVo(clfCode);
    }

    public List<String> getUniqueVoName(String clfCode) throws ExecutionException, InterruptedException {
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getUniqueVo(clfCode).stream().map(VoDataBean::getVo_name).collect(Collectors.toList());
            incomeName.removeIf(Objects::isNull);
        }
        AppUtils.getInstance().showLog("getUniqueVoName" + incomeName.size(), MasterDataDao.class);
        return incomeName;
    }

    public List<ShgDataBean> getShgDataWithVo(String voCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getShgDataWithVo(voCode);
    }

    public List<String> getShgNameWithVo(String voCode) throws ExecutionException, InterruptedException {
        List<String> incomeName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incomeName = getShgDataWithVo(voCode).stream().map(ShgDataBean::getShgName).collect(Collectors.toList());
        }
        AppUtils.getInstance().showLog("getShgNameWithVo" + incomeName.size(), MasterDataDao.class);
        return incomeName;
    }

    private void deleteDuplicateEntries(String member_code) {
        try {
            String [] arr=member_code.split(",");
            for(int i=0; i<arr.length;i++){
                String[] singleEntry=arr[i].split("|");
                String shgCode=singleEntry[0];
                String memberCode=singleEntry[1];
                String sectorCode=singleEntry[2];
                String activityCode=singleEntry[3];
                String entryType=singleEntry[4];
                syncDataRepo.deleteDuplicateEntries(shgCode,memberCode,sectorCode,activityCode,entryType);
            }
        }catch (Exception e){
            AppUtils.getInstance().showLog("DuplicateEntriesParingExp"+e.toString(),HomeViewModel.class);
        }

    }

    String getMemberDOJ(String memberCode) throws ExecutionException, InterruptedException {
        return masterDataRepo.getMemberDoj(memberCode);
    }

    public List<String> loadSeccNameData(String memberCode){
        return  masterDataRepo.getSeccNameData(memberCode);
    }

    public List<SeccEntity> getSeccData(String memberCode){
        return  masterDataRepo.getSeccData(memberCode);
    }

    public void deleteActivity(String memberCode,String activityCode){
        masterDataRepo.deleteActivity(memberCode,activityCode);
    }


    public List<MemberEntryEntity> getAllEntryData(String memberCode, String entryStatus){
        return masterDataRepo.getAllMemberDataWithEntryStatus(memberCode,entryStatus);
    }

    public String getSeccStatus(String memberCode){
        return masterDataRepo.getSeccStatus(memberCode);
    }

    public void updateConfirmationStatus(String memberCode, String entryStatus){
         masterDataRepo.updateConfirmationStatus(memberCode,entryStatus);
    }


}

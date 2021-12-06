package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDuplicateResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.repository.SyncDataRepo;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;

import java.util.List;

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

}

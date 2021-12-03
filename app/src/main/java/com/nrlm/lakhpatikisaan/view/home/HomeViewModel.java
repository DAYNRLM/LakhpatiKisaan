package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class HomeViewModel extends ViewModel {
    private MasterDataRepo masterDataRepo;
    public HomeViewModel(){
        masterDataRepo=MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor());

        }

    public void getMasterData() {
        try {
            LogRequestBean logRequestBean=new LogRequestBean("UPAGASSDAD","UP"
                    ,"111","111","111");

            masterDataRepo.makeMasterDataRequest(logRequestBean, new RepositoryCallback() {
                @Override
                public void onComplete(Result result) {
                    AppUtils.getInstance().showLog("masterDataResult"+result.toString(),DashBoardFragment.class);
                    if (result instanceof Result.Success){
                        //fill db
                        MasterDataResponseBean masterDataResponseBean= (MasterDataResponseBean) ((Result.Success) result).data;
                        AppUtils.getInstance().showLog("masterDataResponseBean"+masterDataResponseBean.getLocation_master().toString(),HomeViewModel.class);

                    }else {
                            Object errorObject=((Result.Error) result).exception ;
                            if (errorObject != null){
                                if (errorObject instanceof MasterDataResponseBean.Error){
                                    MasterDataResponseBean.Error responseError= (MasterDataResponseBean.Error) errorObject;
                                    AppUtils.getInstance().showLog(responseError.getCode()+" ApiErrorObj"+responseError.getMessage(),HomeViewModel.class);
                                } else  if (errorObject instanceof Throwable){
                                   Throwable exception= (Throwable)errorObject;
                                    AppUtils.getInstance().showLog("RetrofitErrors:-------"+exception.getMessage(),HomeViewModel.class);
                                }
                            }

                    }
                }
            });


        }catch (Exception e){
            AppUtils.getInstance().showLog("Exceptioncall"+e,DashBoardFragment.class);
    }

    }




    // common alert dialog
    public MutableLiveData<String> commonAleartDialog(Context context){
        MutableLiveData<String> resetData = new MutableLiveData<>();

        new MaterialAlertDialogBuilder(context).setTitle("Test").setIcon(R.drawable.ic_baseline_add_circle_outline_24)
                .setMessage("check")
                .setPositiveButton("ok",(dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetData.setValue("ok");
                }).setNegativeButton("cancel",(dialogInterface, i) -> {

            dialogInterface.dismiss();
            resetData.setValue("cancel");
        }).show();
        return resetData;
    }







}

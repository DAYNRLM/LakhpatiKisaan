package com.nrlm.lakhpatikisaan.view.home;

import android.view.View;

import androidx.lifecycle.ViewModel;

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
            LogRequestBean logRequestBean=new LogRequestBean("HRKSVISHAKHA","hr"
                    ,"111","111","111");
            AppUtils.getInstance().showLog("ObjectCreated", DashBoardFragment.class);
            masterDataRepo.makeMasterDataRequest(logRequestBean, new RepositoryCallback<MasterDataResponseBean>() {
               @Override
               public void onComplete(Result<MasterDataResponseBean> result) {
                   AppUtils.getInstance().showLog("result"+result.toString(),DashBoardFragment.class);
               }
           });


        }catch (Exception e){
            AppUtils.getInstance().showLog("Exceptioncall"+e,DashBoardFragment.class);
    }

    }







}

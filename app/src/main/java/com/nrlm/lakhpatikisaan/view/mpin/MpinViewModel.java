package com.nrlm.lakhpatikisaan.view.mpin;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.nrlm.lakhpatikisaan.database.entity.CheckDeleteShgEntity;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.DeleteShgRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDeleteShgResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.repository.CheckAndDeleteShgRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MpinViewModel extends ViewModel {
    private CheckAndDeleteShgRepo checkAndDeleteShgRepo;
    private HomeViewModel homeViewModel;
    private String apiStatus;

    public void initializeObjects(Context context) {
        checkAndDeleteShgRepo = CheckAndDeleteShgRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        homeViewModel = new HomeViewModel();
    }

   public String makeCheckDeleteShgRequest(Context context, final LogRequestBean logRequestBean) {

        checkAndDeleteShgRepo.makeCheckDeleteShgApiRequest(logRequestBean, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {

                    if (((CheckDeleteShgResponseBean) ((Result.Success) result).data).getShg_data().size() > 0) {
                        /* 1. sync all data
                         *  2. hit delete shg api */
                        homeViewModel.checkDuplicateAtServer(context, logRequestBean.getLogin_id(), logRequestBean.getState_short_name(), logRequestBean.getImei_no(),
                                logRequestBean.getDevice_name(), logRequestBean.getLocation_coordinate(), AppConstant.entryCompleted);

                        List<CheckDeleteShgEntity> checkDeleteShgEntityList= null;
                        try {
                            checkDeleteShgEntityList = getShgToDelete();


                            DeleteShgRequestBean deleteShgRequestBean=new DeleteShgRequestBean();
                            deleteShgRequestBean.setDevice_name(logRequestBean.getDevice_name());
                            deleteShgRequestBean.setImei_no(logRequestBean.getImei_no());
                            deleteShgRequestBean.setLocation_coordinate(logRequestBean.getLocation_coordinate());
                            deleteShgRequestBean.setLogin_id(logRequestBean.getLogin_id());
                            deleteShgRequestBean.setState_short_name(logRequestBean.getState_short_name());

                            String shgCodesForDelete="";
                            for (CheckDeleteShgEntity checkDeleteShgEntity:checkDeleteShgEntityList){
                                shgCodesForDelete+=checkDeleteShgEntity.getShgCode()+",";
                            }

                            deleteShgRequestBean.setShg_code(AppUtils.getInstance().removeComma(shgCodesForDelete));

                            makeDeleteShgRequest(deleteShgRequestBean);
                        } catch (ExecutionException e) {
                            AppUtils.getInstance().showLog("getShgToDeleteExp:-------" + e.getMessage()
                                    , AuthViewModel.class);
                        } catch (InterruptedException e) {
                            AppUtils.getInstance().showLog("getShgToDeleteExp:-------" + e.getMessage()
                                    , AuthViewModel.class);
                        }


                    } else {
                        /*got to dashboard*/
                        apiStatus="No data to delete";
                    }
                } else {
                    /*got to dashboard*/
                    apiStatus="Error in check shg for delete api ";
                }
            }
        });
return apiStatus;
    }

    public void makeDeleteShgRequest(DeleteShgRequestBean deleteShgRequestBean) {
        checkAndDeleteShgRepo.makeDeleteShgApiRequest(deleteShgRequestBean, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success){
                    SimpleResponseBean simpleResponseBean=(SimpleResponseBean) ((Result.Success)result).data;
                    if (simpleResponseBean.getError().getCode().equalsIgnoreCase("E200")){
                        apiStatus ="E200";
                    }else apiStatus=simpleResponseBean.getError().getCode();
                }else {
                    Object errorObject = ((Result.Error) result).exception;
                    if (errorObject != null) {
                        if (errorObject instanceof SimpleResponseBean.Error) {
                            SimpleResponseBean.Error responseError = (SimpleResponseBean.Error) errorObject;
                            apiStatus=responseError.getCode();
                            AppUtils.getInstance().showLog(responseError.getCode() + "SyncEntriesApiErrorObj"
                                    + responseError.getMessage(), AuthViewModel.class);
                        } else if (errorObject instanceof Throwable) {
                            Throwable exception = (Throwable) errorObject;
                            apiStatus=exception.getMessage();
                            AppUtils.getInstance().showLog("SyncEntriesRetrofitErrors:-------" + exception.getMessage()
                                    , AuthViewModel.class);
                        }
                    }
                }
            }
        });
    }

    private List<CheckDeleteShgEntity> getShgToDelete() throws ExecutionException, InterruptedException {
        return checkAndDeleteShgRepo.getShgToDelete();
    }

    public String getApiStatus(){
        return apiStatus;
    }
}

package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SupportiveMastersResponseBean;
import com.nrlm.lakhpatikisaan.repository.LoginRepo;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.home.DashBoardFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

public class AuthViewModel extends ViewModel {
    private LoginRepo loginRepo;
    private MasterDataRepo masterDataRepo;
    public AuthViewModel (){

    }

   public void  makeLoginRequestData(Context context){
       loginRepo=LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(),context);
       masterDataRepo=MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(),context);
      final LoginRequestBean  loginRequestBean=new LoginRequestBean();
       loginRequestBean.setAndroid_api_version("1.0.0");
       loginRequestBean.setAndroid_version("1.0.0");
       loginRequestBean.setApp_login_time("2021-04-13 16:33:23");
       loginRequestBean.setApp_versions("1.0.0");
       loginRequestBean.setDate("06-12-2021");
       loginRequestBean.setDevice_name("OPPO-OP4B79L1-CPH1933");
       loginRequestBean.setImei_no("5d7eaa5ef9d3ebed");
       loginRequestBean.setLocation_coordinate("28.6771787,77.4923927");
       loginRequestBean.setLogout_time("2021-04-13 16:33:23");
       loginRequestBean.setLogin_id("HRKSVISHAKHA");
       loginRequestBean.setPassword("c6024fd19953c32dc6e2b8fe91684a16a889cc8482157f1ec652616517537239");
       makeLogin(loginRequestBean);

   }

    public void makeLogin(LoginRequestBean loginRequestBean){
       loginRepo.makeLoginRequest(loginRequestBean, new RepositoryCallback() {
           @Override
           public void onComplete(Result result) {
               try {
               AppUtils.getInstance().showLog("loginDataResult"+result.toString(), AuthViewModel.class);
               if (result instanceof Result.Success){
                   LoginResponseBean loginResponseBean= (LoginResponseBean) ((Result.Success) result).data;
                   AppUtils.getInstance().showLog("loginDataResponseBean"+loginResponseBean.getError().getCode()+"---"+loginResponseBean.getError().getMessage(), AuthViewModel.class);

                   if (loginResponseBean.getError().getCode().equalsIgnoreCase("E200")){
                          LogRequestBean logRequestBean=new LogRequestBean("UPAGASSDAD","up"
                                   ,"111","111","111");

                           masterDataRepo.makeMasterDataRequest(logRequestBean, new RepositoryCallback() {
                               @Override
                               public void onComplete(Result result) {
                                   AppUtils.getInstance().showLog("masterDataResult"+result.toString(),AuthViewModel.class);
                                   if (result instanceof Result.Success){
                                       MasterDataResponseBean masterDataResponseBean= (MasterDataResponseBean) ((Result.Success) result).data;
                                       AppUtils.getInstance().showLog("masterDataResponseBean"+masterDataResponseBean.getError().getCode()+"---"
                                               +masterDataResponseBean.getError().getMessage(),AuthViewModel.class);

                                   }else {
                                       Object errorObject=((Result.Error) result).exception ;
                                       if (errorObject != null){
                                           if (errorObject instanceof MasterDataResponseBean.Error){
                                               MasterDataResponseBean.Error responseError= (MasterDataResponseBean.Error) errorObject;
                                               AppUtils.getInstance().showLog(responseError.getCode()+" MasterApiErrorObj"
                                                       +responseError.getMessage(),AuthViewModel.class);
                                           } else  if (errorObject instanceof Throwable){
                                               Throwable exception= (Throwable)errorObject;
                                               AppUtils.getInstance().showLog("MasterRetrofitErrors:-------"+exception.getMessage()
                                                       ,AuthViewModel.class);
                                           }
                                       }

                                   }
                               }
                           });

                           masterDataRepo.makeSupportiveMasterDataRequest(logRequestBean, new RepositoryCallback() {
                               @Override
                               public void onComplete(Result result) {
                                   AppUtils.getInstance().showLog("supportiveMasterDataResult"+result.toString(),AuthViewModel.class);
                                   if (result instanceof Result.Success){
                                       SupportiveMastersResponseBean supportiveMastersResponseBean = (SupportiveMastersResponseBean) ((Result.Success) result).data;
                                       AppUtils.getInstance().showLog("supportiveMasterDataResponseBean"+supportiveMastersResponseBean.getError().getCode()+"---"
                                               +supportiveMastersResponseBean.getError().getMessage(),AuthViewModel.class);

                                   }else {
                                       Object errorObject=((Result.Error) result).exception ;
                                       if (errorObject != null){
                                           if (errorObject instanceof SupportiveMastersResponseBean.Error){
                                               SupportiveMastersResponseBean.Error responseError= (SupportiveMastersResponseBean.Error) errorObject;
                                               AppUtils.getInstance().showLog(responseError.getCode()+"SupportiveMasterApiErrorObj"
                                                       +responseError.getMessage(),AuthViewModel.class);
                                           } else  if (errorObject instanceof Throwable){
                                               Throwable exception= (Throwable)errorObject;
                                               AppUtils.getInstance().showLog("SupportiveMasterRetrofitErrors:-------"+exception.getMessage()
                                                       ,AuthViewModel.class);
                                           }
                                       }

                                   }

                               }
                           });
                   }

               }else {
                   Object errorObject=((Result.Error) result).exception ;
                   if (errorObject != null){
                       if (errorObject instanceof MasterDataResponseBean.Error){
                           LoginResponseBean.Error responseError= (LoginResponseBean.Error) errorObject;
                           AppUtils.getInstance().showLog(responseError.getCode()+" loginApiErrorObj"+responseError.getMessage(),AuthViewModel.class);
                       } else  if (errorObject instanceof Throwable){
                           Throwable exception= (Throwable)errorObject;
                           AppUtils.getInstance().showLog("RetrofitErrorsLogin:-------"+exception.getMessage(),AuthViewModel.class);
                       }
                   }

               }
               }catch (Exception e){
                   AppUtils.getInstance().showLog("Exceptioncall"+e,DashBoardFragment.class);
               }
           }
       });
    }

    public String loginApiResult(String result){
        return result;
    }
}

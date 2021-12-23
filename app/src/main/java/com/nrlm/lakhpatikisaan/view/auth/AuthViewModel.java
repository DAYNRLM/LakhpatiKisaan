package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.ViewModel;

import com.nrlm.lakhpatikisaan.database.dbbean.LgdVillageCode;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.OtpRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.ResetPasswordBean;
import com.nrlm.lakhpatikisaan.network.model.request.SeccRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.OtpResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SeccResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SimpleResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.SupportiveMastersResponseBean;
import com.nrlm.lakhpatikisaan.repository.LoginRepo;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.home.DashBoardFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AuthViewModel extends ViewModel {
    private LoginRepo loginRepo;
    private MasterDataRepo masterDataRepo;
    private String loginApiStatus = "";
    public String resetPasswordApiStatuss = "";
    public SimpleResponseBean simpleResponseBean;
    public OtpResponseBean otpResponseBean;
    private HomeViewModel homeViewModel;

    public AuthViewModel() {

    }

    public void init(Context context) {

        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        masterDataRepo = MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        homeViewModel = new HomeViewModel();

    }

    public void makeLogin(LoginRequestBean loginRequestBean,Context context) {
        loginRepo.makeLoginRequest(loginRequestBean, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                try {
                    AppUtils.getInstance().showLog("loginDataResult" + result.toString(), AuthViewModel.class);
                    if (result instanceof Result.Success) {

                        LoginResponseBean loginResponseBean = (LoginResponseBean) ((Result.Success) result).data;
                        AppUtils.getInstance().showLog("loginDataResponseBean" + loginResponseBean.getError().getCode() + "---" +
                                loginResponseBean.getError().getMessage(), AuthViewModel.class);

                      //  String stateShortName=loginRepo.getStateNameDB();
                        if (loginResponseBean.getError().getCode().equalsIgnoreCase("E200") && loginResponseBean.getState_short_name()!=null) {
                            loginApiStatus = loginResponseBean.getError().getCode();
                          PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefStateShortName(),loginResponseBean.getState_short_name(),context);
                            LogRequestBean logRequestBean = new LogRequestBean(loginRequestBean.getLogin_id(), loginResponseBean.getState_short_name()
                                    ,loginRequestBean.getImei_no() , loginRequestBean.getDevice_name(), loginRequestBean.getLocation_coordinate());


                            masterDataRepo.makeMasterDataRequest(logRequestBean, new RepositoryCallback() {
                                @Override
                                public void onComplete(Result result) {
                                    AppUtils.getInstance().showLog("masterDataResult" + result.toString(), AuthViewModel.class);
                                    if (result instanceof Result.Success) {
                                        MasterDataResponseBean masterDataResponseBean = (MasterDataResponseBean) ((Result.Success) result).data;
                                        AppUtils.getInstance().showLog("masterDataResponseBean" + masterDataResponseBean.getError().getCode() + "---"
                                                + masterDataResponseBean.getError().getMessage(), AuthViewModel.class);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    List<LgdVillageCode> lgdVillageCodeList = getLgdVillageCodes();

                                                    AppUtils.getInstance().showLog("lgdCodesListSize:- "+lgdVillageCodeList.size(),AuthViewModel.class);
                                                    SeccRequestBean seccRequestBean = new SeccRequestBean();

                                                    seccRequestBean.setDevice_name(logRequestBean.getDevice_name());
                                                    seccRequestBean.setImei_no(logRequestBean.getImei_no());
                                                    seccRequestBean.setLocation_coordinate(logRequestBean.getLocation_coordinate());
                                                    seccRequestBean.setLogin_id(logRequestBean.getLogin_id());
                                                    seccRequestBean.setState_short_name(logRequestBean.getState_short_name());

                                                    String lgdVillageCodes="";

                                                    for (LgdVillageCode lgdVillageCode:lgdVillageCodeList){
                                                        lgdVillageCodes+=lgdVillageCode.getLgd_village_code()+",";
                                                    }

                                                    AppUtils.getInstance().showLog("lgdCodesFromDb"+AppUtils.getInstance().removeComma(lgdVillageCodes),AuthViewModel.class);

                                                    seccRequestBean.setLgd_village_code(AppUtils.getInstance().removeComma(lgdVillageCodes));

                                                    masterDataRepo.makeSeccDataRequest(seccRequestBean, new RepositoryCallback() {
                                                        @Override
                                                        public void onComplete(Result result) {
                                                            AppUtils.getInstance().showLog("SeccMasterDataResult" + result.toString(), AuthViewModel.class);
                                                            if (result instanceof Result.Success) {
                                                                SeccResponseBean seccResponseBean = (SeccResponseBean) ((Result.Success) result).data;
                                                                AppUtils.getInstance().showLog("SeccrDataResponseBean" + seccResponseBean.getError().getCode() + "---"
                                                                        + seccResponseBean.getError().getMessage(), AuthViewModel.class);

                                                            } else {
                                                                Object errorObject = ((Result.Error) result).exception;
                                                                if (errorObject != null) {
                                                                    if (errorObject instanceof SupportiveMastersResponseBean.Error) {
                                                                        SeccResponseBean.Error responseError = (SeccResponseBean.Error) errorObject;
                                                                        AppUtils.getInstance().showLog(responseError.getCode() + "SeccApiErrorObj"
                                                                                + responseError.getMessage(), AuthViewModel.class);
                                                                    } else if (errorObject instanceof Throwable) {
                                                                        Throwable exception = (Throwable) errorObject;
                                                                        AppUtils.getInstance().showLog("SeccRetrofitErrors:-------" + exception.getMessage()
                                                                                , AuthViewModel.class);
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    });

                                                } catch (ExecutionException e) {
                                                    AppUtils.getInstance().showLog("makeSeccDataRequestExp:- "+e.getMessage(),AuthViewModel.class);
                                                } catch (InterruptedException e) {
                                                    AppUtils.getInstance().showLog("makeSeccDataRequestExp:- "+e.getMessage(),AuthViewModel.class);
                                                }
                                            }
                                        },6000);

                                    } else {
                                        Object errorObject = ((Result.Error) result).exception;
                                        if (errorObject != null) {
                                            if (errorObject instanceof MasterDataResponseBean.Error) {
                                                MasterDataResponseBean.Error responseError = (MasterDataResponseBean.Error) errorObject;
                                                AppUtils.getInstance().showLog(responseError.getCode() + " MasterApiErrorObj"
                                                        + responseError.getMessage(), AuthViewModel.class);
                                            } else if (errorObject instanceof Throwable) {
                                                Throwable exception = (Throwable) errorObject;
                                                AppUtils.getInstance().showLog("MasterRetrofitErrors:-------" + exception.getMessage()
                                                        , AuthViewModel.class);
                                            }
                                        }

                                    }
                                }
                            });

                            masterDataRepo.makeSupportiveMasterDataRequest(logRequestBean, new RepositoryCallback() {
                                @Override
                                public void onComplete(Result result) {
                                    AppUtils.getInstance().showLog("supportiveMasterDataResult" + result.toString(), AuthViewModel.class);
                                    if (result instanceof Result.Success) {
                                        SupportiveMastersResponseBean supportiveMastersResponseBean = (SupportiveMastersResponseBean) ((Result.Success) result).data;
                                        AppUtils.getInstance().showLog("supportiveMasterDataResponseBean" + supportiveMastersResponseBean.getError().getCode() + "---"
                                                + supportiveMastersResponseBean.getError().getMessage(), AuthViewModel.class);




                                    } else {
                                        Object errorObject = ((Result.Error) result).exception;
                                        if (errorObject != null) {
                                            if (errorObject instanceof SupportiveMastersResponseBean.Error) {
                                                SupportiveMastersResponseBean.Error responseError = (SupportiveMastersResponseBean.Error) errorObject;
                                                AppUtils.getInstance().showLog(responseError.getCode() + "SupportiveMasterApiErrorObj"
                                                        + responseError.getMessage(), AuthViewModel.class);
                                            } else if (errorObject instanceof Throwable) {
                                                Throwable exception = (Throwable) errorObject;
                                                AppUtils.getInstance().showLog("SupportiveMasterRetrofitErrors:-------" + exception.getMessage()
                                                        , AuthViewModel.class);
                                            }
                                        }

                                    }

                                }
                            });


                        }else loginApiStatus = "";

                        } else {
                        //Object errorObject = ;
                        AppUtils.getInstance().showLog("ClassTypeError" + ((Result.Error) result).exception.getClass(), AuthViewModel.class);
                        if (((Result.Error) result).exception != null) {
                            if (((Result.Error) result).exception instanceof LoginResponseBean.Error) {
                                LoginResponseBean.Error responseError = (LoginResponseBean.Error) ((Result.Error) result).exception;
                                loginApiStatus = responseError.getCode();
                                AppUtils.getInstance().showLog(responseError.getCode() + " loginApiErrorObj" + responseError.getMessage(), AuthViewModel.class);
                            } else if (((Result.Error) result).exception instanceof Throwable) {
                                Throwable exception = (Throwable) ((Result.Error) result).exception;
                                loginApiStatus = exception.getMessage();
                                AppUtils.getInstance().showLog("RetrofitErrorsLogin:-------" + exception.getMessage(), AuthViewModel.class);
                            }
                        }

                    }
                } catch (Exception e) {
                    AppUtils.getInstance().showLog("Exceptioncall" + e, DashBoardFragment.class);
                }
            }
        });
    }


    public void syncAllData(Context context, String loginId, String stateShortName, String imei,
                            String deviceName, String location) {
        homeViewModel.checkDuplicateAtServer(context, loginId, stateShortName, imei,
                deviceName, location, AppConstant.entryCompleted);
    }

 public void deleteAllMasterDataLg()
 {
     loginRepo.deleteAllMaster();
 }
    public void makeOtpRequest(Context context) {
        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        String otp = AppUtils.getInstance().getRandomOtp();
        OtpRequestBean otpRequestBean = new OtpRequestBean();
        otpRequestBean.setMobile(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getForgotMobileNumber(), context));
        otpRequestBean.setOtpMessage(otp);
        ViewUtilsKt.toast(context, otp);


        PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getRandomOtp(), otp, context);
        AppUtils.getInstance().showLog("OTP " + otp, AuthViewModel.class);

        OtpRequest(otpRequestBean);


    }

    private void OtpRequest(OtpRequestBean otpRequestBean) {
        loginRepo.callOtpServices(otpRequestBean, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                AppUtils.getInstance().showLog("OtpResult" + result.toString(), AuthViewModel.class);
                if (result instanceof Result.Success) {
                    otpResponseBean = (OtpResponseBean) ((Result.Success) result).data;

                }


            }
        });


    }

    public void ResetPasswordRequestData(Context context) {
        ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        resetPasswordBean.setPassword(AppUtils.getInstance().getSha256(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefFrgtPass(), context)));
        resetPasswordBean.setDevice_name(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefDeviceinfo(), context));
        resetPasswordBean.setImei_no(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(), context));
        resetPasswordBean.setLocation_coordinate("28.6771787,77.4923927");
        resetPasswordBean.setLogin_id(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(), context));
        ResetPassword(resetPasswordBean);

    }

    private void ResetPassword(ResetPasswordBean resetPasswordBean) {
        loginRepo.resetPasswordRequestLog(resetPasswordBean, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    simpleResponseBean = (SimpleResponseBean) ((Result.Success) result).data;
                    resetPasswordApiStatuss = "E200";

                }
            }
        });

    }

    private List<LgdVillageCode> getLgdVillageCodes() throws ExecutionException, InterruptedException {
        return loginRepo.getLgdVillageCodes();
    }

    public String loginApiResult() {
        return loginApiStatus;

    }


    public void deleteAllMaster(){
        loginRepo.deleteAllMaster();
    }





}

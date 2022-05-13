package com.nrlm.lakhpatikisaan.view.auth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nrlm.lakhpatikisaan.R;
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
import com.nrlm.lakhpatikisaan.utils.AppDeviceInfoUtils;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.home.DashBoardFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AuthViewModel extends ViewModel {
    private LoginRepo loginRepo;
    private MasterDataRepo masterDataRepo;
    private String loginApiStatus = "";
    public String resetPasswordApiStatuss = "";
    public SimpleResponseBean simpleResponseBean;
    public OtpResponseBean otpResponseBean;
    private HomeViewModel homeViewModel;
    private TelephonyManager telephonyManager;


    public AuthViewModel() {

    }

    public void init(Context context) {

        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        masterDataRepo = MasterDataRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        homeViewModel = new HomeViewModel();

    }

    public void makeLogin(LoginRequestBean loginRequestBean, Context context) {
        /*******make json object is encrypted and *********/
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreq=gson.toJson(loginRequestBean);

            encryptedObject.addProperty("data",cryptography.encrypt(logreq.toString()));
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
        loginRepo.makeLoginRequest(encryptedObject, new RepositoryCallback() {
            @Override
            public void onComplete(Result result) {
                try {
                    AppUtils.getInstance().showLog("loginDataResult" + result.toString(), AuthViewModel.class);
                    if (result instanceof Result.Success) {

                        LoginResponseBean loginResponseBean = (LoginResponseBean) ((Result.Success) result).data;
                        AppUtils.getInstance().showLog("loginDataResponseBean" + loginResponseBean.getError().getCode() + "---" +
                                loginResponseBean.getError().getMessage(), AuthViewModel.class);

                        //  String stateShortName=loginRepo.getStateNameDB();
                        if (loginResponseBean.getError().getCode().equalsIgnoreCase("E200") && loginResponseBean.getState_short_name() != null) {
                            loginApiStatus = loginResponseBean.getError().getCode();
                            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefStateShortName(), loginResponseBean.getState_short_name(), context);
                            LogRequestBean logRequestBean = new LogRequestBean(loginRequestBean.getLogin_id(), loginResponseBean.getState_short_name()
                                    , loginRequestBean.getImei_no(), loginRequestBean.getDevice_name(), loginRequestBean.getLocation_coordinate());


                            /*masterDataRepo.makeMasterDataRequest(logRequestBean, new RepositoryCallback() {
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
                                                    AppUtils.getInstance().showLog("lgdCodesListSize:- " + lgdVillageCodeList.size(), AuthViewModel.class);
                                                    SeccRequestBean seccRequestBean = new SeccRequestBean();
                                                    seccRequestBean.setDevice_name(logRequestBean.getDevice_name());
                                                    seccRequestBean.setImei_no(logRequestBean.getImei_no());
                                                    seccRequestBean.setLocation_coordinate(logRequestBean.getLocation_coordinate());
                                                    seccRequestBean.setLogin_id(logRequestBean.getLogin_id());
                                                    seccRequestBean.setState_short_name(logRequestBean.getState_short_name());
                                                    String lgdVillageCodes = "";
                                                    for (LgdVillageCode lgdVillageCode : lgdVillageCodeList) {
                                                        lgdVillageCodes += lgdVillageCode.getLgd_village_code() + ",";
                                                    }
                                                    AppUtils.getInstance().showLog("lgdCodesFromDb" + AppUtils.getInstance().removeComma(lgdVillageCodes), AuthViewModel.class);
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
                                                    AppUtils.getInstance().showLog("makeSeccDataRequestExp:- " + e.getMessage(), AuthViewModel.class);
                                                } catch (InterruptedException e) {
                                                    AppUtils.getInstance().showLog("makeSeccDataRequestExp:- " + e.getMessage(), AuthViewModel.class);
                                                }
                                            }
                                        }, 6000);
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
                            });*/

                            getMasterData(logRequestBean);

                            /*masterDataRepo.makeSupportiveMasterDataRequest(logRequestBean, new RepositoryCallback() {
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
                            });*/

                            getSupportiveMasters(logRequestBean);


                        }
                        else loginApiStatus = "";

                    } else {
                        //Object errorObject = ;
                        AppUtils.getInstance().showLog("ClassTypeError" + ((Result.Error) result).exception.getClass(), AuthViewModel.class);
                        if (((Result.Error) result).exception != null) {
                            if (((Result.Error) result).exception instanceof LoginResponseBean.Error) {
                                LoginResponseBean.Error responseError = (LoginResponseBean.Error) ((Result.Error) result).exception;
                                loginApiStatus = responseError.getCode().trim();
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
        });    }


    public void syncAllData(Context context, String loginId, String stateShortName, String imei,
                            String deviceName, String location) {
        homeViewModel.checkDuplicateAtServer(context, loginId, stateShortName, imei,
                deviceName, location, AppConstant.entryCompleted);
    }

    public void deleteAllMasterDataLg() {
        loginRepo.deleteAllMaster();
    }

    public void makeOtpRequest(Context context) {
        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        String otp = AppUtils.getInstance().getRandomOtp();
        OtpRequestBean otpRequestBean = new OtpRequestBean();
        otpRequestBean.setMobileno(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getForgotMobileNumber(), context));
        otpRequestBean.setMessage(otp + " (" + context.getResources().getString(R.string.app_name) + ")");
        //ViewUtilsKt.toast(context, otp);


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
                    AppUtils.getInstance().showLog("Responseotp : -" + otpResponseBean.getError().getCode(), AuthViewModel.class);
                    loginApiStatus=otpResponseBean.getError().getCode();
                    return;

                }else {

                    AppUtils.getInstance().showLog("OtpResultError" + ((Result.Error) result).exception.getClass(), AuthViewModel.class);
                    if (((Result.Error) result).exception != null) {
                        if (((Result.Error) result).exception instanceof OtpResponseBean.Error) {
                            OtpResponseBean.Error responseError = (OtpResponseBean.Error) ((Result.Error) result).exception;
                            loginApiStatus = responseError.getMessage();
                            AppUtils.getInstance().showLog(responseError.getCode() + "OtpApiErrorObj" + responseError.getMessage(), AuthViewModel.class);
                        } else if (((Result.Error) result).exception instanceof Throwable) {
                            Throwable exception = (Throwable) ((Result.Error) result).exception;
                            loginApiStatus = "E500";
                            AppUtils.getInstance().showLog("RetrofitErrorsOtp:-------" + exception.getMessage(), AuthViewModel.class);
                        }
                    }

                }
            }
        });
    }

    public void ResetPasswordRequestData(Context context,String userId) {
        ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
        loginRepo = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), context);
        resetPasswordBean.setPassword(AppUtils.getInstance().getSha256(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefFrgtPass(), context)));
        resetPasswordBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());
        resetPasswordBean.setImei_no(getIMEINo1(context));
        resetPasswordBean.setMobileno(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getForgotMobileNumber(), context));
        resetPasswordBean.setLocation_coordinate("28.6771787,77.4923927");
        resetPasswordBean.setLogin_id(userId);
        ResetPassword(resetPasswordBean);

    }
    public String getIMEINo1(Context context) {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount(context) > 0) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Build.getSerial();

                    AppUtils.getInstance().showLog("BUILD SERIAL " + Build.getSerial(), AppDeviceInfoUtils.class);

                } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);
                    AppUtils.getInstance().showLog("BUILD SERIAL-imeiNo1 " + imeiNo1, AppDeviceInfoUtils.class);

                } else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    imeiNo1 = "dummy_123456789";
                    AppUtils.getInstance().showLog("BUILD SERIAL-dummy " + imeiNo1, AppDeviceInfoUtils.class);
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        } catch (Exception e) {
            AppUtils.getInstance().showLog("Expection in imeiiiiii: " + e, AppDeviceInfoUtils.class);
        }
        //appSharedPreferences.setImeiNumber(imeiNo1);
        AppUtils.getInstance().showLog("imeiiiiii: " + imeiNo1, AppDeviceInfoUtils.class);
        return imeiNo1;
    }
    private int getSIMSlotCount(Context context) {
        int getPhoneCount = 0;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPhoneCount = telephonyManager.getPhoneCount();
            }
        } catch (Exception e) {
            AppUtils.getInstance().showLog("Expection: " + e, AppDeviceInfoUtils.class);
        }
        AppUtils.getInstance().showLog("getSimSlotCount: " + getPhoneCount, AppDeviceInfoUtils.class);
        return getPhoneCount;
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

    public void getMasterData(LogRequestBean logRequestBean){
        JsonObject encryptedObject =new JsonObject();
        try {
            Cryptography cryptography = new Cryptography();
            Gson gson=new Gson();
            String logreqBean=gson.toJson(logRequestBean);
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

        masterDataRepo.makeMasterDataRequest(encryptedObject, new RepositoryCallback() {
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

                                AppUtils.getInstance().showLog("lgdCodesListSize:- " + lgdVillageCodeList.size(), AuthViewModel.class);
                                SeccRequestBean seccRequestBean = new SeccRequestBean();

                                seccRequestBean.setDevice_name(logRequestBean.getDevice_name());
                                seccRequestBean.setImei_no(logRequestBean.getImei_no());
                                seccRequestBean.setLocation_coordinate(logRequestBean.getLocation_coordinate());
                                seccRequestBean.setLogin_id(logRequestBean.getLogin_id());
                                seccRequestBean.setState_short_name(logRequestBean.getState_short_name());

                                String lgdVillageCodes = "";

                                for (LgdVillageCode lgdVillageCode : lgdVillageCodeList) {
                                    lgdVillageCodes += lgdVillageCode.getLgd_village_code() + ",";
                                }

                                AppUtils.getInstance().showLog("lgdCodesFromDb" + AppUtils.getInstance().removeComma(lgdVillageCodes), AuthViewModel.class);

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
                                AppUtils.getInstance().showLog("makeSeccDataRequestExp:- " + e.getMessage(), AuthViewModel.class);
                            } catch (InterruptedException e) {
                                AppUtils.getInstance().showLog("makeSeccDataRequestExp:- " + e.getMessage(), AuthViewModel.class);
                            }
                        }
                    }, 6000);

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
    }

    public void getSupportiveMasters(LogRequestBean logRequestBean){

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
    }


}

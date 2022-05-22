package com.nrlm.lakhpatikisaan.view.auth;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;


import com.google.gson.Gson;
import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppDateFactory;
import com.nrlm.lakhpatikisaan.utils.AppDeviceInfoUtils;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.mpin.MpinActivity;

import org.json.JSONException;

import java.util.Locale;

public class AuthFragment extends BaseFragment<AuthViewModel, FragmentAuthLoginBinding> {

    private ProgressDialog progressDialog;
    private AuthViewModel authViewModel;
    private TelephonyManager telephonyManager;
    private String loginApiStatus="E2";

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentAuthLoginBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAuthLoginBinding.inflate(inflater, container, false);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onFragmentReady() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(getContext());
        progressDialog=new ProgressDialog(getContext());
        String loginId = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(), getCurrentContext());
        String stateShortName = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(), getCurrentContext());
        String imei = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(), getCurrentContext());
        String deviceInfoSaved = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefDeviceinfo(), getContext());

        if (NetworkFactory.isInternetOn(getContext()))
            if (!(loginId.equalsIgnoreCase("")) && !(stateShortName.equalsIgnoreCase("")))
                AppUtils.getInstance().showLog("imei0000" + imei, AuthFragment.class);
                authViewModel.syncAllData(getContext(), loginId, stateShortName, imei, deviceInfoSaved, ".2719545,.3145555");


        binding.tvForgetPassword.setOnClickListener(v -> {
            NavDirections action = AuthFragmentDirections.actionAuthFragmentToSendOtpFragment();
            navController.navigate(action);
        });
        if (android.os.Build.VERSION.SDK_INT < 11) {
            binding.etPassword.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.clear();
                }
            });
        } else {
            binding.etPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub

                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }
        binding.btnLogin.setOnClickListener(v -> {



            String password = binding.etPassword.getText().toString();
            String userId = binding.etUserId.getText().toString().trim().toUpperCase().replaceAll("\\s","");
            if (userId.equalsIgnoreCase("")) {
                binding.etUserId.setError(getString(R.string.invalid_userid));
            } else if (password.equalsIgnoreCase("")) {
                binding.etPassword.setError(getString(R.string.invalid_password));
            } else {
                if (!(loginId.equalsIgnoreCase("")) && !(stateShortName.equalsIgnoreCase(""))) {
                 //   authViewModel.deleteAllMasterDataLg();
                }
                AppUtils.getInstance().showLog("sha256Pass" + AppUtils.getInstance().getSha256(password), AuthFragment.class);
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                String imeiNo = getIMEINo1(getContext());
                String deviceInfo = AppUtils.getInstance().getDeviceInfo();
                AppUtils.getInstance().showLog("imeiNoFinal" + imeiNo, AuthFragment.class);
                if (imeiNo!=null &&!imeiNo.equalsIgnoreCase(""))
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefImeiNo(), imeiNo, getContext());
                if (deviceInfo != null && !deviceInfo.equalsIgnoreCase(""))
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefDeviceinfo(), deviceInfo, getContext());
                    PreferenceFactory.getInstance().getSharedPreferenceIntegerData(PreferenceKeyManager.getPrefKeyMstData(),getContext());

                if (NetworkFactory.isInternetOn(getContext())) {

                    final LoginRequestBean loginRequestBean = new LoginRequestBean();
                    /*  -------------lOCAL-----------------*/
                 /*   loginRequestBean.setLogin_id("UPAGASSDAD");
                    loginRequestBean.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
                    loginRequestBean.setImei_no("5d7eaa5ef9d3ebed");*/
                    /*---------------LIVE------------------*/
                    loginRequestBean.setLogin_id(userId);
                    loginRequestBean.setPassword(AppUtils.getInstance().getSha256(password));

                    loginRequestBean.setImei_no(imeiNo);
                 //   loginRequestBean.setImei_no("5d7eaa5ef9d3e");
                    loginRequestBean.setAndroid_api_version(AppUtils.getInstance().getAndroidApiVersion());
                    loginRequestBean.setAndroid_version("10");
                  // String apploginTime=AppDateFactory.getInstance().getCurrentDateAndTime();
                  // String apploginTime="2022-02-16 14:50:48";

                    loginRequestBean.setApp_login_time(AppDateFactory.getInstance().getCurrentDateAndTime());
                    loginRequestBean.setApp_versions(BuildConfig.VERSION_NAME);
                    loginRequestBean.setDate(AppDateFactory.getInstance().getCurrentDate());
                    loginRequestBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());

                    loginRequestBean.setLocation_coordinate("28.6771787,77.4923927");
                    loginRequestBean.setLogout_time(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLogoutTime(), getContext()));


                    authViewModel.makeLogin(loginRequestBean, getContext());
                    AppUtils.getInstance().showLog("Initial request"+new Gson().toJson(loginRequestBean).toString(),AuthFragment.class);

                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                           loginApiStatus = authViewModel.loginApiResult();

                            AppUtils.getInstance().showLog("loginApiStatusTTTTT" + loginApiStatus, AuthFragment.class);
                            progressDialog.dismiss();
                            if (loginApiStatus.equalsIgnoreCase("E200")) {
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginSessionKey(), "logedin", getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginId(), userId, getContext());
                                intentToMpin();

                            } else {
                                try {
                                    AppUtils.getInstance().showLog("loginApiStatusdf" + loginApiStatus, AuthFragment.class);
                                    showServerError(loginApiStatus);

                                } catch (Exception e) {
                                    AppUtils.getInstance().showLog("loginApiStatusdfdfdfdf" + loginApiStatus, AuthFragment.class);
                                }
                            }
                        }
                    }, 40000);
                } else {
                    try {
                        progressDialog.dismiss();
                        showServerError(AppConstant.noInternetCode);
                    } catch (JSONException e) {
                        AppUtils.getInstance().showLog("NoInternetElse" + e.getMessage(), AuthFragment.class);
                    }
                }
            }

        });

        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkFactory.isInternetOn(getCurrentContext())) {
                    NavDirections action = AuthFragmentDirections.actionAuthFragmentToSignUpFragment();
                    navController.navigate(action);
                }else
                {
                    try {
                        progressDialog.dismiss();
                        showServerError(AppConstant.noInternetCode);
                    } catch (JSONException e) {
                        AppUtils.getInstance().showLog("NoInternetElse" + e.getMessage(), AuthFragment.class);
                    }

                }
            }
        });
    }

    private void updateApplication() {
       // final String appPackageName = context.getPackageName();
        try {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/11ai-E0CY-RshvTO3aHAISREQi74kEhb6/view?usp=sharing")));
            getCurrentContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nrlm.lakhpatikisaan")));
        } catch (android.content.ActivityNotFoundException anfe) {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://nrlm.gov.in/outerReportAction.do?methodName=showIndex")));
            getCurrentContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nrlm.lakhpatikisaan")));

        }
        //((Activity) context).finish();
        ((Activity) getCurrentContext()).finish();
    }
    public void showServerError(String error_code) throws JSONException {
        switch (error_code) {

            case "E202":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_fields)
                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, true
                );

                break;

            case "E208":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_device)
                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, true
                );

                break;


            case "E201":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_security_validation)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E1004":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_validation)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E206":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_session_exist)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "12163":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.NO_INTERNET_TITLE)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E22":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.login_attempt_exceded)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E203":

            case "E305":

                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_id_or_pass)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;
            case "E23":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_login_time)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;
            case "E2":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, "Info"
                        , "Invalid App version", "Update", (dialog, which) -> updateApplication(), "Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            ((Activity) getCurrentContext()).finish();
                        },false);
                break;


            default:
                if (loginApiStatus.trim().equalsIgnoreCase("E2")){

                    DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, "Info"
                            , "Invalid App version", "Update",
                            (dialog, which) -> updateApplication(),
                            "Cancel", (dialog, which) -> {
                                dialog.dismiss();
                                ((Activity) getCurrentContext()).finish();
                            },false);
                    return;
                }else {
                    DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.SERVER_ERROR_TITLE)
                            , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                    );
                }
              break;
        }
    }

    private void intentToMpin() {
        Intent intent = new Intent(getContext(), MpinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public String getIMEINo1(Context context) {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount(context) > 0) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Build.getSerial();

                    AppUtils.getInstance().showLog("BUILD SERIAL " + Build.getSerial(), AppDeviceInfoUtils.class);

                } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);
                    AppUtils.getInstance().showLog("BUILD SERIAL-imeiNo1 " + imeiNo1, AppDeviceInfoUtils.class);

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

}

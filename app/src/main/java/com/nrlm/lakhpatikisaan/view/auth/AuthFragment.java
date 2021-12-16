package com.nrlm.lakhpatikisaan.view.auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

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

public class AuthFragment extends BaseFragment<AuthViewModel, FragmentAuthLoginBinding> {

    private ProgressDialog progressDialog;
    private AuthViewModel authViewModel;
    private TelephonyManager telephonyManager;

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

        binding.tvForgetPassword.setOnClickListener(v -> {
            NavDirections action = AuthFragmentDirections.actionAuthFragmentToSendOtpFragment();
            navController.navigate(action);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString();
            String userId = binding.etUserId.getText().toString().trim().toUpperCase();
            if (userId.equalsIgnoreCase("")) {
                binding.etUserId.setError(getString(R.string.invalid_userid));
            } else if (password.equalsIgnoreCase("")) {
                binding.etPassword.setError(getString(R.string.invalid_password));
            } else {
                AppUtils.getInstance().showLog("sha256Pass" + AppUtils.getInstance().getSha256(password), AuthFragment.class);
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                String imeiNo = getIMEINo1(getContext());
                AppUtils.getInstance().showLog("imeiNoFinal" + imeiNo, AuthFragment.class);
                if (!imeiNo.equalsIgnoreCase(""))
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefImeiNo(), imeiNo, getContext());
                if (NetworkFactory.isInternetOn(getContext())) {

                    final LoginRequestBean loginRequestBean = new LoginRequestBean();
                    /*  -------------lOCAL-----------------*/
                   /* loginRequestBean.setLogin_id("UPAGASSDAD");
                    loginRequestBean.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
                    loginRequestBean.setImei_no("5d7eaa5ef9d3ebed");*/

                    /*---------------LIVE------------------*/
                    loginRequestBean.setLogin_id(userId);
                    loginRequestBean.setPassword(AppUtils.getInstance().getSha256(password));
                    loginRequestBean.setImei_no("5d7eaa5ef9d3ebe");

                    loginRequestBean.setAndroid_api_version(AppUtils.getInstance().getAndroidApiVersion());
                    loginRequestBean.setAndroid_version("10");
                    loginRequestBean.setApp_login_time(AppDateFactory.getInstance().getCurrentDateAndTime());
                    loginRequestBean.setApp_versions(BuildConfig.VERSION_NAME);
                    loginRequestBean.setDate(AppDateFactory.getInstance().getCurrentDate());
                    loginRequestBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());

                    loginRequestBean.setLocation_coordinate("28.6771787,77.4923927");
                    loginRequestBean.setLogout_time("2021-04-13 16:33:23");


                    authViewModel.makeLogin(loginRequestBean, getContext());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String loginApiStatus = authViewModel.loginApiResult();
                            AppUtils.getInstance().showLog("loginApiStatus" + loginApiStatus, AuthFragment.class);
                            progressDialog.dismiss();
                            if (loginApiStatus.equalsIgnoreCase("E200")) {
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginSessionKey(), "logedin", getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginId(), userId, getContext());
                                intentToMpin();
                         /*   Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                            } else {
                                try {

                                    showServerError(loginApiStatus);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 10000);
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
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_id_or_pass)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E23":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_login_time)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;


            default:
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.SERVER_ERROR_TITLE)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
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

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Build.getSerial();

                    AppUtils.getInstance().showLog("BUILD SERIAL "+ Build.getSerial(), AppDeviceInfoUtils.class);

                }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);
                    AppUtils.getInstance().showLog("BUILD SERIAL-imeiNo1 "+ imeiNo1,AppDeviceInfoUtils.class);

                }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    imeiNo1 ="dummy_123456789";
                    AppUtils.getInstance().showLog("BUILD SERIAL-dummy "+ imeiNo1,AppDeviceInfoUtils.class);
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection in imeiiiiii: "+e,AppDeviceInfoUtils.class);
        }
        //appSharedPreferences.setImeiNumber(imeiNo1);
        AppUtils.getInstance().showLog("imeiiiiii: "+imeiNo1,AppDeviceInfoUtils.class);
        return imeiNo1;
    }


    private int getSIMSlotCount(Context context) {
        int getPhoneCount = 0;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPhoneCount = telephonyManager.getPhoneCount();
            }
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        AppUtils.getInstance().showLog("getSimSlotCount: "+getPhoneCount,AppDeviceInfoUtils.class);
        return getPhoneCount;
    }

}

package com.nrlm.lakhpatikisaan.view.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppDateFactory;
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
            if (userId.equalsIgnoreCase("")){
                binding.etUserId.setError(getString(R.string.invalid_userid));
            }else if(password.equalsIgnoreCase("")) {
                binding.etPassword.setError(getString(R.string.invalid_password));
            }else {

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                String imeiNo=AppUtils.getInstance().getIMEINo1(getContext());
                if (!imeiNo.equalsIgnoreCase(""))
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefImeiNo(),imeiNo,getContext());
                if (NetworkFactory.isInternetOn(getContext())){

                    final LoginRequestBean loginRequestBean = new LoginRequestBean();
                    loginRequestBean.setLogin_id(userId);
                    loginRequestBean.setPassword(AppUtils.getInstance().getSha256(password));

                    loginRequestBean.setAndroid_api_version(AppUtils.getInstance().getAndroidApiVersion());
                    loginRequestBean.setAndroid_version("10");
                    loginRequestBean.setApp_login_time("2021-04-13 16:33:23");
                    loginRequestBean.setApp_versions(BuildConfig.VERSION_NAME);
                    loginRequestBean.setDate(AppDateFactory.getInstance().getTodayDate());
                    loginRequestBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());
                    loginRequestBean.setImei_no(imeiNo);
                    loginRequestBean.setLocation_coordinate("28.6771787,77.4923927");
                    loginRequestBean.setLogout_time("2021-04-13 16:33:23");

                    authViewModel.makeLogin(loginRequestBean,getContext());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String loginApiStatus=authViewModel.loginApiResult();
                            AppUtils.getInstance().showLog("loginApiStatus"+loginApiStatus,AuthFragment.class);
                            progressDialog.dismiss();
                            if (loginApiStatus.equalsIgnoreCase("E200")){
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginSessionKey(),"logedin",getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginId(),userId,getContext());
                                intentToMpin();
                         /*   Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                            }else {
                                try {

                                    showServerError(loginApiStatus);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },10000);
                }else {
                    try {
                        progressDialog.dismiss();
                        showServerError(AppConstant.noInternetCode);
                    } catch (JSONException e) {
                        AppUtils.getInstance().showLog("NoInternetElse"+e.getMessage(),AuthFragment.class);
                    }
                }


            }

        });
    }
    public  void showServerError(String error_code) throws JSONException {
        switch (error_code) {

            case "E202":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_fields)
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



            default:
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.SERVER_ERROR_TITLE)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
        }
    }
    private void intentToMpin() {
        Intent intent=new Intent(getContext(), MpinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

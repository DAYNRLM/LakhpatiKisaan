package com.nrlm.lakhpatikisaan.view.auth;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentForgetpasswordBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.repository.LoginRepo;
import com.nrlm.lakhpatikisaan.utils.AppDeviceInfoUtils;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

import org.json.JSONException;

public class ForgetPasswordFragment extends BaseFragment<AuthViewModel, FragmentForgetpasswordBinding> {
    private String userId;
    private TelephonyManager telephonyManager;

    private String password;
    private String confirmPassword;
    private String etrOpt;
    LinearLayout linearLayoutFurtherView;
    private AuthViewModel authViewModel;
    ProgressDialog progressDialog;
    private LoginRepo loginRepo;

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentForgetpasswordBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentForgetpasswordBinding.inflate(inflater, container, false);
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
        linearLayoutFurtherView=(LinearLayout) view.findViewById(R.id.llfurther_view);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.verifyOtpbtn.setOnClickListener(view1 -> {
            etrOpt=binding.etOtp.getText().toString();
            checkingOtp(etrOpt);
        /*    userId=binding.etUserId.getText().toString();
            password=binding.etPassword.getText().toString();
            confirmPassword=binding.etConfirmPassword.getText().toString();*/

                });
        binding.btnUpdatePw.setOnClickListener(view1 -> {

                    userId=binding.etUserId.getText().toString().toUpperCase().trim();
            password=binding.etPassword.getText().toString();
            confirmPassword=binding.etConfirmPassword.getText().toString();

        //  String prefLoginId= ;
            if (!userId.equalsIgnoreCase("")) {
                String device= AppUtils.getInstance().getDeviceInfo();
                if (password.equalsIgnoreCase(confirmPassword) && !password.equalsIgnoreCase("") && !confirmPassword.equalsIgnoreCase("")) {
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefFrgtPass(),confirmPassword,getCurrentContext());
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage(getString(R.string.loading_heavy));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    authViewModel.ResetPasswordRequestData(getContext(),userId,getIMEINo1(getContext()),device);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if (authViewModel.resetPasswordApiStatuss.equalsIgnoreCase("E200")) {
                       /* DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert),  authViewModel.simpleResponseBean.getError().getMessage()
                                , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
                        );*/

                                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), R.drawable.ic_launcher_background, getString(R.string.alert), authViewModel.simpleResponseBean.getError().getMessage(), getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent refresh = new Intent(getCurrentContext(), AuthActivity.class);
                                        getCurrentContext().startActivity(refresh);

                                    }
                                }, false);


                            } else {
                                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), getContext().getResources().getString(R.string.oops_smthng_wrong)
                                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false);


                            }
                        }
                    }, 2000);

                }
                else {
                    DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert),  getString(R.string.invalid_password)
                            , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
                    );

                }
              /*      }else {
                        //dialog
                    }
                }else {9718724676
                    //dialog
                }*/


            }else {
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert),  getString(R.string.invalid_userid)
                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
                );
            }
        });

    }



    private void checkingOtp(String etrOpt) {


        String genratedOtp = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getRandomOtp(), getCurrentContext());
     //   Toast.makeText(getCurrentContext(), genratedOtp, Toast.LENGTH_SHORT).show();
        if (!genratedOtp.equalsIgnoreCase(etrOpt)) {
            DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), getContext().getResources().getString(R.string.invalid_otp_msg)
                    , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
            );
            return;
        } else {

            linearLayoutFurtherView.setVisibility(LinearLayout.VISIBLE);
            binding.verifyOtpbtn.setVisibility(View.GONE);
        }
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
}

package com.nrlm.lakhpatikisaan.view.mpin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentVerifyMpinBinding;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

public class VerifyMpinFragment extends BaseFragment<MpinViewModel, FragmentVerifyMpinBinding> {

    String apiStatus = "";

    @Override
    public Class<MpinViewModel> getViewModel() {
        return MpinViewModel.class;
    }

    @Override
    public FragmentVerifyMpinBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentVerifyMpinBinding.inflate(inflater, container, false);
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
        binding.btnVerify.setOnClickListener(viewBtn -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.setCancelable(false);
            progressDialog.show();
            String vrfMpin = binding.pinviewGetMpin.getText().toString();
            if (vrfMpin.isEmpty() ) {
                ViewUtilsKt.toast(getCurrentContext(), getCurrentContext().getResources().getString(R.string.mpin_can_not_empty));
            } else {
                if (vrfMpin.equalsIgnoreCase(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyMpin(), getCurrentContext()))) {
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyLoginDone(), "ok", getCurrentContext());

                    if (NetworkFactory.isInternetOn(getCurrentContext())) {
                        MpinViewModel mpinViewModel = new ViewModelProvider(getActivity()).get(MpinViewModel.class);
                        mpinViewModel.initializeObjects(getCurrentContext());

                        String loginId=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(),getCurrentContext());
                        String stateShortName=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(),getCurrentContext());
                        String imei=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(),getCurrentContext());

                        mpinViewModel.makeCheckDeleteShgRequest(getCurrentContext(),new LogRequestBean(loginId,stateShortName,imei,
                                AppUtils.getInstance().getDeviceInfo(),"00.00"));


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                apiStatus = mpinViewModel.getApiStatus();
                                status(apiStatus);
                                if (apiStatus !=null && apiStatus.equalsIgnoreCase("E200")) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getContext(), AuthActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                    Intent intentToHomeActy = new Intent(getCurrentContext(), HomeActivity.class);
                                    intentToHomeActy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentToHomeActy);
                                }
                            }
                        }, 10000);
                    } else {
                        progressDialog.dismiss();
                        Intent intentToHomeActy = new Intent(getCurrentContext(), HomeActivity.class);
                        intentToHomeActy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentToHomeActy);
                    }
                } else {
                    progressDialog.dismiss();
                    DialogFactory.getInstance().showAlert(getCurrentContext(), getCurrentContext().getResources().getString(R.string.wrong_mpin), "Ok");
                }
            }
        });
    }

    private void status(String apiStatus) {
        if (apiStatus != null && apiStatus.equalsIgnoreCase("E200")) {
            Toast.makeText(getContext(), "Shg data deletion and synchronization successful.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Shg data deletion and synchronization failed.", Toast.LENGTH_SHORT).show();
        }
    }
}


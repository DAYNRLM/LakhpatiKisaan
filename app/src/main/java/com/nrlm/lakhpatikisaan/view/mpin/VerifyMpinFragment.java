package com.nrlm.lakhpatikisaan.view.mpin;

import android.annotation.SuppressLint;
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
import com.nrlm.lakhpatikisaan.database.AppDatabase;
import com.nrlm.lakhpatikisaan.database.dao.CheckDeleteShgDao;
import com.nrlm.lakhpatikisaan.databinding.FragmentVerifyMpinBinding;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VerifyMpinFragment extends BaseFragment<MpinViewModel, FragmentVerifyMpinBinding> {
int counter;
    String apiStatus = "";
    String vrfMpin;
    ProgressDialog progressDialog;
    CheckDeleteShgDao checkDeleteShgDao;

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
       counter = PreferenceFactory.getInstance().getSharedPreferenceIntegerData(PreferenceKeyManager.getPrefKeyMpinCounter(),getCurrentContext());

            binding.btnVerify.setOnClickListener(viewBtn -> {
               // new HomeViewModel().checkDuplicateAtServer(getCurrentContext(),PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(), getCurrentContext()), PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(), getCurrentContext()),PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(), getCurrentContext()), PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefDeviceinfo(), getContext()),".2719545,.3145555", AppConstant.entryCompleted);
                AppDatabase appDatabase = AppDatabase.getDatabase(getCurrentContext());
                checkDeleteShgDao = appDatabase.getCheckDeleteShgDao();

                AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        checkDeleteShgDao.deleteAll();
                    }
                });

                progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.setCancelable(false);
            progressDialog.show();
           // String vrfMpin = binding.pinviewGetMpin.getText().toString();
          /*  if (vrfMpin.isEmpty() ) {
                ViewUtilsKt.toast(getCurrentContext(), getCurrentContext().getResources().getString(R.string.mpin_can_not_empty));
            } else {
                getApiCall();
            }*/
            String getMpin = binding.pinviewGetMpin.getText().toString();

            if (isTimeValidForLogin()) {
                binding.tvMpieError.setVisibility(View.GONE);
            }

            if (counter < 1) {
                if (PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyCountdownTime(),getCurrentContext()).equalsIgnoreCase("")) {
                    checkTime();
                    binding.tvMpieError.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    binding.tvMpieError.setText("Wrong PIN limit reached. Try after 15 minutes.");
                    ViewUtilsKt.toast(getCurrentContext(), "not allowed");

                } else {
                    binding.tvMpieError.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    binding.tvMpieError.setText("Wrong PIN limit reached. Try after 15 minutes.");
                    ViewUtilsKt.toast(getCurrentContext(), "not allowed");

                }

            } else {
                if (getMpin.isEmpty()) {
                    progressDialog.dismiss();
                    ViewUtilsKt.toast(getCurrentContext(), "Mpin can't be empty.");

                } else {
                    if (getMpin.equalsIgnoreCase(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyMpin(),getCurrentContext()))) {
                //  String mst=  PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyMstData(),getCurrentContext());


                         getApiCall();


                    } else {
                        progressDialog.dismiss();
                        ViewUtilsKt.toast(getCurrentContext(), "Mpin is Wrong");
                        binding.pinviewGetMpin.setText("");
                        binding.tvMpieError.setVisibility(View.VISIBLE);
                        binding.tvMpieError.setText("Wrong PIN " + counter + " attempt remaining.");
                        counter = counter - 1;
                        PreferenceFactory.getInstance().saveSharedPreferenceData(PreferenceKeyManager.getPrefKeyMpinCounter(),counter,getCurrentContext());
                        ViewUtilsKt.toast(getCurrentContext(), "" + counter);
                    }
                }
            }

        });
    }
    private void checkTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        AppUtils.getInstance().showLog("diff time   ::" + df.format(calendar.getTime()), VerifyMpinFragment.class);
        // Add 10 minutes to current date
        calendar.add(Calendar.MINUTE, 1);
        AppUtils.getInstance().showLog("time after 10  min.  ::" + df.format(calendar.getTime()), VerifyMpinFragment.class);

        Objects.requireNonNull(PreferenceFactory.getInstance()).saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyCountdownTime(),""+df.format(calendar.getTime()),getCurrentContext());

        df.format(calendar.getTime());

    }
    public boolean isTimeValidForLogin() {
        boolean status = false;
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (Objects.requireNonNull(PreferenceFactory.getInstance()).getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyCountdownTime(),getCurrentContext()).equalsIgnoreCase("")) {
            status = false;
        } else {
            Date savedDateTime = getDateFormate(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyCountdownTime(),getCurrentContext()));
            Date currentDateTime = getDateFormate(df.format(calendar.getTime()));
            AppUtils.getInstance().showLog("savedDateTime   ::" + savedDateTime, VerifyMpinFragment.class);
            AppUtils.getInstance().showLog("currentDateTime   ::" + currentDateTime, VerifyMpinFragment.class);
            if (currentDateTime.compareTo(savedDateTime) >= 0) {
                status = true;
                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyCountdownTime(),"",getCurrentContext());
                PreferenceFactory.getInstance().saveSharedPreferenceData(PreferenceKeyManager.getPrefKeyMpinCounter(),3,getCurrentContext());
            }
        }
        return status;
    }

    public Date getDateFormate(String date) {
        Date convertedDate = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            convertedDate = sdf.parse(date);
            sdf.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    private void getApiCall() {

            Objects.requireNonNull(PreferenceFactory.getInstance()).saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyLoginDone(), "ok", getCurrentContext());

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
                }, 8000);
            } else {
                progressDialog.dismiss();
                Intent intentToHomeActy = new Intent(getCurrentContext(), HomeActivity.class);
                intentToHomeActy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToHomeActy);
            }

    }


 /*   public void deleteShgD() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                checkDeleteShgDao.deleteAll();
            }
        });
    }
*/
    private void status(String apiStatus) {
        if (apiStatus != null && apiStatus.equalsIgnoreCase("E200")) {
            Toast.makeText(getContext(), "Data synchronized successfully.", Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(getContext(), "Shg data deletion and synchronization failed.", Toast.LENGTH_SHORT).show();
        }
    }

}



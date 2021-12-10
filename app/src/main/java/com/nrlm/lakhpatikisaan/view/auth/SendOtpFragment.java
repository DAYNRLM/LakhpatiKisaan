package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class SendOtpFragment extends BaseFragment<AuthViewModel,FragmentOtpSendBinding>  {
    private AuthViewModel authViewModel;
    String mobileNumber;

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentOtpSendBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentOtpSendBinding.inflate(inflater, container, false);
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



        binding.btnSendOtp.setOnClickListener(v -> {
            mobileNumber=binding.etMobileNumber.getText().toString();
            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getForgotMobileNumber(),mobileNumber,getCurrentContext());

            if(mobileNumber.equalsIgnoreCase("") || mobileNumber.equals(null))
           {
               DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), getString(R.string.number_cant_be_empty)
                       , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
               );
           }else {
               authViewModel.makeOtpRequest(getCurrentContext());
               NavDirections action = SendOtpFragmentDirections.actionSendOtpFragmentToForgetPasswordFragment();
               navController.navigate(action);
           }




        });
    }
}

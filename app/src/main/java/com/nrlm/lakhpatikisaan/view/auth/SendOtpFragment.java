package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class SendOtpFragment extends BaseFragment<AuthViewModel,FragmentOtpSendBinding>  {
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

        String mobileNumber=binding.etMobileNumber.getText().toString();


        binding.btnSendOtp.setOnClickListener(v -> {
            NavDirections action = SendOtpFragmentDirections.actionSendOtpFragmentToForgetPasswordFragment();
            navController.navigate(action);
        });
    }
}

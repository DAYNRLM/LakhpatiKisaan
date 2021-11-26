package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

public class AuthFragment extends BaseFragment<AuthViewModel, FragmentAuthLoginBinding> {

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
        binding.tvForgetPassword.setOnClickListener(v -> {
            NavDirections action = AuthFragmentDirections.actionAuthFragmentToSendOtpFragment();
            navController.navigate(action);
        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.nrlm.lakhpatikisaan.databinding.FragmentForgetpasswordBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class ForgetPasswordFragment extends BaseFragment<AuthViewModel, FragmentForgetpasswordBinding> {
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
}

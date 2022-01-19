package com.nrlm.lakhpatikisaan.view.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentSignUpBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;


public class SignUpFragment extends BaseFragment<AuthViewModel, FragmentSignUpBinding> {

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentSignUpBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSignUpBinding.inflate(inflater,container,false );
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onFragmentReady() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }
}
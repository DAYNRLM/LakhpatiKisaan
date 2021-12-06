package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nrlm.lakhpatikisaan.adaptor.ShgMemberAdapter;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentShgMemberBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ShgMemberFragment  extends BaseFragment<HomeViewModel, FragmentShgMemberBinding> {
    ShgMemberAdapter shgMemberAdapter;
    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentShgMemberBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentShgMemberBinding.inflate(inflater, container, false);
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

        List<String> str =  new ArrayList<>();


        shgMemberAdapter  =  new ShgMemberAdapter(str,getCurrentContext(),navController);
        binding.rvShgMembers.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
        binding.rvShgMembers.setAdapter(shgMemberAdapter);
        shgMemberAdapter.notifyDataSetChanged();

    }
}

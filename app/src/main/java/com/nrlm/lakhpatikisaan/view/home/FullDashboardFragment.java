package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentFullDashboardBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class FullDashboardFragment extends BaseFragment<HomeViewModel, FragmentFullDashboardBinding> {


    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentFullDashboardBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentFullDashboardBinding.inflate(inflater,container,false);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getHomeViewModelRepos(getCurrentContext());

        binding.shgNumberTextview.setText(viewModel.getShgCount());
        binding.memberNumberTextview.setText(viewModel.getMemberCount());
        int syncPending= Integer.parseInt( viewModel.getBeforeLocally()) +Integer.parseInt(viewModel.getAfterLocally());
        int syncComplted= Integer.parseInt( viewModel.getAfterDataFromServer()) +Integer.parseInt(viewModel.getBeforeServerData());

        binding.syncBLocallyNumberTextview.setText(syncComplted);
        binding.syncALocallyNumberTextview.setText(syncPending);
        binding.syncBOnlineNumberTextview.setText(viewModel.getBeforeServerData());
        binding.syncAOnlineNumberTextview.setText(viewModel.getAfterDataFromServer());
        int totalBeforeMemberLeft= Integer.parseInt( viewModel.getMemberCount()) -Integer.parseInt(viewModel.getBeforeServerData());
        binding.totalBMemLeftTextview.setText(""+totalBeforeMemberLeft);

        int totalAfterMemberLeft= Integer.parseInt( viewModel.getMemberCount()) -Integer.parseInt(viewModel.getAfterDataFromServer());
        binding.totalAMemLeftTextview.setText(""+totalAfterMemberLeft);
    }

    @Override
    public void onFragmentReady() {

    }
}
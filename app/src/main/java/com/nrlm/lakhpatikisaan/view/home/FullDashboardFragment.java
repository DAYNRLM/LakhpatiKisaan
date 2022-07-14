package com.nrlm.lakhpatikisaan.view.home;



import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.util.Log;
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
        binding.SyncedServer.setText(viewModel.getBeforeServerData());
        binding.syncLocally.setText(viewModel.getBeforeLocally());
        int surveyShgAllCompleted = Integer.parseInt(viewModel.getShgCount()) -Integer.parseInt(viewModel.getshgWhoseAtleastOneMemberLeft());
        String s = String.valueOf(surveyShgAllCompleted);
        binding.shgWhoseAllMembercompleted.setText(s);
        binding.shgWhoseOneMembercompleted.setText(viewModel.getshgWhoseAtleastOneMemberLeft());
        binding.surveyCompleted.setText(viewModel.getSurveyCompleted());
        binding.surveyPending.setText(viewModel.getSUrveyPending());




    }

    @Override
    public void onFragmentReady() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
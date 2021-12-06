package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nrlm.lakhpatikisaan.adaptor.ShgMemberAdapter;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentShgMemberBinding;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        viewModel.getHomeViewModelRepos(getContext());
        try {
            List<MemberListDataBean> memberListMasterData= viewModel.memberListMasterData("322249");
            AppUtils.getInstance().showLog("memberListMasterData"+memberListMasterData.size(),ShgMemberFragment.class);
            shgMemberAdapter  =  new ShgMemberAdapter(memberListMasterData,getCurrentContext(),navController);
            binding.rvShgMembers.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
            binding.rvShgMembers.setAdapter(shgMemberAdapter);
            shgMemberAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> str =  new ArrayList<>();




    }
}

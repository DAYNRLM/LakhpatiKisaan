package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;

public class DashBoardFragment extends BaseFragment<HomeViewModel, FragmentDashboardBinding> {
    ArrayAdapter<String> locationFromAdapter;
    ArrayAdapter<String> blockAdapter;
    ArrayAdapter<String> gpAdapter;
    ArrayAdapter<String> villageAdapter;

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentDashboardBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDashboardBinding.inflate(inflater, container, false);
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

        binding.test.animate().alpha(1f).setDuration(7000).start();
      //  HomeViewModel authViewModel=   new ViewModelProvider(this).get(HomeViewModel.class);


        locationFromAdapter =new ArrayAdapter<String>(getContext(), R.layout.spinner_text,viewModel.getLocationFrom());
        binding.spinnerSelectLocationFrom.setAdapter(locationFromAdapter);
        locationFromAdapter.notifyDataSetChanged();


        binding.spinnerSelectLocationFrom.setOnItemClickListener((adapterView, view1, i, l) -> {
            if(viewModel.getLocationFrom().get(i).equalsIgnoreCase("Geography")){
                binding.layoutGeoLocation.setVisibility(View.VISIBLE);
                binding.layoutCboLocation.setVisibility(View.GONE);
                loadBlockData();


            }else {
                binding.layoutCboLocation.setVisibility(View.VISIBLE);
                binding.layoutGeoLocation.setVisibility(View.GONE);
            }
        });




        binding.btnGoToMember.setOnClickListener(view1 -> {
            NavDirections navDirections = DashBoardFragmentDirections.actionDashBoardFragmentToShgMemberFragment();
            navController.navigate(navDirections);
        });
    }

    private void loadBlockData() {
        blockAdapter =new ArrayAdapter<String>(getContext(), R.layout.spinner_text,viewModel.loadBlockName());
        binding.spinnerSelectBlock.setAdapter(blockAdapter);
        blockAdapter.notifyDataSetChanged();

        binding.spinnerSelectBlock.setOnItemClickListener((adapterView, view, i, l) -> {
            String str = viewModel.getAllBlockData().get(i).getBlock_code();
        });

    }
}

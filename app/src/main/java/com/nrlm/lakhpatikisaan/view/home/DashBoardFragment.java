package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.adaptor.GpDataAdaptor;
import com.nrlm.lakhpatikisaan.adaptor.ShgAdaptor;
import com.nrlm.lakhpatikisaan.adaptor.VillageAdaptor;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.ShgDataBean;
import com.nrlm.lakhpatikisaan.database.dbbean.VillageDataBean;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DashBoardFragment extends BaseFragment<HomeViewModel, FragmentDashboardBinding> {
    ArrayAdapter<String> locationFromAdapter;
    ArrayAdapter<String> blockAdapter;
    ArrayAdapter<String> gpAdapter;
    ArrayAdapter<String> villageAdapter;
    ArrayAdapter<String> shgAdaptor;

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
        viewModel.getHomeViewModelRepos(getCurrentContext());
        binding.test.animate().alpha(1f).setDuration(7000).start();
        //  HomeViewModel authViewModel=   new ViewModelProvider(this).get(HomeViewModel.class);


        locationFromAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getLocationFrom());
        binding.spinnerSelectLocationFrom.setAdapter(locationFromAdapter);
        locationFromAdapter.notifyDataSetChanged();


        binding.spinnerSelectLocationFrom.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (viewModel.getLocationFrom().get(i).equalsIgnoreCase("Geography")) {
                binding.layoutGeoLocation.setVisibility(View.VISIBLE);
                binding.layoutCboLocation.setVisibility(View.GONE);
                loadBlockData();


            } else {
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
        blockAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadBlockName());
        binding.spinnerSelectBlock.setAdapter(blockAdapter);
        blockAdapter.notifyDataSetChanged();

        binding.spinnerSelectBlock.setOnItemClickListener((adapterView, view, i, l) -> {

             /*   List<GpDataBean> gpDataBeanList = viewModel.getGpListData(blockCode);
                GpDataAdaptor gpDataAdaptor = new GpDataAdaptor(getCurrentContext(), gpDataBeanList);*/
            String blockCode = viewModel.getAllBlockData().get(i).getBlockCode();
            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedBlockCode(), blockCode, getContext());

            loadGpData(blockCode);

        });

    }

    private void loadGpData(String blockCode) {
        try {
            List<GpDataBean> gpDataBeanList = viewModel.getGpListData(blockCode);
            gpAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getGpListName(blockCode));
            binding.spinnerSelectGp.setAdapter(gpAdapter);
            gpAdapter.notifyDataSetChanged();
            binding.spinnerSelectGp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // binding.spinnerSelectGp.setText(((GpDataBean) parent.getItemAtPosition(position)).getGpName());

                    String gpCode = gpDataBeanList.get(position).getGpCode();
                    AppUtils.getInstance().showLog("gpCode" + gpCode, DashBoardFragment.class);
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedGpCode(), gpCode, getContext());
                    loadVillageData(gpCode);
                          /*  List<VillageDataBean> villageDataBeanList = viewModel.getVillageListData(gpCode);
                            VillageAdaptor villageAdaptor = new VillageAdaptor(getCurrentContext(), villageDataBeanList);*/

                }
            });

        } catch (ExecutionException e) {
            e.printStackTrace();
            AppUtils.getInstance().showLog("gpCodeExpOuter" + e, DashBoardFragment.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
            AppUtils.getInstance().showLog("gpCodeExpOuter" + e, DashBoardFragment.class);
        }

    }

    private void loadVillageData(String gpCode) {
        try {
            List<VillageDataBean> villageDataBeanList = viewModel.getVillageListData(gpCode);
            villageAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getVillageListName(gpCode));
            binding.spinnerSelectVillage.setAdapter(villageAdapter);
            villageAdapter.notifyDataSetChanged();

            binding.spinnerSelectVillage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String villageCode = villageDataBeanList.get(position).getVillageCode();
                    AppUtils.getInstance().showLog("villageCode" + gpCode, DashBoardFragment.class);
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedVillageCode(), villageCode, getContext());
                    loadShgData(villageCode);

                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loadShgData(String villageCode) {
        try {
            List<ShgDataBean> shgDataBeanList = viewModel.getShgListData(villageCode);
            shgAdaptor = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getShgListName(villageCode));
            binding.spinnerSelectShg.setAdapter(shgAdaptor);
            shgAdaptor.notifyDataSetChanged();

          binding.spinnerSelectShg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  String shgCode = shgDataBeanList.get(position).getShgCode();
                  String memberCount=null;
                  String beforeEntryMemberCount=null;
                  String afterEntryMemberCount=null;
                  try {
                      memberCount = viewModel.getMemberCount(shgCode);
                      beforeEntryMemberCount= viewModel.getBeforeEntryMemberCount(shgCode);
                      afterEntryMemberCount=viewModel.getAfterEntryMemberCount(shgCode);
                      binding.tvMemberCount.setText(memberCount);
                      binding.completedBeforeEntryCount.setText(beforeEntryMemberCount);
                      binding.completedAfterEntryCount.setText(afterEntryMemberCount);
                  } catch (ExecutionException e) {
                      e.printStackTrace();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }

                  AppUtils.getInstance().showLog("shgCodeee" + shgCode, DashBoardFragment.class);
                  PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedShgCode(), shgCode, getContext());
              }
          });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

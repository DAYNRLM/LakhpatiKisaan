package com.nrlm.lakhpatikisaan.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.adaptor.EntryBeforeNrlmFoldAdapter;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryAfterNrlmBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryBinding;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class IncomeEntryAfterNrlmFragment extends BaseFragment<HomeViewModel, FragmentMemberEntryAfterNrlmBinding> {
    EntryBeforeNrlmFoldAdapter entryBeforeNrlmFoldAdapter;
    List<MemberEntryEntity> memberEntryDataItem;

    ArrayAdapter<String> sectorAdapter;
    ArrayAdapter<String> activityAdapter;
    ArrayAdapter<String> frequencyAdapter;
    ArrayAdapter<String> incomeAdapter;
    private String loginApiStatus="E2";

    String shgCode;
    String shgMemberCode;
    String entryYearCode;
    String entryMonthCode;
    String entryCreatedDate;
    String sectorDate;
    String activityCode;
    String incomeFrequencyCode;
    String incomeRangCode;
    String flagBeforeAfterNrlm;
    String flagSyncStatus;

    String sectorName;
    String activityName;
    String incomeFrequencyName;
    String incomeRangName;
    String monthName;

    int showingYear;

    int count = 0;

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentMemberEntryAfterNrlmBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMemberEntryAfterNrlmBinding.inflate(inflater, container, false);
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

        Calendar today = Calendar.getInstance();
        viewModel.getHomeViewModelRepos(getCurrentContext());


        try {
            shgMemberCode = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefSelectedMemberCode(), getContext());
            shgCode = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefSelectedShgCode(), getContext());
            String memberName = viewModel.getMemberNameDB(shgMemberCode);
            String shgName = viewModel.getShgNameDB(shgCode);
            String joiningDate = viewModel.getMemberJoiningDate(shgMemberCode);
            String memberBelongingName = viewModel.getMemberBelongingName(shgMemberCode);

            String memberDOJ = viewModel.getMemberDOJ(shgMemberCode);

            showingYear = appDateFactory.getMemberClanderYear(memberDOJ, AppConstant.nrlm_formation_date);

            binding.tvMonth.setText(monthName);
            binding.tvYear.setText("" + entryYearCode);

            binding.tvMemberNameCode.setTextColor(getCurrentContext().getResources().getColor(R.color.orange_700));
            binding.tvShgNameCode.setText("Member : " + memberName + " (" + shgMemberCode + ")");
            binding.tvMemberNameCode.setText("SHG : " + shgName + " (" + shgCode + ")");
            binding.joiningDates.setText("Member's joining date : " + joiningDate);
            binding.tvMemberBelonging.setText("Father/Husband Name :"+memberBelongingName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        memberEntryDataItem = viewModel.getAllEntryData(shgMemberCode, AppConstant.afterNrlmStatus);
        if (!memberEntryDataItem.isEmpty()) {

            count = memberEntryDataItem.size();

            entryBeforeNrlmFoldAdapter = new EntryBeforeNrlmFoldAdapter(memberEntryDataItem, getCurrentContext(), viewModel);
            binding.rvEntryRecyclerview.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
            binding.rvEntryRecyclerview.setAdapter(entryBeforeNrlmFoldAdapter);
            entryBeforeNrlmFoldAdapter.notifyDataSetChanged();
            binding.cvRecyclerview.setVisibility(View.VISIBLE);
            binding.cvSelectActivity.setVisibility(View.GONE);
            binding.btnAddNewActivity.setText(getCurrentContext().getResources().getString(R.string.add_activity_msg));
            binding.tvTotalActivityCount.setVisibility(View.GONE);
            binding.tvTotalActivityCount.setText(getCurrentContext().getResources().getString(R.string.total_activity) + count);

            binding.tvMonth.setText(memberEntryDataItem.get(0).getMonthName());
            binding.tvYear.setText("" + memberEntryDataItem.get(0).getEntryYearCode());

            binding.llSelectDate.setVisibility(View.GONE);
            binding.llStartActivity.setVisibility(View.VISIBLE);
            binding.ccDisplayDate.setVisibility(View.VISIBLE);

            entryYearCode = String.valueOf(memberEntryDataItem.get(0).getEntryYearCode());
            entryMonthCode = String.valueOf(memberEntryDataItem.get(0).getMonthName());



            resetFunction(1);

        } else {

        }


        binding.btnMonthYear.setOnClickListener(view1 -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getCurrentContext(), new MonthPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int selectedMonth, int selectedYear) {

                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");

                    today.set(Calendar.MONTH, selectedMonth);


                    String month_name = String.valueOf(selectedMonth + 1);


                    binding.tvMonth.setText(month_name);
                    binding.tvYear.setText("" + selectedYear);

                    binding.llSelectDate.setVisibility(View.GONE);
                    binding.llStartActivity.setVisibility(View.VISIBLE);
                    binding.ccDisplayDate.setVisibility(View.VISIBLE);

                    entryYearCode = String.valueOf(selectedYear);
                    entryMonthCode = String.valueOf(selectedMonth + 1);
                       PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPREF_KEY_month(),entryMonthCode , getContext());



                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

            //.setMinMonth(Calendar.FEBRUARY)
            builder.setActivatedMonth(today.get(Calendar.MONTH))
                    .setActivatedYear(today.get(Calendar.YEAR))
                    .setMinYear(today.get(Calendar.YEAR))
                    .setMaxYear(today.get(Calendar.YEAR))
                    .setTitle("Select Month")
                    .setMonthRange(Calendar.JANUARY, today.get(Calendar.MONTH)).build().show();
        });

        binding.btnAddNewActivity.setOnClickListener(view1 -> {
            binding.cvSelectActivity.setVisibility(View.VISIBLE);
           // loadSector();

            loadAllActivity(shgMemberCode);
        });

        binding.btnAddActivityDetail.setOnClickListener(view1 -> {
            if (sectorDate == null || sectorDate.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.sector_not_fill));

            } else if (activityCode == null || activityCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.activity_not_fill));
            } else if (incomeFrequencyCode == null || incomeFrequencyCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.frequency_not_fill));
            } else if (incomeRangCode == null || incomeRangCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.range_not_fill));
            } else {
                loadEntryList();

                memberEntryDataItem = viewModel.getAllEntryData(shgMemberCode, AppConstant.afterNrlmStatus);

                count = memberEntryDataItem.size();

                entryBeforeNrlmFoldAdapter = new EntryBeforeNrlmFoldAdapter(memberEntryDataItem, getCurrentContext(), viewModel);
                binding.rvEntryRecyclerview.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
                binding.rvEntryRecyclerview.setAdapter(entryBeforeNrlmFoldAdapter);
                entryBeforeNrlmFoldAdapter.notifyDataSetChanged();


                binding.cvRecyclerview.setVisibility(View.VISIBLE);
                binding.cvSelectActivity.setVisibility(View.GONE);
                binding.btnAddNewActivity.setText(getCurrentContext().getResources().getString(R.string.add_activity_msg));
                binding.tvTotalActivityCount.setVisibility(View.GONE);
                binding.tvTotalActivityCount.setText(getCurrentContext().getResources().getString(R.string.total_activity) + count);

                resetFunction(1);
            }

        });

        binding.btnSaveEntry.setOnClickListener(v -> {

            /*NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
            navController.navigate(navDirections);*/
            if (memberEntryDataItem.size() > 0) {
                new MaterialAlertDialogBuilder(getCurrentContext()).setTitle("User Confirmation").setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                        .setItems(AppConstant.ConstantObject.getConfirmation(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String arr[] = AppConstant.ConstantObject.getStatus();
                                String str = arr[i];
                                if (str.equalsIgnoreCase("1")) {
                                    /****data save in database and
                                     * sync operation perform and
                                     * redirect to afternrl screen****/
                                    dialogInterface.dismiss();


                                    /*** pending update cofirmation status for
                                     * need to make a coloum for status confirmation  in masterEntryEntity
                                     * after NRML before syn data*****/


                                    if (NetworkFactory.isInternetOn(getContext())) {

                                        ProgressDialog progressDialog = new ProgressDialog(getCurrentContext());
                                        progressDialog.setMessage("" + getCurrentContext().getResources().getString(R.string.loading_heavy));
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        viewModel.checkDuplicateAtServer(getContext()
                                                , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(), getContext())
                                                , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(), getContext())
                                                , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(), getContext())
                                                , AppUtils.getInstance().getDeviceInfo()
                                                , "0.0,0.0"
                                                , AppConstant.entryCompleted);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (viewModel.getSyncApiStatus() != null && viewModel.getSyncApiStatus().equalsIgnoreCase("E200")) {
                                                    progressDialog.dismiss();
                                                    try {
                                                        viewModel.updateAfterEntryDateInLocal(shgMemberCode, entryMonthCode + "-" + entryYearCode);
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(getContext(), "Data Synced Successfully!!!", Toast.LENGTH_LONG).show();
                                                    NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                                                    navController.navigate(navDirections);

                                                    /****this update date code comes after data sync successfully*****/


                                                } else {

                                                    Toast.makeText(getContext(), "Data Synced failed!!!", Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                    NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                                                    navController.navigate(navDirections);

                                                    /* ***this update date code comes after data sync sucessfully*****/
                                                    try {
                                                        viewModel.updateAfterEntryDateInLocal(shgMemberCode, entryMonthCode + "-" + entryYearCode);
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                        }, 6000);
                                    } else {
                                        try {
                                            viewModel.updateAfterEntryDateInLocal(shgMemberCode, entryMonthCode + "-" + entryYearCode);
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(getContext(), "Data saved successfully!!!", Toast.LENGTH_LONG).show();
                                        NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                                        navController.navigate(navDirections);
                                    }


                                } else if (str.equalsIgnoreCase("2")) {
                                    dialogInterface.dismiss();

                               /*  NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                                 navController.navigate(navDirections);*/

                                }
                            }
                        }).setCancelable(true).show();

            } else {
                Toast.makeText(getContext(), "Activities are not added", Toast.LENGTH_LONG).show();
            }


        });

        binding.spinnerSelectSector.setOnItemClickListener((adapterView, view1, i, l) -> {
            sectorDate = String.valueOf(viewModel.getAllSectorData().get(i).getSector_code());
            sectorName = viewModel.loadSectorData().get(i);
            resetFunction(2);
            //loadActivityData(viewModel.getAllSectorData().get(i).getSector_code(), shgMemberCode);

        });
        binding.btnReset.setOnClickListener(view1 -> {
          //  ViewUtilsKt.toast(getCurrentContext(), "Not working yet");
        });
    }

    private void loadAllActivity(String memberCode) {
        /****** tis selection based on condition on activity id*****/
        activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getAllSelectedActivityName(memberCode, AppConstant.afterNrlmStatus));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            try{
                activityCode = String.valueOf(viewModel.getAllSelectedActivity(memberCode, AppConstant.afterNrlmStatus).get(i).getActivity_code());
                activityName = viewModel.getAllSelectedActivityName(memberCode, AppConstant.afterNrlmStatus).get(i);

                sectorDate = String.valueOf(viewModel.getAllSelectedActivity(memberCode, AppConstant.afterNrlmStatus).get(i).getSector_code());
                sectorName = viewModel.SectorName(viewModel.getAllSelectedActivity(memberCode, AppConstant.afterNrlmStatus).get(i).getSector_code());
                resetFunction(3);
                loadFreaquency();

            }catch (Exception e){
                if (!RetrofitClient.server.equalsIgnoreCase("live"))
                    Toast.makeText(getCurrentContext(),"IndexOutOfBoundsException AtIncomeEntryAfterFragment",Toast.LENGTH_LONG).show();
                AppUtils.getInstance().showLog("IndexOutOfBoundsException",IncomeEntryAfterNrlmFragment.class);
            }

        });

    }

    private void loadEntryList() {

        MemberEntryEntity memberEntryEntity = new MemberEntryEntity();
        memberEntryEntity.shgCode = shgCode;
        memberEntryEntity.shgMemberCode = shgMemberCode;
        if (entryYearCode.equalsIgnoreCase("2022")|| entryYearCode.equalsIgnoreCase("2023")||entryYearCode.equalsIgnoreCase("2024")||entryYearCode.equalsIgnoreCase("2025")||entryYearCode.equalsIgnoreCase("2026")||entryYearCode.equalsIgnoreCase("2027")||entryYearCode.equalsIgnoreCase("2028")||entryYearCode.equalsIgnoreCase("2029")||entryYearCode.equalsIgnoreCase("2030")||entryYearCode.equalsIgnoreCase("2031")||entryYearCode.equalsIgnoreCase("2032")||entryYearCode.equalsIgnoreCase("2033")){
            memberEntryEntity.entryYearCode = entryYearCode;
            memberEntryEntity.entryMonthCode= PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPREF_KEY_month(), getContext());;

            memberEntryEntity.entryCreatedDate = appDateFactory.getTimeStamp();
            memberEntryEntity.sectorDate = sectorDate;
            memberEntryEntity.activityCode = activityCode;

            memberEntryEntity.incomeFrequencyCode = incomeFrequencyCode;
            memberEntryEntity.incomeRangCode = incomeRangCode;
            memberEntryEntity.flagBeforeAfterNrlm = AppConstant.afterNrlmStatus;
            memberEntryEntity.monthName=entryMonthCode;

            memberEntryEntity.flagSyncStatus = AppConstant.unsyncStatus;
            memberEntryEntity.sectorName = sectorName;
            memberEntryEntity.activityName = activityName;
            memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
            memberEntryEntity.incomeRangName = incomeRangName;
            memberEntryEntity.seccNumber = "0";
            memberEntryEntity.seccName = "";
            memberEntryEntity.entryCompleteConfirmation = "1";

            // memberEntryDataItem.add(memberEntryEntity);

            viewModel.insertBeforeNrlmEntryData(memberEntryEntity);

        }
        else {
            DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please change Your phone language in English"
                    , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
            );        }







    }

    private void loadSector() {
        sectorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadSectorData());
        binding.spinnerSelectSector.setAdapter(sectorAdapter);
        sectorAdapter.notifyDataSetChanged();
    }

    private void loadActivityData(int id, String memberCode) {
        /****** tis selection based on condition on activity id*****/
        activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getSelectedActivityName(id, memberCode, AppConstant.afterNrlmStatus));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            try {
                activityCode = String.valueOf(viewModel.getSelectedActivity(id, memberCode, AppConstant.afterNrlmStatus).get(i).getActivity_code());
                activityName = viewModel.getSelectedActivityName(id, memberCode, AppConstant.afterNrlmStatus).get(i);
                resetFunction(3);
                loadFreaquency();
            } catch (Exception e) {
                if (!RetrofitClient.server.equalsIgnoreCase("live"))
                    Toast.makeText(getCurrentContext(), "IndexOutOffBond Exception", Toast.LENGTH_LONG).show();
                AppUtils.getInstance().showLog("Indexoutoffbond Exp", IncomeEntryAfterNrlmFragment.class);
            }

        });
    }


    private void loadFreaquency() {

        frequencyAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadFrequencyData());
        binding.spinnerSelectFrequency.setAdapter(frequencyAdapter);
        frequencyAdapter.notifyDataSetChanged();

        binding.spinnerSelectFrequency.setOnItemClickListener((adapterView, view, i, l) -> {
            incomeFrequencyCode = String.valueOf(viewModel.getAllFrequencyData().get(i).getFrequency_id());
            incomeFrequencyName = viewModel.loadFrequencyData().get(i);
            loadIncomeData(viewModel.getAllFrequencyData().get(i).getFrequency_id());
            resetFunction(4);
        });
    }

    private void loadIncomeData(int frequency_id) {
        incomeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadIncomeData(frequency_id));
        binding.spinnerSelectIncome.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();

        binding.spinnerSelectIncome.setOnItemClickListener((adapterView, view, i, l) -> {
            incomeRangCode = String.valueOf(viewModel.getAllIncomeData(frequency_id).get(i).getRange_id());
            incomeRangName = viewModel.loadIncomeData(frequency_id).get(i);
        });

    }

    public void resetFunction(int id) {
        switch (id) {
            case 1:

                sectorAdapter = null;
                activityAdapter = null;
                frequencyAdapter = null;
                incomeAdapter = null;
                binding.spinnerSelectSector.setText("");
                binding.spinnerSelectActivity.setText("");
                binding.spinnerSelectFrequency.setText("");
                binding.spinnerSelectIncome.setText("");
                activityCode = null;
                incomeFrequencyCode = null;
                incomeRangCode = null;
                sectorDate = null;
                sectorName = null;
                activityName = null;
                incomeFrequencyName = null;
                incomeRangName = null;

                break;

            case 2:

                activityAdapter = null;
                frequencyAdapter = null;
                incomeAdapter = null;

                binding.spinnerSelectActivity.setText("");
                binding.spinnerSelectFrequency.setText("");
                binding.spinnerSelectIncome.setText("");

                activityCode = null;
                incomeFrequencyCode = null;
                incomeRangCode = null;

                activityName = null;
                incomeFrequencyName = null;
                incomeRangName = null;

                break;

            case 3:


                frequencyAdapter = null;
                incomeAdapter = null;

                binding.spinnerSelectFrequency.setText("");
                binding.spinnerSelectIncome.setText("");


                incomeFrequencyCode = null;
                incomeRangCode = null;

                incomeFrequencyName = null;
                incomeRangName = null;

                break;

            case 4:
                incomeAdapter = null;

                binding.spinnerSelectIncome.setText("");

                incomeRangCode = null;

                incomeRangName = null;

                break;

            case 5:
                binding.cvSelectActivity.setVisibility(View.GONE);

                sectorAdapter = null;
                activityAdapter = null;
                frequencyAdapter = null;
                incomeAdapter = null;

                binding.spinnerSelectSector.setText("");
                binding.spinnerSelectActivity.setText("");
                binding.spinnerSelectFrequency.setText("");
                binding.spinnerSelectIncome.setText("");

                activityCode = null;
                incomeFrequencyCode = null;
                incomeRangCode = null;
                sectorDate = null;

                sectorName = null;
                activityName = null;
                incomeFrequencyName = null;
                incomeRangName = null;

                break;

        }


    }
}

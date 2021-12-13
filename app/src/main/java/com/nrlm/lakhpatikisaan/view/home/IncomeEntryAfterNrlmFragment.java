package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
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

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.adaptor.EntryBeforeNrlmFoldAdapter;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryAfterNrlmBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryBinding;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
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

public class IncomeEntryAfterNrlmFragment extends BaseFragment<HomeViewModel, FragmentMemberEntryAfterNrlmBinding> {
    EntryBeforeNrlmFoldAdapter entryBeforeNrlmFoldAdapter;
    List<MemberEntryEntity> memberEntryDataItem;

    ArrayAdapter<String> sectorAdapter;
    ArrayAdapter<String> activityAdapter;
    ArrayAdapter<String> frequencyAdapter;
    ArrayAdapter<String> incomeAdapter;

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
        memberEntryDataItem = new ArrayList<>();
        viewModel.getHomeViewModelRepos(getCurrentContext());



        binding.btnMonthYear.setOnClickListener(view1 -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getCurrentContext(), new MonthPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int selectedMonth, int selectedYear) {

                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");

                    today.set(Calendar.MONTH, selectedMonth);

                    String month_name = month_date.format(today.getTime());


                    binding.tvMonth.setText(month_name);
                    binding.tvYear.setText("" + selectedYear);

                    binding.llSelectDate.setVisibility(View.GONE);
                    binding.llStartActivity.setVisibility(View.VISIBLE);
                    binding.ccDisplayDate.setVisibility(View.VISIBLE);

                    entryYearCode = String.valueOf(selectedYear);
                    entryMonthCode = String.valueOf(selectedMonth);
                    monthName = month_name;

                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

            //.setMinMonth(Calendar.FEBRUARY)
            builder.setActivatedMonth(today.get(Calendar.MONTH))
                    .setActivatedYear(today.get(Calendar.YEAR))
                    .setMinYear(1990)
                    .setMaxYear(today.get(Calendar.YEAR))
                    .setTitle("Select Month")
                    .setMonthRange(Calendar.JANUARY, today.get(Calendar.MONTH)).build().show();
        });

        binding.btnAddActivityDetail.setOnClickListener(view1 -> {
          if (sectorDate == null || sectorDate.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), "Select Sector first");

            } else if (activityCode == null || activityCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), "Select Activity first");
            } else if (incomeFrequencyCode == null || incomeFrequencyCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), "Select Frequency first");
            } else if (incomeRangCode == null || incomeRangCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), "Select Income Range first");
            } else {
                loadEntryList();
                count++;

                entryBeforeNrlmFoldAdapter = new EntryBeforeNrlmFoldAdapter(memberEntryDataItem, getCurrentContext());
                binding.rvEntryRecyclerview.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
                binding.rvEntryRecyclerview.setAdapter(entryBeforeNrlmFoldAdapter);
                entryBeforeNrlmFoldAdapter.notifyDataSetChanged();


                binding.cvRecyclerview.setVisibility(View.VISIBLE);
                binding.cvSelectActivity.setVisibility(View.GONE);
                binding.btnAddActivity.setText("Add Another Activity");
                binding.tvTotalActivityCount.setVisibility(View.VISIBLE);
                binding.tvTotalActivityCount.setText("Total Activities is :" + count);

                resetFunction(1);
            }

        });

        binding.btnAddActivity.setOnClickListener(view1 -> {
            if (count == 0) {
                Observer<String> actionObserver = new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        if (s.equalsIgnoreCase("ok")) {
                            binding.btnChangeMonthYear.setVisibility(View.GONE);
                            binding.cvSelectActivity.setVisibility(View.VISIBLE);
                            loadSector();
                        } else {
                            ViewUtilsKt.toast(getCurrentContext(), "Chenage Date First");

                        }

                    }
                };

                viewModel.commonAleartDialog(getCurrentContext()).observe(getViewLifecycleOwner(), actionObserver);

            } else {
                binding.cvSelectActivity.setVisibility(View.VISIBLE);
            }

        });

        binding.spinnerSelectSector.setOnItemClickListener((adapterView, view1, i, l) -> {
            sectorDate = String.valueOf(viewModel.getAllSectorData().get(i).getSector_code());
            sectorName = viewModel.loadSectorData().get(i);
            resetFunction(2);
            loadActivityData(viewModel.getAllSectorData().get(i).getSector_code());

        });

        binding.btnAddActivity.setOnClickListener(view1 -> {
            if (count == 0) {
                Observer<String> actionObserver = new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        if (s.equalsIgnoreCase("ok")) {
                            binding.btnChangeMonthYear.setVisibility(View.GONE);
                            binding.cvSelectActivity.setVisibility(View.VISIBLE);
                            loadSector();
                        } else {
                            ViewUtilsKt.toast(getCurrentContext(), "Chenage Date First");

                        }

                    }
                };

                viewModel.commonAleartDialog(getCurrentContext()).observe(getViewLifecycleOwner(), actionObserver);

            } else {
                binding.cvSelectActivity.setVisibility(View.VISIBLE);
            }
        });

        binding.btnSaveEntry.setOnClickListener(v -> {
          //  Toast.makeText(getContext(), "Data Synced Successfully!!!", Toast.LENGTH_LONG).show();
            viewModel.insertBeforeNrlmEntryData(memberEntryDataItem);

            if (NetworkFactory.isInternetOn(getContext())){
                viewModel.checkDuplicateAtServer(getContext()
                        , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(),getContext())
                        ,PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(),getContext())
                        ,PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(),getContext())
                        , AppUtils.getInstance().getDeviceInfo()
                        ,"0.0,0.0"
                        ,AppConstant.entryCompleted);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (viewModel.getSyncApiStatus().equalsIgnoreCase("E200")){
                            Toast.makeText(getContext(), "Data Synced Successfully!!!", Toast.LENGTH_LONG).show();
                            NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                            navController.navigate(navDirections);
                        }else {
                            NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                            navController.navigate(navDirections);
                        }

                    }
                },6000);
            }else {
                NavDirections navDirections = IncomeEntryAfterNrlmFragmentDirections.actionIncomeEntryAfterNrlmFragmentToShgMemberFragment();
                navController.navigate(navDirections);
            }


        });
    }

    private void loadEntryList() {

        MemberEntryEntity memberEntryEntity = new MemberEntryEntity();
        memberEntryEntity.shgCode = "";
        memberEntryEntity.shgMemberCode = "";
        memberEntryEntity.entryYearCode = entryYearCode;
        memberEntryEntity.entryMonthCode = entryMonthCode;
        memberEntryEntity.entryCreatedDate = appDateFactory.getTimeStamp();
        memberEntryEntity.sectorDate = sectorDate;
        memberEntryEntity.activityCode = activityCode;
        memberEntryEntity.incomeFrequencyCode = incomeFrequencyCode;
        memberEntryEntity.incomeRangCode = incomeRangCode;
        memberEntryEntity.flagBeforeAfterNrlm = flagBeforeAfterNrlm;
        memberEntryEntity.flagSyncStatus = AppConstant.afterNrlmStatus;

        memberEntryEntity.sectorName = sectorName;
        memberEntryEntity.activityName = activityName;
        memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
        memberEntryEntity.incomeRangName = incomeRangName;
        memberEntryEntity.monthName = monthName;

        memberEntryEntity.seccNumber = "";
        memberEntryEntity.entryCompleteConfirmation="1";

        memberEntryDataItem.add(memberEntryEntity);

    }

    private void loadSector() {
        sectorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadSectorData());
        binding.spinnerSelectSector.setAdapter(sectorAdapter);
        sectorAdapter.notifyDataSetChanged();
    }

    private void loadActivityData(int id) {
        activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadActivityData(id));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            activityCode = String.valueOf(viewModel.getAllActivityData(id).get(i).getActivity_code());
            activityName = viewModel.loadActivityData(id).get(i);
            resetFunction(3);
            loadFreaquency();
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
        });
    }

    private void loadIncomeData(int frequency_id) {
        incomeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadIncomeData(frequency_id));
        binding.spinnerSelectIncome.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();

        binding.spinnerSelectIncome.setOnItemClickListener((adapterView, view, i, l) -> {
            incomeRangCode = String.valueOf(viewModel.getAllIncomeData(frequency_id).get(i).getFrequency_id());
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

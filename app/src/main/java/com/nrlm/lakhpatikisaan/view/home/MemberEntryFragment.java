package com.nrlm.lakhpatikisaan.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.adaptor.EntryBeforeNrlmFoldAdapter;
import com.nrlm.lakhpatikisaan.adaptor.ShgMemberAdapter;
import com.nrlm.lakhpatikisaan.database.entity.AadhaarEntity;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.databinding.CustomAadharVerifyDialogBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentShgMemberBinding;
import com.nrlm.lakhpatikisaan.network.model.AadharPojo;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.AuthViewModel;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MemberEntryFragment extends BaseFragment<HomeViewModel, FragmentMemberEntryBinding> {

    EntryBeforeNrlmFoldAdapter entryBeforeNrlmFoldAdapter;
    List<MemberEntryEntity> memberEntryDataItem;
    List<String> monthYearItem;

    ArrayAdapter<String> sectorAdapter;
    ArrayAdapter<String> activityAdapter;
    ArrayAdapter<String> frequencyAdapter;
    ArrayAdapter<String> incomeAdapter;
    ArrayAdapter<String> seccAdapter;

    String shgCode;
    String shgMemberCode;
    String entryYearCode;
    String entryMonthCode;
    String entryCreatedDate;
    String sectorDate;
    String activityCode;
    String incomeFrequencyCode;
    String incomeRangCode;
    String flagBeforeAfterNrlm = "B";
    String flagSyncStatus;

    String sectorName;
    String activityName;
    String incomeFrequencyName;
    String incomeRangName;
    String monthName;
    String seccNumber="";
    String seccName;
    String aadharStatus;


    int count = 0;
    private HomeViewModel homeViewModel;

    LayoutInflater inflate;
    XmlPullParser parser;
    ProgressDialog progressDialog;

    @Override

    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override

    public FragmentMemberEntryBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        inflate =inflater;
        return FragmentMemberEntryBinding.inflate(inflater, container, false);
    }

    @Override

    public Context getCurrentContext() {
        return getContext();
    }

    @Override

    public void onFragmentReady() {

    }

    @SuppressLint("SetTextI18n")
    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Calendar today = Calendar.getInstance();
        viewModel.getHomeViewModelRepos(getCurrentContext());




        try {
            shgMemberCode = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefSelectedMemberCode(), getContext());
            shgCode = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefSelectedShgCode(), getContext());
            String memberName = viewModel.getMemberNameDB(shgMemberCode);
            String shgName = viewModel.getShgNameDB(shgCode);
            String joiningDate = viewModel.getMemberJoiningDate(shgMemberCode);
            aadharStatus = viewModel.getAadharStatusFromMaster(shgMemberCode);
            String memberBelongingName = viewModel.getMemberBelongingName(shgMemberCode);




            String memberDOJ = viewModel.getMemberDOJ(shgMemberCode);

            monthYearItem = appDateFactory.monthYear(memberDOJ, AppConstant.nrlm_formation_date);
            loadSecc(shgMemberCode);

            monthName = monthYearItem.get(0);
            entryYearCode = monthYearItem.get(1);
            entryMonthCode = monthYearItem.get(2);


            binding.tvMonth.setText(monthName);
            binding.tvYear.setText("" + entryYearCode);

            binding.tvMemberNameCode.setTextColor(getCurrentContext().getResources().getColor(R.color.orange_700));
            binding.tvShgNameCode.setText(getString(R.string.member)+memberName + " (" + shgMemberCode + ")");
            binding.tvMemberNameCode.setText("SHG : "+shgName + " (" + shgCode + ")");
            binding.joiningDate.setText(getString(R.string.memberjoiningdate) +  joiningDate );
            binding.tvMemberBelonging.setText(getString(R.string.fathername)+memberBelongingName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        /*****for aadhar layout*****/

        if(aadharStatus.equalsIgnoreCase("0")){
            binding.cvAadharCardEntry.setVisibility(View.VISIBLE);
        }else {
            binding.cvAadharCardEntry.setVisibility(View.GONE);
            binding.cvChangeMonthYear.setVisibility(View.VISIBLE);

        }


        binding.ivAadhrScanner.setOnClickListener(view1 -> {
           // IntentIntegrator integrator = new IntentIntegrator(getActivity());
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("scan QR code");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();

        });



        binding.btnSaveAadhar.setOnClickListener(view1 -> {
            if(binding.etAadharNumber.getText().toString().isEmpty()){
                ViewUtilsKt.toast(getCurrentContext(),"Enter 12 Digit aadhar number first.");
            }else if (binding.etAadharName.getText().toString().isEmpty()){
                ViewUtilsKt.toast(getCurrentContext(),"Enter Name as per aadhar.");
            }else {

                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                        new MaterialAlertDialogBuilder(getCurrentContext());

                CustomAadharVerifyDialogBinding customAadharVerifyDialogBinding =CustomAadharVerifyDialogBinding.inflate(inflate);

                materialAlertDialogBuilder.setView(customAadharVerifyDialogBinding.getRoot());
                materialAlertDialogBuilder.setCancelable(false);
                AlertDialog cusDialog = materialAlertDialogBuilder.show();
                customAadharVerifyDialogBinding.tvVerifiedAadharNumber.setText(binding.etAadharNumber.getText().toString());
                customAadharVerifyDialogBinding.tvVerifiedAadharName.setText(binding.etAadharName.getText().toString());


                customAadharVerifyDialogBinding.btnConfirmAadhar.setOnClickListener(view2 -> {
                    AadhaarEntity aadhaarEntity =  new AadhaarEntity();
                    aadhaarEntity.setAadharNumber(binding.etAadharNumber.getText().toString());
                    aadhaarEntity.setAadharName(binding.etAadharName.getText().toString());
                    aadhaarEntity.setAadharSyncStatus("0");
                    aadhaarEntity.setShgMemberCode(shgMemberCode);
                    aadhaarEntity.setAadharVerifiedStatus("0");
                    viewModel.insertAadhar(aadhaarEntity);

                    viewModel.updateAadharStatus(shgMemberCode,"1");

                    ViewUtilsKt.toast(getCurrentContext(),"Aadhar Saved successfully");
                    binding.tvAadharNumber.setText(binding.etAadharNumber.getText().toString());
                    binding.tvaadharName.setText(binding.etAadharName.getText().toString());
                    binding.llBtnLayout.setVisibility(View.GONE);
                    binding.llEnterAadharData.setVisibility(View.GONE);
                    binding.llDisplayAadharData.setVisibility(View.VISIBLE);
                    cusDialog.dismiss();

                    binding.cvChangeMonthYear.setVisibility(View.VISIBLE);



                });

                customAadharVerifyDialogBinding.btnUpdateAadhar.setOnClickListener(view2 -> {

                    binding.llBtnLayout.setVisibility(View.VISIBLE);
                    binding.llEnterAadharData.setVisibility(View.GONE);
                    binding.llDisplayAadharData.setVisibility(View.VISIBLE);
                    cusDialog.dismiss();

                });

            }

        });

        binding.btnUpdateAadhar.setOnClickListener(view1 -> {
            binding.llDisplayAadharData.setVisibility(View.GONE);
            binding.llEnterAadharData.setVisibility(View.VISIBLE);
            binding.etAadharName.setText("");
            binding.etAadharNumber.setText("");
        });

        binding.etAadharNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 12) {
                    /**check enter aadhar card exist in our local database or not**/
                    if (viewModel.isAadharExistInLocal(editable.toString())) {
                        binding.etAadharNumber.setText("");
                        ViewUtilsKt.toast(getCurrentContext(),"Aadhar Card already Exist.");
                    } else {
                        binding.ivAadhrScanner.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.ivAadhrScanner.setVisibility(View.GONE);
                }
            }
        });


        /*** check if data exist in member entry table or not******/
        memberEntryDataItem = viewModel.getAllEntryData(shgMemberCode, AppConstant.beforeNrlmStatus);

        if (!memberEntryDataItem.isEmpty()) {
            count = memberEntryDataItem.size();
            entryBeforeNrlmFoldAdapter = new EntryBeforeNrlmFoldAdapter(memberEntryDataItem, getCurrentContext(), viewModel);
            binding.rvEntryRecyclerview.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
            binding.rvEntryRecyclerview.setAdapter(entryBeforeNrlmFoldAdapter);
            entryBeforeNrlmFoldAdapter.notifyDataSetChanged();


            binding.cvRecyclerview.setVisibility(View.VISIBLE);
            binding.cvSelectActivity.setVisibility(View.GONE);
            binding.btnAddActivity.setText(getCurrentContext().getResources().getString(R.string.add_activity_msg));
            binding.tvTotalActivityCount.setVisibility(View.VISIBLE);
            binding.tvTotalActivityCount.setText(getCurrentContext().getResources().getString(R.string.total_activity) + count);

            seccName = memberEntryDataItem.get(0).getSeccName();
            seccNumber = memberEntryDataItem.get(0).getSeccNumber();

            binding.spinnerSeccNumber.setText(memberEntryDataItem.get(0).getSeccName());


            resetFunction(1);


        } else {

        }


        /**** add activity after selection****/
        binding.btnAddActivityDetail.setOnClickListener(view1 -> {

            if (seccNumber == null || seccNumber.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.secc_not_fill));
            } else if (sectorDate == null || sectorDate.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.sector_not_fill));
            } else if (activityCode == null || activityCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.activity_not_fill));
            } else if (incomeFrequencyCode == null || incomeFrequencyCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.frequency_not_fill));
            } else if (incomeRangCode == null || incomeRangCode.isEmpty()) {
                ViewUtilsKt.toast(getCurrentContext(), getContext().getResources().getString(R.string.range_not_fill));
            } else {
                loadEntryList();
                memberEntryDataItem = viewModel.getAllEntryData(shgMemberCode, AppConstant.beforeNrlmStatus);
                count = memberEntryDataItem.size();
                entryBeforeNrlmFoldAdapter = new EntryBeforeNrlmFoldAdapter(memberEntryDataItem, getCurrentContext(), viewModel);
                binding.rvEntryRecyclerview.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
                binding.rvEntryRecyclerview.setAdapter(entryBeforeNrlmFoldAdapter);
                entryBeforeNrlmFoldAdapter.notifyDataSetChanged();
                binding.cvRecyclerview.setVisibility(View.VISIBLE);
                binding.cvSelectActivity.setVisibility(View.GONE);
                binding.btnAddActivity.setText(getCurrentContext().getResources().getString(R.string.add_activity_msg));
                binding.tvTotalActivityCount.setVisibility(View.GONE);
                binding.tvTotalActivityCount.setText(getCurrentContext().getResources().getString(R.string.total_activity) + count);
                resetFunction(1);
            }
        });
       /****** Start add activity btn
         * user can add multiple activity*****/
        binding.btnAddActivity.setOnClickListener(view1 -> {
            binding.cvSelectActivity.setVisibility(View.VISIBLE);
            try {
                seccNumber=viewModel.checkSeccNumberInDb(shgMemberCode);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //loadSector();

            loadAllActivity( shgMemberCode);

            if (seccNumber!=null){
                if (!seccNumber.equalsIgnoreCase("") )
                    binding.ttlSeccc.setVisibility(View.GONE);
                else binding.ttlSeccc.setVisibility(View.VISIBLE);
            }else binding.ttlSeccc.setVisibility(View.VISIBLE);

        });
        /****** for reset current and all data on this screen*******/
        binding.btnReset.setOnClickListener(view1 -> {
           // ViewUtilsKt.toast(getCurrentContext(),"Not working yet");
        });
        binding.btnSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberEntryDataItem.size()>0){
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
                                        /***update confirmation status based on member code and entry flag**/
                                        viewModel.updateConfirmationStatus(shgMemberCode, AppConstant.beforeNrlmStatus);
                                  /*  NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToIncomeEntryAfterNrlmFragment();
                                    navController.navigate(navDirections);*/
                                        if (NetworkFactory.isInternetOn(getContext())) {
                                            ProgressDialog progressDialog=new ProgressDialog(getCurrentContext());
                                            progressDialog.setMessage(""+getCurrentContext().getResources().getString(R.string.loading_heavy));
                                            progressDialog.setCancelable(false);
                                            progressDialog.show();
                                            viewModel.checkDuplicateAtServer(getContext()
                                                    , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginId(), getContext())
                                                    , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefStateShortName(), getContext())
                                                    , PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(), getContext())
                                                    , AppUtils.getInstance().getDeviceInfo()
                                                    , "0.3536455,0.3288837"
                                                    , AppConstant.entryCompleted);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                   if (viewModel.getSyncApiStatus()!=null && viewModel.getSyncApiStatus().equalsIgnoreCase("E200")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Data Synced Successfully!!!", Toast.LENGTH_LONG).show();
                                                        try {
                                                            viewModel.updateBeforeEntryDateInLocal(shgMemberCode,entryMonthCode+"-"+entryYearCode);
                                                        } catch (ExecutionException e) {
                                                            e.printStackTrace();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                       }
                                                        NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToIncomeEntryAfterNrlmFragment();
                                                        navController.navigate(navDirections);
                                                       Navigation.findNavController(requireView()).popBackStack(R.id.memberEntryFragment, true);
                                                        /****this update date code comes after data sync sucessfully*****/
                                                    } else {
                                                       Toast.makeText(getContext(), "Data Synced Failed!!!", Toast.LENGTH_LONG).show();
                                                       NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToIncomeEntryAfterNrlmFragment();
                                                       navController.navigate(navDirections);
                                                        try {
                                                            viewModel.updateBeforeEntryDateInLocal(shgMemberCode,entryMonthCode+"-"+entryYearCode);
                                                        } catch (ExecutionException e) {
                                                            e.printStackTrace();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }

                                                        /****this update date code comes after data sync sucessfully*****/
                                                    }
                                                }

                                            }, 6000);
                                        } else {
                                            try {
                                                viewModel.updateBeforeEntryDateInLocal(shgMemberCode,entryMonthCode+"-"+entryYearCode);
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(getContext(), "Data saved successfully!!!", Toast.LENGTH_LONG).show();
                                            NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToIncomeEntryAfterNrlmFragment();
                                            navController.navigate(navDirections);
                                            Navigation.findNavController(requireView()).popBackStack(R.id.memberEntryFragment, true);
                                        }
                                    } else if (str.equalsIgnoreCase("2")) {
                                        dialogInterface.dismiss();
                                       /* NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToShgMemberFragment();
                                        navController.navigate(navDirections);*/
                  /*                  Observer<String> actionObserver = new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentToShgMemberFragment();
                                            navController.navigate(navDirections);
                                        }
                                    };
                                  viewModel.commonAleartDialog(getCurrentContext()).observe(getViewLifecycleOwner(), actionObserver);*/

                                    }
                                }
                            }).setCancelable(true).show();
                }else {
                    Toast.makeText(getContext(),"Activities are not added",Toast.LENGTH_LONG).show();
                }


            }
        });

        binding.spinnerSelectSector.setOnItemClickListener((adapterView, view1, i, l) -> {
            sectorDate = String.valueOf(viewModel.getAllSectorData().get(i).getSector_code());
            sectorName = viewModel.loadSectorData().get(i);
           resetFunction(2);
            loadActivityData(viewModel.getAllSectorData().get(i).getSector_code(), shgMemberCode);

        });
    }




    private void loadAllActivity(String memberCode) {
        /****** tis selection based on condition on activity id*****/
        activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getAllSelectedActivityName( memberCode, AppConstant.beforeNrlmStatus));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            activityCode = String.valueOf(viewModel.getAllSelectedActivity( memberCode, AppConstant.beforeNrlmStatus).get(i).getActivity_code());
            activityName = viewModel.getAllSelectedActivityName(memberCode, AppConstant.beforeNrlmStatus).get(i);

            sectorDate = String.valueOf(viewModel.getAllSelectedActivity( memberCode, AppConstant.beforeNrlmStatus).get(i).getSector_code());
            sectorName = viewModel.SectorName(viewModel.getAllSelectedActivity( memberCode, AppConstant.beforeNrlmStatus).get(i).getSector_code());
             resetFunction(3);
            loadFreaquency();
        });

    }

    private void loadSecc(String memberCode) {
        String seccStatus = viewModel.getSeccStatus(memberCode);
        if (seccStatus.equalsIgnoreCase("")) {
            binding.spinnerSeccNumber.setVisibility(View.GONE);
            seccNumber="0";
        } else {
            List<String> seccname=viewModel.loadSeccNameData(memberCode);
            if (seccname!=null && seccname.size()>0){
                seccAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text,seccname );
                binding.spinnerSeccNumber.setAdapter(seccAdapter);
                seccAdapter.notifyDataSetChanged();

                binding.spinnerSeccNumber.setOnItemClickListener((adapterView, view, i, l) -> {
                    seccName = viewModel.loadSeccNameData(memberCode).get(i);
               //     seccNumber = viewModel.getSeccData(memberCode).get(i).getSecc_no();
                    seccNumber = "0";
                });
            }else {
                seccNumber="0";
                binding.spinnerSeccNumber.setOnClickListener(view -> {
                   // Toast.makeText(getContext(),"SECC Data not found",Toast.LENGTH_LONG).show();
                });
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){

            // ViewUtilsKt.toast(this,"INSIDE ONACTIVITY RESULT");
            XmlPullParserFactory pullParserFactory;
            try {
                pullParserFactory = XmlPullParserFactory.newInstance();
                parser = pullParserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(result.getContents()));
                processParsing(parser);

            }catch (Exception e){
                ViewUtilsKt.toast(getCurrentContext(),"This is not right QR code");
            }

        }
    }

    private void processParsing(@NonNull XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
      //  appUtils.showLog("eventType" + eventType, HomeActivity.class);

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                //appUtils.showLog("startDocuments", HomeActivity.class);
            } else if (eventType == XmlPullParser.START_TAG) {
               // appUtils.showLog("Start tag " + parser.getName(), HomeActivity.class);
                if (parser.getName().equalsIgnoreCase("PrintLetterBarcodeData")) {
                    String uid = parser.getAttributeValue(null, "uid");
                    String name = parser.getAttributeValue(null, "name");
                    String  gender = parser.getAttributeValue(null, "gender");

                    progressDialog = new ProgressDialog(getCurrentContext());
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage(getString(R.string.loading_heavy));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(uid.equalsIgnoreCase(binding.etAadharNumber.getText().toString())){
                                binding.etAadharName.setText(name);
                            }else {
                                binding.etAadharNumber.setText("");
                                binding.etAadharName.setText("");
                                ViewUtilsKt.toast(getCurrentContext(),"Entered aadhaar number is not valid");
                            }

                        }
                    },3000);




                   //  ViewUtilsKt.toast(getCurrentContext(),""+uid+"--"+name+"--"+gender);
                }

            } else if (eventType == XmlPullParser.END_TAG) {
               // appUtils.showLog("End tag " + parser.getName(), HomeActivity.class);
            } else if (eventType == XmlPullParser.TEXT) {
               // appUtils.showLog("Text" + parser.getText(), HomeActivity.class);
            }
            eventType = parser.next();
        }
       // appUtils.showLog("EndDocuments", HomeActivity.class);
    }

    private void loadEntryList() {

        MemberEntryEntity memberEntryEntity = new MemberEntryEntity();
        memberEntryEntity.shgCode = shgCode;
        memberEntryEntity.shgMemberCode = shgMemberCode;
        if (entryYearCode.equalsIgnoreCase("2011")||entryYearCode.equalsIgnoreCase("2012")||entryYearCode.equalsIgnoreCase("2013")||entryYearCode.equalsIgnoreCase("2014")||entryYearCode.equalsIgnoreCase("2015")||entryYearCode.equalsIgnoreCase("2016")||entryYearCode.equalsIgnoreCase("2017")||entryYearCode.equalsIgnoreCase("2018")||entryYearCode.equalsIgnoreCase("2019")||entryYearCode.equalsIgnoreCase("2020")||entryYearCode.equalsIgnoreCase("2021")||entryYearCode.equalsIgnoreCase("2022")|| entryYearCode.equalsIgnoreCase("2023")||entryYearCode.equalsIgnoreCase("2024")||entryYearCode.equalsIgnoreCase("2025")||entryYearCode.equalsIgnoreCase("2026")||entryYearCode.equalsIgnoreCase("2027")||entryYearCode.equalsIgnoreCase("2028")||entryYearCode.equalsIgnoreCase("2029")||entryYearCode.equalsIgnoreCase("2030")||entryYearCode.equalsIgnoreCase("2031")||entryYearCode.equalsIgnoreCase("2032")||entryYearCode.equalsIgnoreCase("2033")) {
            memberEntryEntity.entryYearCode = entryYearCode;


            if (entryMonthCode.equalsIgnoreCase("12") || entryMonthCode.equalsIgnoreCase("11") || entryMonthCode.equalsIgnoreCase("10") || entryMonthCode.equalsIgnoreCase("09") || entryMonthCode.equalsIgnoreCase("08") || entryMonthCode.equalsIgnoreCase("07") || entryMonthCode.equalsIgnoreCase("06") || entryMonthCode.equalsIgnoreCase("05") || entryMonthCode.equalsIgnoreCase("04") || entryMonthCode.equalsIgnoreCase("03") || entryMonthCode.equalsIgnoreCase("02")  || entryMonthCode.equalsIgnoreCase("01")) {
                memberEntryEntity.entryMonthCode = entryMonthCode;
            } else {

                memberEntryEntity.entryMonthCode = "01";
            }
      /*  switch (entryMonthCode) {
            case "12":
                entryMonthCode = "12";
                break;

            case "11":
                entryMonthCode = "11";
                break;

            case "10":
                entryMonthCode = "10";
                break;

            case "09":
                entryMonthCode = "09";
                break;

            case "08":
                entryMonthCode = "08";
                break;

            case "07":
                entryMonthCode = "07";
                break;
            case "06":
                entryMonthCode = "06";
                break;
            case "05":
                entryMonthCode = "05";
                break;
            case "04":
                entryMonthCode = "04";
                break;
            case "03":
                entryMonthCode = "03";
                break;
            case "02":
                entryMonthCode = "02";
                break;
            case "01":
                entryMonthCode = "01";
                break;
            default:
                entryMonthCode = "01";

        }*/


            memberEntryEntity.entryCreatedDate = appDateFactory.getTimeStamp();
            memberEntryEntity.sectorDate = sectorDate;
            memberEntryEntity.activityCode = activityCode;
            memberEntryEntity.incomeFrequencyCode = incomeFrequencyCode;

            if (incomeFrequencyCode.equalsIgnoreCase("1"))
            {
                if (incomeRangCode.equalsIgnoreCase("1")||incomeRangCode.equalsIgnoreCase("2")||incomeRangCode.equalsIgnoreCase("3")||incomeRangCode.equalsIgnoreCase("4")){
                    memberEntryEntity.incomeRangCode = incomeRangCode;
                    memberEntryEntity.flagBeforeAfterNrlm = AppConstant.beforeNrlmStatus;
                    memberEntryEntity.flagSyncStatus = AppConstant.unsyncStatus;
                    memberEntryEntity.sectorName = sectorName;
                    memberEntryEntity.activityName = activityName;
                    memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
                    memberEntryEntity.incomeRangName = incomeRangName;
                    memberEntryEntity.monthName = entryMonthCode;
                    memberEntryEntity.seccNumber = seccNumber;
                    memberEntryEntity.seccName = seccName;
                    memberEntryEntity.entryCompleteConfirmation = "0";
                    viewModel.insertBeforeNrlmEntryData(memberEntryEntity);
                }
                else  DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please Uninstall the app download from Playstore"
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );

            }
            else if (incomeFrequencyCode.equalsIgnoreCase("2")){
                if (incomeRangCode.equalsIgnoreCase("5")||incomeRangCode.equalsIgnoreCase("6")||incomeRangCode.equalsIgnoreCase("7")||incomeRangCode.equalsIgnoreCase("8")){
                    memberEntryEntity.incomeRangCode = incomeRangCode;
                    memberEntryEntity.flagBeforeAfterNrlm = AppConstant.beforeNrlmStatus;
                    memberEntryEntity.flagSyncStatus = AppConstant.unsyncStatus;
                    memberEntryEntity.sectorName = sectorName;
                    memberEntryEntity.activityName = activityName;
                    memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
                    memberEntryEntity.incomeRangName = incomeRangName;
                    memberEntryEntity.monthName = entryMonthCode;
                    memberEntryEntity.seccNumber = seccNumber;
                    memberEntryEntity.seccName = seccName;
                    memberEntryEntity.entryCompleteConfirmation = "0";
                    viewModel.insertBeforeNrlmEntryData(memberEntryEntity);
                }
                else  DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please Uninstall the app download from Playstore"
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
            }
            else if (incomeFrequencyCode.equalsIgnoreCase("3")){
                if (incomeRangCode.equalsIgnoreCase("9")||incomeRangCode.equalsIgnoreCase("10")||incomeRangCode.equalsIgnoreCase("11")||incomeRangCode.equalsIgnoreCase("12")){
                    memberEntryEntity.incomeRangCode = incomeRangCode;
                    memberEntryEntity.flagBeforeAfterNrlm = AppConstant.beforeNrlmStatus;
                    memberEntryEntity.flagSyncStatus = AppConstant.unsyncStatus;
                    memberEntryEntity.sectorName = sectorName;
                    memberEntryEntity.activityName = activityName;
                    memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
                    memberEntryEntity.incomeRangName = incomeRangName;
                    memberEntryEntity.monthName = entryMonthCode;
                    memberEntryEntity.seccNumber = seccNumber;
                    memberEntryEntity.seccName = seccName;
                    memberEntryEntity.entryCompleteConfirmation = "0";
                    viewModel.insertBeforeNrlmEntryData(memberEntryEntity);
                }
                else  DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please Uninstall the app download from Playstore"
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
            }
            else if (incomeFrequencyCode.equalsIgnoreCase("4")){
                if (incomeRangCode.equalsIgnoreCase("13")||incomeRangCode.equalsIgnoreCase("14")||incomeRangCode.equalsIgnoreCase("15")||incomeRangCode.equalsIgnoreCase("16")){
                    memberEntryEntity.incomeRangCode = incomeRangCode;
                    memberEntryEntity.flagBeforeAfterNrlm = AppConstant.beforeNrlmStatus;
                    memberEntryEntity.flagSyncStatus = AppConstant.unsyncStatus;
                    memberEntryEntity.sectorName = sectorName;
                    memberEntryEntity.activityName = activityName;
                    memberEntryEntity.incomeFrequencyName = incomeFrequencyName;
                    memberEntryEntity.incomeRangName = incomeRangName;
                    memberEntryEntity.monthName = entryMonthCode;
                    memberEntryEntity.seccNumber = seccNumber;
                    memberEntryEntity.seccName = seccName;
                    memberEntryEntity.entryCompleteConfirmation = "0";
                    viewModel.insertBeforeNrlmEntryData(memberEntryEntity);
                }
                else  DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please Uninstall the app download from Playstore"
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
            }
            NavDirections navDirections = MemberEntryFragmentDirections.actionMemberEntryFragmentSelf();


        }
        else {
            DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), "Please change Your phone language in English"
                    , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
            );        }

    }
    private void loadActivityData(int id, String memberCode) {
        /****** tis selection based on condition on activity id*****/
        activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.getSelectedActivityName(id, memberCode, AppConstant.beforeNrlmStatus));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            activityCode = String.valueOf(viewModel.getSelectedActivity(id, memberCode, AppConstant.beforeNrlmStatus).get(i).getActivity_code());
            activityName = viewModel.getSelectedActivityName(id, memberCode, AppConstant.beforeNrlmStatus).get(i);
            resetFunction(3);
            loadFreaquency();
        });


       /* activityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadActivityData(id,memberCode));
        binding.spinnerSelectActivity.setAdapter(activityAdapter);
        activityAdapter.notifyDataSetChanged();

        binding.spinnerSelectActivity.setOnItemClickListener((adapterView, view1, i, l) -> {
            activityCode = String.valueOf(viewModel.getAllActivityData(id).get(i).getActivity_code());
            activityName = viewModel.loadActivityData(id,memberCode).get(i);
            resetFunction(3);
            loadFreaquency();
        });*/
    }

    private void loadSector() {
        sectorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, viewModel.loadSectorData());
        binding.spinnerSelectSector.setAdapter(sectorAdapter);
        sectorAdapter.notifyDataSetChanged();
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

    public boolean isDataValidate() {
        boolean status = true;
        if (sectorDate.isEmpty()) {
            ViewUtilsKt.toast(getCurrentContext(), "Select Sector first");
            status = false;
        } else if (activityCode.isEmpty()) {
            ViewUtilsKt.toast(getCurrentContext(), "Select Activity first");
            status = false;
        } else if (incomeFrequencyCode.isEmpty()) {
            ViewUtilsKt.toast(getCurrentContext(), "Select Frequency first");
            status = false;
        } else if (incomeRangCode.isEmpty()) {
            ViewUtilsKt.toast(getCurrentContext(), "Select Income Range first");
            status = false;
        } else {
            status = true;
        }
        return status;
    }
    private void updateApplication() {
        // final String appPackageName = context.getPackageName();
        try {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/11ai-E0CY-RshvTO3aHAISREQi74kEhb6/view?usp=sharing")));
            getCurrentContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nrlm.lakhpatikisaan")));
        } catch (android.content.ActivityNotFoundException anfe) {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://nrlm.gov.in/outerReportAction.do?methodName=showIndex")));
            getCurrentContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nrlm.lakhpatikisaan")));

        }
        //((Activity) context).finish();
        ((Activity) getCurrentContext()).finish();
    }
}









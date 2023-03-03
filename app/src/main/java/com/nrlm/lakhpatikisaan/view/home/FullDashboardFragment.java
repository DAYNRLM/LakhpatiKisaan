package com.nrlm.lakhpatikisaan.view.home;



import static com.nrlm.lakhpatikisaan.network.vollyCall.VolleyService.volleyService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.adaptor.ShgMemberAdapter;
import com.nrlm.lakhpatikisaan.database.entity.CheckDeleteShgEntity;
import com.nrlm.lakhpatikisaan.databinding.FragmentDashboardBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentFullDashboardBinding;
import com.nrlm.lakhpatikisaan.network.model.request.DashboardRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.MemberInActiveRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.BlockApiResponse;
import com.nrlm.lakhpatikisaan.network.model.response.CheckDeleteShgResponseBean;
import com.nrlm.lakhpatikisaan.network.model.response.DashboardResponse;
import com.nrlm.lakhpatikisaan.network.model.response.StateApiResponse;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyResult;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyService;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppDateFactory;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.auth.SignUpFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FullDashboardFragment extends BaseFragment<HomeViewModel, FragmentFullDashboardBinding> {

    ProgressDialog progressDialog;
    public VolleyResult mResultCallBack = null;
    DashboardResponse dashboardResponse;
    public   String date2=null;

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
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewModel.getHomeViewModelRepos(getCurrentContext());




            String memberAlotted = Objects.requireNonNull(PreferenceFactory.getInstance()).getSharedPrefrencesData(PreferenceKeyManager.getMemberAlotted(), getCurrentContext());
            String shgAlotted = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgAlotted(), getCurrentContext());
            String shgMemberSurveyCompleted = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgMemberSurveyCompleted(), getCurrentContext());
            String shgMemberSurveyPending = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgMemberSurveyPending(), getCurrentContext());
            String shgWhoseAllMemberSurveyCompleted = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgWhoseAllMemberSurveyCompleted(), getCurrentContext());
            String shgAtleastOneMemberSurveyCompleted = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgAtleastOneMemberSurveyCompleted(), getCurrentContext());
             date2 = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getDate(), getCurrentContext());

        assert memberAlotted != null;
        if(memberAlotted.equalsIgnoreCase("")) {
            @SuppressLint("SimpleDateFormat") String currdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = null;
            try {
                d1 = sdformat.parse(currdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 =null;
            try {
                if(date2.equalsIgnoreCase("")){

                    date2="2022-06-12";
                    d2 = sdformat.parse(date2);


                }
                else{

                    d2 = sdformat.parse(date2);


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }




            assert d1 != null;
            if(d1.compareTo(d2) > 0) {
                System.out.println("currdate > Date 2");

                binding.btnUpdate.setVisibility(View.VISIBLE);
            }  else if(d1.compareTo(d2) == 0) {
                System.out.println("Both dates are equal");
                binding.btnUpdate.setVisibility(View.GONE);
            }



        }
         else {
            @SuppressLint("SimpleDateFormat") String currdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = null;
            try {
                d1 = sdformat.parse(currdate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 = null;
            try {
                assert date2 != null;
                d2 = sdformat.parse(date2);
              //  d2 = sdformat.parse("2023-02-22");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert d1 != null;
            if(d1.compareTo(d2) > 0) {
                System.out.println("currdate > Date 2");
                binding.btnUpdate.setVisibility(View.VISIBLE);
            }  else if(d1.compareTo(d2) == 0) {
                System.out.println("Both dates are equal");
                binding.btnUpdate.setVisibility(View.GONE);
            }



            binding.shgNumberTextview.setText(shgAlotted);
            binding.memberNumberTextview.setText(memberAlotted);
            binding.surveyCompleted.setText(shgMemberSurveyCompleted);
            binding.surveyPending.setText(shgMemberSurveyPending);
            binding.shgWhoseAllMembercompleted.setText(shgWhoseAllMemberSurveyCompleted);
            binding.shgWhoseOneMembercompleted.setText(shgAtleastOneMemberSurveyCompleted);
            //  binding.syncedServer.setText(shgMemberSurveyCompleted);
            binding.date.setText(date2);
            binding.SyncedServer.setText(shgMemberSurveyCompleted);
            binding.syncLocally.setText(viewModel.getBeforeLocally());

        }
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnUpdate.setVisibility(View.GONE);
                callDashboardApi();




            }
        });







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




    private void callDashboardApi () {

        if(NetworkFactory.isInternetOn(getCurrentContext()))
        {


            progressDialog = new ProgressDialog(getCurrentContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();




            /*******make json object is encrypted and *********/
            JSONObject encryptedObject =new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();


                String imeiNo=  PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(),getCurrentContext());


                DashboardRequestBean dashboardRequestBean=new DashboardRequestBean();

                String stateShortName=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyStateShortName(),getCurrentContext());
                String loginId=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyLoginid(),getCurrentContext());
                dashboardRequestBean.setLogin_id(loginId);
                dashboardRequestBean.setImei_no(imeiNo);
                dashboardRequestBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());
                dashboardRequestBean.setLocation_coordinate("10.3111313,154.16546");
                dashboardRequestBean.setState_short_name(stateShortName);


                encryptedObject.accumulate("data",cryptography.encrypt(new Gson().toJson(dashboardRequestBean)));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /***********************************************/

            AppUtils.getInstance().showLog("request" +encryptedObject, FullDashboardFragment.class);
            mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    progressDialog.dismiss();
                    JSONObject jsonObject = null;

                    String objectResponse="";
                    if(response.has("data")){
                        try {
                            objectResponse=response.getString("data");
                            AppUtils.getInstance().showLog("dashboard"+objectResponse,FullDashboardFragment.class);


                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("objjjjjjj"+objectResponse,FullDashboardFragment.class);
                        }
                    }else {
                        return;
                    }

                    try {
                        JSONObject jsonObject1=new JSONObject(objectResponse);
                        objectResponse=jsonObject1.getString("data");
                        AppUtils.getInstance().showLog("dashboard"+jsonObject1,FullDashboardFragment.class);
                    }catch (JSONException e)
                    {
                        AppUtils.getInstance().showLog("exceptionDataOfBlock"+e,FullDashboardFragment.class);

                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Cryptography cryptography = new Cryptography();
                            jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                            //if (jsonObject.getString("E200").equalsIgnoreCase("Success"))
                            AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), FullDashboardFragment.class);
                            JSONObject error=jsonObject.getJSONObject("error");
                            if(error.getString("code").equalsIgnoreCase("E200"))
                                dashboardResponse= DashboardResponse. jsonToJava(jsonObject.toString());
                              /*  for (int i = 1; i<=1;i++){
                                    String shgAlotted = dashboardResponse.getDashboarddata().get(i).getShg_alloted();
                                    Toast.makeText(getCurrentContext(),shgAlotted,Toast.LENGTH_LONG).show();
                                    binding.shgTextview.setText(shgAlotted);
                                }*/
                            for(int j=0;j<dashboardResponse.getDashboarddata().size();j++){

                                String shgAlotted = dashboardResponse.getDashboarddata().get(j).getShg_alloted();
                                String memberAlotted = dashboardResponse.getDashboarddata().get(j).getShg_member_alloted();
                                String shgMemberSurveyCompleted = dashboardResponse.getDashboarddata().get(j).getShg_member_survey_completed();
                                String shgMemberSurveyPending = dashboardResponse.getDashboarddata().get(j).getShg_member_survey_pending();
                                String shgWhoseAllMemberSurveyCompleted = dashboardResponse.getDashboarddata().get(j).getShg_whose_all_mem_survy_completed();
                                String shgAtleastOneMemberSurveyCompleted = dashboardResponse.getDashboarddata().get(j).getShg_whose_atleast_one_mem_survy_completed();
                                String date = dashboardResponse.getDashboarddata().get(j).getCurrent_date();
                                binding.syncLocally.setText(viewModel.getBeforeLocally());


                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getShgAlotted(),shgAlotted , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getMemberAlotted(),memberAlotted , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getShgMemberSurveyCompleted(),shgMemberSurveyCompleted , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getShgMemberSurveyPending(),shgMemberSurveyPending , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getShgWhoseAllMemberSurveyCompleted(),shgWhoseAllMemberSurveyCompleted , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getShgAtleastOneMemberSurveyCompleted(),shgAtleastOneMemberSurveyCompleted , getContext());
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getDate(),date , getContext());





                                binding.shgNumberTextview.setText(shgAlotted);
                                binding.memberNumberTextview.setText(memberAlotted);
                                binding.surveyCompleted.setText(shgMemberSurveyCompleted);
                                binding.surveyPending.setText(shgMemberSurveyPending);
                                binding.shgWhoseAllMembercompleted.setText(shgWhoseAllMemberSurveyCompleted);
                                binding.shgWhoseOneMembercompleted.setText(shgAtleastOneMemberSurveyCompleted);
                              //  binding.syncedServer.setText(shgMemberSurveyCompleted);
                                binding.date.setText(date);
                                binding.SyncedServer.setText(shgMemberSurveyCompleted);



                            }

                                AppUtils.getInstance().showLog("responseJSON" + error.toString(), FullDashboardFragment.class);

                            {
                                AppUtils.getInstance().showLog("responseJSONmain" + error.toString(), FullDashboardFragment.class);


                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            AppUtils.getInstance().showLog("DecryptEx" + e, ShgMemberAdapter.class);
                        }
                    }


                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    progressDialog.dismiss();

                }
            };
            volleyService = VolleyService.getInstance(getCurrentContext());

            volleyService.postDataVolley("dashboardRequest", AppConstant.baseUrl+"dashboarddata", encryptedObject, mResultCallBack);
            //volleyService.postDataVolley("InactiveRequest", "http://10.197.183.105:8989/lakhpatishg/inactivememberdata", encryptedObject, mResultCallBack);



        }
        else
        {

          String memberAlotted=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getMemberAlotted(),getCurrentContext());
          String shgAlotted=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgAlotted(),getCurrentContext());
          String shgMemberSurveyCompleted=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgMemberSurveyCompleted(),getCurrentContext());
          String shgMemberSurveyPending=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgMemberSurveyPending(),getCurrentContext());
          String shgWhoseAllMemberSurveyCompleted=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgWhoseAllMemberSurveyCompleted(),getCurrentContext());
          String shgAtleastOneMemberSurveyCompleted=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getShgAtleastOneMemberSurveyCompleted(),getCurrentContext());
          String date=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getDate(),getCurrentContext());


            binding.shgNumberTextview.setText(shgAlotted);
            binding.memberNumberTextview.setText(memberAlotted);
            binding.surveyCompleted.setText(shgMemberSurveyCompleted);
            binding.surveyPending.setText(shgMemberSurveyPending);
            binding.shgWhoseAllMembercompleted.setText(shgWhoseAllMemberSurveyCompleted);
            binding.shgWhoseOneMembercompleted.setText(shgAtleastOneMemberSurveyCompleted);
            binding.date.setText(date);
            binding.SyncedServer.setText(shgMemberSurveyCompleted);
            binding.syncLocally.setText(viewModel.getBeforeLocally());

            Toast.makeText(getCurrentContext(), "Please check Internet Connection", Toast.LENGTH_LONG).show();




        }





    }

    }



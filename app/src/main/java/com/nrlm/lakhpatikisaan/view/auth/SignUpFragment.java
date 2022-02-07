package com.nrlm.lakhpatikisaan.view.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentSignUpBinding;
import com.nrlm.lakhpatikisaan.network.model.response.BlockApiResponse;
import com.nrlm.lakhpatikisaan.network.model.response.DistrictApiResponse;
import com.nrlm.lakhpatikisaan.network.model.response.LanguageApiResponse;
import com.nrlm.lakhpatikisaan.network.model.response.StateApiResponse;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyResult;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyService;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SignUpFragment extends BaseFragment<AuthViewModel, FragmentSignUpBinding> {
    ProgressDialog progressDialog;
    public VolleyResult mResultCallBack = null;
    public VolleyService volleyService;
    public ArrayAdapter<String> stateAdapter;
    public ArrayAdapter<String> districAdapter;
    List<String> state;
    StateApiResponse stateApiResponse;
    DistrictApiResponse districtApiResponse;
    List<String> district;
    BlockApiResponse blockApiResponse;
    List<String> block;
    ArrayAdapter<String> blockAdapter;
    ArrayAdapter<String> languageAdapter;
    List<String> language;
    LanguageApiResponse languageApiResponse;
    String selectedBlock;
    String mobileNumber;
    int selectedLanguageCode;
    String userName;
    NavController navController;
    boolean otpVerified;


    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentSignUpBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSignUpBinding.inflate(inflater, container, false);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onFragmentReady() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        state = new ArrayList<>();
        volleyService = VolleyService.getInstance(getCurrentContext());
        navController = NavHostFragment.findNavController(this);


        callStateApi();


        binding.spinnerState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StateApiResponse.StateResponse selectedState = stateApiResponse.getStateResponse().get(position);
                String selectedStateVal = selectedState.getState_code();
                getDistrictApiCall(selectedStateVal);
                binding.spinnerDistrict.setText("");
                binding.spinnerBlock.setText("");
                binding.spinnerDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DistrictApiResponse.DistrictResponse selectedDistrict = districtApiResponse.getDistrictResponse().get(position);
                        String selectedDistrictCode = selectedDistrict.getDistrict_code();
                        getBlockApiCall(selectedDistrictCode);
                        binding.spinnerBlock.setText("");

                        binding.spinnerBlock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BlockApiResponse.BlockResponse blockResponse = blockApiResponse.getBlockResponse().get(position);
                                selectedBlock = blockResponse.getBlock_code();
                                getLanguageApiCall();
                                binding.selectLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        LanguageApiResponse.LanguageResponse languageResponse=languageApiResponse.getLanguageResponse().get(position);
                                        selectedLanguageCode=languageResponse.getId();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        binding.etUserMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                if(s.toString().length()<10)
                {
                    binding.otpLiner.setVisibility(View.GONE);
                    binding.submit.setVisibility(View.GONE);
                }
                if (s.toString().length() == 10) {
                    if (AppUtils.isValid(s.toString())) {
                        Toast.makeText(getCurrentContext(), "Valid", Toast.LENGTH_SHORT).show();
                        mobileNumber=binding.etUserMobile.getText().toString().trim();
                        getApiCallForMobileVerification(s.toString());
                    } else {
                        Toast.makeText(getCurrentContext(),"Mobile number is not valid.", Toast.LENGTH_SHORT).show();
                        binding.etUserMobile.setText("");

                    }
                }
            }
       });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.spinnerState.getText()!=null&&binding.spinnerDistrict.getText()!=null&&binding.spinnerBlock!=null&&binding.etUserName!=null
                        &&binding.etUserMobile!=null){
                    getSignUpSubmitApiCall();
                }else
                {
                    DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert),"Entered details are improper."
                            , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false


                    );
                }
            }
        });
    }

    private void getSignUpSubmitApiCall() {

            if (NetworkFactory.isInternetOn(getCurrentContext())) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                userName=binding.etUserName.getText().toString().trim();
              String email=binding.etUserEmail.getText().toString().trim();
                JSONObject submitSignUp=new JSONObject();
                try {

                    submitSignUp.accumulate("entity_code",selectedBlock);
                    submitSignUp.accumulate("username",userName);
                    submitSignUp.accumulate("mobile_no",mobileNumber);
                    submitSignUp.accumulate("email_id",email);
                    submitSignUp.accumulate("language_id",selectedLanguageCode);
                    submitSignUp.accumulate("created_by_app_name","Lakhpati Didi");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(submitSignUp.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack=new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("ExceptionInVerifyMobile" +
                                        ""+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataAtSubmit"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionAtSubmit"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        try {
                            JSONObject submitRes=jsonObject.getJSONObject("error");
                            if(submitRes.getString("code").equalsIgnoreCase("E200"))
                            {
                                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), R.drawable.ic_launcher_background, getString(R.string.alert), "We have sent the request to Block for verification, once it get verified you will receive your credentials through SMS.", getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        NavDirections navDirections=SignUpFragmentDirections.actionSignUpFragmentToAuthFragment();
                                        navController.navigate(navDirections);

                                    }
                                }, false);

                            }else
                            {
                                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), submitRes.getString("message")
                                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false


                                );
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {

                    }
                };

                volleyService.postDataVolley("submitRequest","https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/usersignup",encryptedObject,mResultCallBack);

            }else{

            }

    }
    private void getApiCallForMobileVerification(String mobi) {
        JSONObject mobileVerfication=null;

        if (NetworkFactory.isInternetOn(getCurrentContext())) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.loading_heavy));
            progressDialog.setCancelable(false);
            progressDialog.show();
            if(mobi.equals(null) ||mobi.equalsIgnoreCase("") || mobi.length()<10 )
            {
                progressDialog.dismiss();
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), getString(R.string.number_cant_be_empty)
                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
                );
            }
            else {
                String otp = AppUtils.getInstance().getRandomOtp();
                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getRandomOtp(), otp, getCurrentContext());
                  AppUtils.getInstance().showLog("otp:-"+otp, SignUpFragment.class);

                mobileVerfication = new JSONObject();
                try {
                    mobileVerfication.accumulate("mobileno", mobi);
                    mobileVerfication.accumulate("message", otp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
              JSONObject   encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(mobileVerfication.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("ExceptionInVerifyMobile" +
                                        ""+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataMobileNumberVerification"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionDataOfMobileNumberVerification"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        try {
                            JSONObject error=jsonObject.getJSONObject("error");
                            if(error.getString("code").equalsIgnoreCase("E200"))
                            {
                                binding.otpLiner.setVisibility(View.VISIBLE);
                                binding.etOtp.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(@NonNull Editable s) {
                                        if (s.toString().length() == 4) {
                                            if (binding.etOtp.getText().toString().equalsIgnoreCase(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getRandomOtp(),getCurrentContext()))) {
                                                Toast.makeText(getCurrentContext(), "Valid", Toast.LENGTH_SHORT).show();
                                                binding.otpLiner.setVisibility(View.GONE);
                                                binding.mobiverify.setVisibility(View.VISIBLE);
                                                binding.submit.setVisibility(View.VISIBLE);
                                            } else {
                                                Toast.makeText(getCurrentContext(),"Invalid Otp", Toast.LENGTH_SHORT).show();
                                                binding.etOtp.setText("");

                                            }
                                        }
                                    }
                                });
                            }else if(error.getString("code").equalsIgnoreCase("E202"))
                            {
                                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), error.getString("message")
                                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false );
                            return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }


                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {
                               AppUtils.getInstance().showLog(""+error.toString(),SignUpFragment.class);

                    }
                };
                volleyService.postDataVolley("verifyMobileNumber", "https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/verifymobile", encryptedObject, mResultCallBack);

            }

        } else {

        }

    }

        private void getLanguageApiCall ()
        {
            if (NetworkFactory.isInternetOn(getCurrentContext())) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject langReq = new JSONObject();
                try {
                    langReq.accumulate("app_name", "Lakhpati Didi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(langReq.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("exceptionInLanguageEncryption"+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataOfLanguageData"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionDataOfLanguageData"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        language = new ArrayList<>();
                        languageApiResponse = LanguageApiResponse.jsonToJava(jsonObject.toString());
                        languageApiResponse.getStatus();
                        for (LanguageApiResponse.LanguageResponse languageResponse : languageApiResponse.getLanguageResponse()) {
                            language.add(languageResponse.getLanguage_name());
                        }

                        languageAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_text, language);
                        languageAdapter.setDropDownViewResource(R.layout.spinner_text);

                        binding.selectLanguage.setAdapter(languageAdapter);
                        languageAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {

                    }
                };

                volleyService.postDataVolley("languageData", "https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/languagemasterdata", encryptedObject, mResultCallBack);

            } else {

            }
        }
        private void getBlockApiCall (String selectedDistrictCode)
        {

            if (NetworkFactory.isInternetOn(getCurrentContext())) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject blockReq = new JSONObject();
                try {
                    blockReq.accumulate("district_code", selectedDistrictCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(blockReq.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("exceptionInBlockData"+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataOfBlock"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionDataOfBlock"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        block = new ArrayList<>();
                        blockApiResponse = BlockApiResponse.jsonToJava(jsonObject.toString());
                        blockApiResponse.getStatus();
                        for (BlockApiResponse.BlockResponse blockResponse : blockApiResponse.getBlockResponse()) {
                            block.add(blockResponse.getBlock_name());
                        }
                        blockAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_text, block);
                        blockAdapter.setDropDownViewResource(R.layout.spinner_text);
                        binding.spinnerBlock.setAdapter(blockAdapter);
                        blockAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {

                    }
                };

                volleyService.postDataVolley("blockData", "https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/blockmasterdata", encryptedObject, mResultCallBack);

            } else {

            }
        }
        private void getDistrictApiCall (String selectedStateVal){
            if (NetworkFactory.isInternetOn(getCurrentContext())) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject districtReq = new JSONObject();
                try {
                    districtReq.accumulate("state_code", selectedStateVal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(districtReq.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("exceptionInEncryptedData"+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataOfDistrict"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionDataOfDistrict"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of districts
                                AppUtils.getInstance().showLog("responseJSONDistrict" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        district = new ArrayList<>();
                        districtApiResponse = DistrictApiResponse.jsonToJava(jsonObject.toString());
                        districtApiResponse.getStatus();
                        for (DistrictApiResponse.DistrictResponse districtResponse : districtApiResponse.getDistrictResponse()) {
                            district.add(districtResponse.getDistrict_name());
                        }

                        districAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_text, district);
                        districAdapter.setDropDownViewResource(R.layout.spinner_text);

                        binding.spinnerDistrict.setAdapter(districAdapter);
                        districAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {

                    }
                };

                volleyService.postDataVolley("districtData", "https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/districtmasterdata", encryptedObject, mResultCallBack);

            } else {

            }

        }

        private void callStateApi () {
            if (NetworkFactory.isInternetOn(getCurrentContext())) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject stateReq = new JSONObject();
                try {
                    stateReq.accumulate("app_version", "1.0.0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*******make json object is encrypted and *********/
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();

                    encryptedObject.accumulate("data",cryptography.encrypt(stateReq.toString()));
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

                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, SignUpFragment.class);
                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;

                        String objectResponse="";
                        if(response.has("data")){
                            try {
                                objectResponse=response.getString("data");

                            }catch (JSONException e)
                            {
                                AppUtils.getInstance().showLog("Exception"+e,SignUpFragment.class);
                            }
                        }else {
                            return;
                        }

                        try {
                            JSONObject jsonObject1=new JSONObject(objectResponse);
                            objectResponse=jsonObject1.getString("data");
                            AppUtils.getInstance().showLog("dataOfState"+jsonObject1,SignUpFragment.class);
                        }catch (JSONException e)
                        {
                            AppUtils.getInstance().showLog("exceptionDataOfSate"+e,SignUpFragment.class);

                        }


                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                Cryptography cryptography = new Cryptography();
                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), SignUpFragment.class);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getCurrentContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                                AppUtils.getInstance().showLog("DecryptEx" + e, SignUpFragment.class);
                            }
                        }
                        stateApiResponse = StateApiResponse.jsonToJava(jsonObject.toString());
                        stateApiResponse.getStatus();
                        for (StateApiResponse.StateResponse stateResponse : stateApiResponse.getStateResponse()) {
                            state.add(stateResponse.getState_name());
                        }

                        stateAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_text, state);
                        stateAdapter.setDropDownViewResource(R.layout.spinner_text);

                        binding.spinnerState.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {

                    }
                };

                volleyService.postDataVolley("stateData", "https://nrlm.gov.in/lakhpatishgDemo/lakhpatishg/statemasterdata", encryptedObject, mResultCallBack);

            } else {

            }
        }

    }



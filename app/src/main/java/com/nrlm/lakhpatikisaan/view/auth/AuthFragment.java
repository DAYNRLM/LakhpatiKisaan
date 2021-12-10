package com.nrlm.lakhpatikisaan.view.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.google.android.material.textfield.TextInputEditText;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

import org.json.JSONException;

public class AuthFragment extends BaseFragment<AuthViewModel, FragmentAuthLoginBinding> {

    private ProgressDialog progressDialog;
    private AuthViewModel authViewModel;

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentAuthLoginBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAuthLoginBinding.inflate(inflater, container, false);
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

         authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.tvForgetPassword.setOnClickListener(v -> {
            NavDirections action = AuthFragmentDirections.actionAuthFragmentToSendOtpFragment();
            navController.navigate(action);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString();
            String userId = binding.etUserId.getText().toString();
            if (userId.equalsIgnoreCase("")){
                binding.etUserId.setError("Invalid user id");
            }else if(password.equalsIgnoreCase("")) {
                binding.etPassword.setError("Invalid password");
            }else {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
                authViewModel.makeLoginRequestData(getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String loginApiStatus=authViewModel.loginApiResult();
                        AppUtils.getInstance().showLog("loginApiStatus"+loginApiStatus,AuthFragment.class);
                        progressDialog.dismiss();
                        if (loginApiStatus.equalsIgnoreCase("E200")){
                            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLoginSessionKey(),"logedin",getContext());
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            try {

                                showServerError(loginApiStatus);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },10000);

            }

        });
    }
    public  void showServerError(String error_code) throws JSONException {
        switch (error_code) {

            case "E202":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.invalid_fields)
                        , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, true
                );

                break;

            case "E201":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_security_validation)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E1004":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_validation)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            case "E206":
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.error_session_exist)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
                break;

            default:
                DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.info), getString(R.string.SERVER_ERROR_TITLE)
                        , getString(R.string.ok), (dialog, which) -> dialog.dismiss(), null, null, true
                );
        }
    }

}

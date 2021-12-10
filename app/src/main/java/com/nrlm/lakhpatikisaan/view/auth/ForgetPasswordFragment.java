package com.nrlm.lakhpatikisaan.view.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentForgetpasswordBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

import org.json.JSONException;

public class ForgetPasswordFragment extends BaseFragment<AuthViewModel, FragmentForgetpasswordBinding> {
    private String userId;
    private String password;
    private String confirmPassword;
    private String etrOpt;
    LinearLayout linearLayoutFurtherView;
    private AuthViewModel authViewModel;
    ProgressDialog progressDialog;

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentForgetpasswordBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentForgetpasswordBinding.inflate(inflater, container, false);
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
        linearLayoutFurtherView=(LinearLayout) view.findViewById(R.id.llfurther_view);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.btnUpdatePw.setOnClickListener(view1 -> {
            etrOpt=binding.etEnterOtp.getText().toString();
            checkingOtp(etrOpt);
        /*    userId=binding.etUserId.getText().toString();
            password=binding.etPassword.getText().toString();
            confirmPassword=binding.etConfirmPassword.getText().toString();*/

                });

    }

    private void checkingOtp(String etrOpt) {
      String genratedOtp=  PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getRandomOtp(),getCurrentContext());

        if (!genratedOtp.equalsIgnoreCase(etrOpt))
        {
            DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), "Entered Otp in invalid."
                    , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
            );
            return;
        }
        else
        {

              /*  linearLayoutFurtherView.setVisibility(LinearLayout.VISIBLE);
                if(userId.equalsIgnoreCase("xyz")&&userId.equalsIgnoreCase(""))
                {
                    if (password.equalsIgnoreCase(confirmPassword) && !password.equalsIgnoreCase("")&& !confirmPassword.equalsIgnoreCase(""))
                    {*/
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.loading_heavy));
            progressDialog.setCancelable(false);
            progressDialog.show();
            authViewModel.ResetPasswordRequestData(getContext());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                       progressDialog.dismiss();
                    if(authViewModel.resetPasswordApiStatuss.equalsIgnoreCase("E200"))
                    {
                        DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert),  authViewModel.simpleResponseBean.getError().getMessage()
                                , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
                        );


                    }else {
                        DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), "Oops something went Wrong"
                                , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false);


                    }
                }
            },2000);

          }


              /*      }else {
                        //dialog
                    }
                }else {
                    //dialog
                }*/



        }



    }


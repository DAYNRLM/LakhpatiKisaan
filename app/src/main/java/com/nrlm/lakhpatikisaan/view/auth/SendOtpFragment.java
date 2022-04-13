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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentAuthLoginBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentOtpSendBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class SendOtpFragment extends BaseFragment<AuthViewModel,FragmentOtpSendBinding>  {
    private AuthViewModel authViewModel;
    String mobileNumber;
    ProgressDialog progressDialog;

    @Override
    public Class<AuthViewModel> getViewModel() {
        return AuthViewModel.class;
    }

    @Override
    public FragmentOtpSendBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentOtpSendBinding.inflate(inflater, container, false);
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
        binding.btnSendOtp.setOnClickListener(v -> {
            mobileNumber=binding.etMobileNumber.getText().toString();

            if(mobileNumber.equals(null) ||mobileNumber.equalsIgnoreCase("") || mobileNumber.length()<10 )
           {
               DialogFactory.getInstance().showAlertDialog(getCurrentContext(), 1, getString(R.string.alert), getString(R.string.number_cant_be_empty)
                       , getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss(), null, null, false
               );
           }else {
                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getForgotMobileNumber(),mobileNumber,getCurrentContext());

                authViewModel.makeOtpRequest(getCurrentContext());
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading_heavy));
                progressDialog.setCancelable(false);
                progressDialog.show();
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.dismiss();
                      String msg=authViewModel.loginApiResult();
                    //  String msg="error";
                       if(msg.equalsIgnoreCase("E200"))
                       {
                           NavDirections action = SendOtpFragmentDirections.actionSendOtpFragmentToForgetPasswordFragment();
                           navController.navigate(action);
                       }else
                       {
                           MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getCurrentContext());
                           materialAlertDialogBuilder.setCancelable(false);
                           materialAlertDialogBuilder.setMessage(msg);
                           materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                                  /* Intent intent = new Intent(getContext(), AuthActivity.class);
                                   startActivity(intent);*/

                               }
                           });
                           materialAlertDialogBuilder.show();

                       }


                   }
               },4000);

           }

        });
    }
}

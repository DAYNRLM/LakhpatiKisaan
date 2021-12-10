package com.nrlm.lakhpatikisaan.view.mpin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentVerifyMpinBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

public class VerifyMpinFragment extends BaseFragment<MpinViewModel, FragmentVerifyMpinBinding> {
    @Override
    public Class<MpinViewModel> getViewModel() {
        return MpinViewModel.class;
    }

    @Override
    public FragmentVerifyMpinBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentVerifyMpinBinding.inflate(inflater,container,false);
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
        binding.btnVerify.setOnClickListener(viewBtn->{
            String vrfMpin=binding.pinviewGetMpin.getText().toString();
            if(vrfMpin.isEmpty())
            {
                ViewUtilsKt.toast(getCurrentContext(),getCurrentContext().getResources().getString(R.string.mpin_can_not_empty));
            }else{
                if(vrfMpin.equalsIgnoreCase(PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyMpin(),getCurrentContext()))){
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyLoginDone(),"ok",getCurrentContext());
                    Intent intentToHomeActy=new Intent(getCurrentContext(), HomeActivity.class);
                    intentToHomeActy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentToHomeActy);
                }else
                {
                    DialogFactory.getInstance().showAlert(getCurrentContext(),"Entered Mpin is wrong.","Ok");
                }
            }
        });
    }
    }


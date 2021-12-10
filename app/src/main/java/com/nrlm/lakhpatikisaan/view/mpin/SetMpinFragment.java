package com.nrlm.lakhpatikisaan.view.mpin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.databinding.FragmentSetMpinBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class SetMpinFragment extends BaseFragment<MpinViewModel, FragmentSetMpinBinding> {
    @Override
    public Class<MpinViewModel> getViewModel() {
        return MpinViewModel.class;
    }

    @Override
    public FragmentSetMpinBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSetMpinBinding.inflate(inflater,container,false);
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
        binding.btnSetMpin.setOnClickListener(viewBtn->{
            String mpin=binding.pinviewFirst.getText().toString();
            String verifyMpin=binding.pinviewSecond.getText().toString();
            if(mpin.isEmpty())
            {
                ViewUtilsKt.toast(getCurrentContext(),getCurrentContext().getResources().getString(R.string.enter_mpin_first));
            }else if(verifyMpin.isEmpty()){
                ViewUtilsKt.toast(getCurrentContext(),getCurrentContext().getResources().getString(R.string.enter_confirm_first));
            }else
            {
                if(mpin.equalsIgnoreCase(verifyMpin))
                {
                    PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefKeyMpin(),verifyMpin,getCurrentContext());
                    NavDirections navDirections= SetMpinFragmentDirections.actionSetMpinFragmentToVerifyMpinFragment();
                    navController.navigate(navDirections);
                }else{
                    DialogFactory.getInstance().showAlert(getCurrentContext(),"Entered Mpin is wrong.","Ok");

                }

            }

        });
    }
}

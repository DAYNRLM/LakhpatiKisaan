package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class HomeViewModel extends ViewModel {
    private MasterDataRepo masterDataRepo;
    public HomeViewModel(){

        }


    // common alert dialog
    public MutableLiveData<String> commonAleartDialog(Context context){
        MutableLiveData<String> resetData = new MutableLiveData<>();

        new MaterialAlertDialogBuilder(context).setTitle("Test").setIcon(R.drawable.ic_baseline_add_circle_outline_24)
                .setMessage("check")
                .setPositiveButton("ok",(dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetData.setValue("ok");
                }).setNegativeButton("cancel",(dialogInterface, i) -> {

            dialogInterface.dismiss();
            resetData.setValue("cancel");
        }).show();
        return resetData;
    }







}

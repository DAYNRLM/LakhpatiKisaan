package com.nrlm.lakhpatikisaan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.nrlm.lakhpatikisaan.network.model.request.ContactsRequestBean;

public class MasterDataViewModel extends ViewModel {


    private void contactRequest(){
        ContactsRequestBean contactsRequestBean = new ContactsRequestBean();
        contactsRequestBean.setState_name("TAMIL NADU");
        contactsRequestBean.setDistrict_name("DINDIGUL");
    }


}

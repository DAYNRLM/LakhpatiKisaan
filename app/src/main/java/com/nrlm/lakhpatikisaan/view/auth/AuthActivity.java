package com.nrlm.lakhpatikisaan.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.MasterDataResponseBean;
import com.nrlm.lakhpatikisaan.repository.MasterDataRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        try {
            LogRequestBean logRequestBean=new LogRequestBean();
            logRequestBean.setImei_no("111");
            logRequestBean.setDevice_name("111");
            logRequestBean.setUser_id("HRKSVISHAKHA");
            logRequestBean.setState_short_name("hr");
            logRequestBean.setLocation_coordinate("111");
            MasterDataRepo.getInstance(AppExecutor.getInstance().networkIO()).makeMasterDataRequest(logRequestBean, new RepositoryCallback<MasterDataResponseBean>() {
                @Override
                public void onComplete(Result<MasterDataResponseBean> result) {
                    AppUtils.getInstance().showLog(result.toString(),AuthActivity.class);
                }
            });
        }catch (Exception e){
            AppUtils.getInstance().showLog("Exception"+e,AuthActivity.class);
        }
    }
}
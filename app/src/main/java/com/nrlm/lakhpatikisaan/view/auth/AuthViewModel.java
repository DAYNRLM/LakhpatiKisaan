package com.nrlm.lakhpatikisaan.view.auth;

import androidx.lifecycle.ViewModel;

import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;
import com.nrlm.lakhpatikisaan.repository.LoginRepo;
import com.nrlm.lakhpatikisaan.repository.RepositoryCallback;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class AuthViewModel extends ViewModel {

    private LoginRepo loginRepo;
    public AuthViewModel (){
        loginRepo=LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor());

    }

    public void makeLogin(LoginRequestBean loginRequestBean){
        loginRepo.makeLoginRequest(loginRequestBean, new RepositoryCallback<LoginResponseBean>() {
            @Override
            public void onComplete(Result<LoginResponseBean> result) {
                if (result instanceof Result.Success){

                    AppUtils.getInstance().showLog(result.toString(), AuthViewModel.class);
                }else {
                    AppUtils.getInstance().showLog(result.toString(),AuthViewModel.class);
                }
            }
        });
    }
}

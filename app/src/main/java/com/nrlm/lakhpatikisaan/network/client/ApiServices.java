package com.nrlm.lakhpatikisaan.network.client;

import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.DeleteShgRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.OtpRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.ResetPasswordBean;
import com.nrlm.lakhpatikisaan.network.model.request.SeccRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

     @POST("login")
    Call<JsonObject> loginApi(@Body LoginRequestBean loginRequestBean);

    @POST("masterdata")
    Call<JsonObject> masterDataApi(@Body LogRequestBean logRequestBean);

    @POST("masteractivity")
    Call<JsonObject> supportiveMasterDataApi(@Body LogRequestBean logRequestBean);

    @POST("checkDuplicay")
    Call<JsonObject> checkDuplicateDataApi(@Body CheckDuplicateRequestBean checkDuplicateRequestBean);


    @POST("addmemberdata")
    Call<JsonObject> syncEntriesDataApi(@Body SyncEntriesRequestBean syncEntriesRequestBean);
    

    @POST("forgotPassword")
    Call<JsonObject> otpApi(@Body OtpRequestBean otpRequestBean);

    @POST("resetPassword")
    Call<JsonObject> resetPasswordApi(@Body ResetPasswordBean resetPasswordBean);

    @POST("unassignShgWeb")
    Call<JsonObject> checkDeleteShgApi(@Body LogRequestBean logRequestBean);

    @POST("deleteShg")
    Call<JsonObject> deleteShgApi(@Body DeleteShgRequestBean deleteShgRequestBean);

    @POST("seccdata")
    Call<JsonObject> seccDataApi(@Body SeccRequestBean seccRequestBean);

}

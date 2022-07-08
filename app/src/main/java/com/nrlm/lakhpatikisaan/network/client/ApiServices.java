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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("login")
    Call<JsonObject> loginApi(@Body JsonObject encryptedObject);

    @POST("masterdata")
    Call<JsonObject> masterDataApi(@Body JsonObject logRequestBean);

    @POST("masteractivity")
    Call<JsonObject> supportiveMasterDataApi(@Body JsonObject logRequestBean);

    @POST("checkDuplicay")
    Call<JsonObject> checkDuplicateDataApi(@Body JsonObject checkDuplicateRequestBean);

//Change here addmemberdata to addshgmemberdata
    @POST("addshgmemberdata")
    Call<JsonObject> syncEntriesDataApi(@Body JsonObject syncEntriesRequestBean);
    

    @POST("forgotPassword")
    Call<JsonObject> otpApi(@Body JsonObject otpRequestBean);

    @POST("resetPassword")
    Call<JsonObject> resetPasswordApi(@Body JsonObject resetPasswordBean);

    @POST("unassignShgWeb")
    Call<JsonObject> checkDeleteShgApi(@Body JsonObject logRequestBean);

    @POST("deleteShg")
    Call<JsonObject> deleteShgApi(@Body JsonObject deleteShgRequestBean);

    @POST("seccdata")
    Call<JsonObject> seccDataApi(@Body JsonObject seccRequestBean);

    @POST("inactivememberdata")
    Call<JsonObject> inActiveApi(@Body JsonObject seccRequestBean);


}

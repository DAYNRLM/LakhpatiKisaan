package com.nrlm.lakhpatikisaan.network.client;

import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.model.request.CheckDuplicateRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LogRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.SyncEntriesRequestBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

     @POST("login")
    Call<JsonObject> loginApi(@Body LoginRequestBean loginRequestBean);

    @POST("masterdata")
    Call<JsonObject> masterDataApi(@Body LogRequestBean logRequestBean);

    @POST("supportivemasterdata")
    Call<JsonObject> supportiveMasterDataApi(@Body LogRequestBean logRequestBean);

    @POST("checkduplicacy")
    Call<JsonObject> checkDuplicateDataApi(@Body CheckDuplicateRequestBean checkDuplicateRequestBean);

    @POST("addmemberdata")
    Call<JsonObject> syncEntriesDataApi(@Body SyncEntriesRequestBean syncEntriesRequestBean);
}

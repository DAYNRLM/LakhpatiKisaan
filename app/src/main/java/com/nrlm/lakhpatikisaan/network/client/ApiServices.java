package com.nrlm.lakhpatikisaan.network.client;

import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.model.request.ContactsRequestBean;
import com.nrlm.lakhpatikisaan.network.model.request.LoginRequestBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("mmu/detail")
    Call<JsonObject> getConTacts(@Body ContactsRequestBean contactsRequestBean);

    @POST("login")
    Call<JsonObject> loginRequest(@Body LoginRequestBean loginRequestBean);
}

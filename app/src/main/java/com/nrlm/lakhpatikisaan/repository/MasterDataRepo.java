package com.nrlm.lakhpatikisaan.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nrlm.lakhpatikisaan.network.client.ApiServices;
import com.nrlm.lakhpatikisaan.network.client.Result;
import com.nrlm.lakhpatikisaan.network.client.RetrofitClient;
import com.nrlm.lakhpatikisaan.network.model.request.ContactsRequestBean;
import com.nrlm.lakhpatikisaan.network.model.response.ContactsResponseBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataRepo {



    private void getMasters(ContactsRequestBean contactsRequestBean) {


        ApiServices apiServices = RetrofitClient.getApiServices();
        Call<JsonObject> call = (Call<JsonObject>) apiServices.getConTacts(contactsRequestBean);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String string = response.body().toString();
                    if (string.contains("data")) {
                        ContactsResponseBean contactsResponseBean = new Gson().fromJson(response.body(), ContactsResponseBean.class);
                        Log.d("DataResponse", contactsResponseBean.toString());
                       // new Result.Success<ContactsResponseBean>(contactsResponseBean);

                    } else {
                        Log.d("ErrorResponse", response.toString());
                       // new Result.Error<>(new Exception("no data in response"));
                        }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Failure", t.toString());
               // new Result.Error<Throwable>(t);
            }
        });
    }
}

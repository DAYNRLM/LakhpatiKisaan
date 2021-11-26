package com.nrlm.lakhpatikisaan.network.client;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String HTTP_TYPE = "https";
    public static final String IP_ADDRESS = "nrlm.gov.in";
    public static final String NRLM_STATUS = "nrlmwebservicedemo";

    public static String BASE_URL = HTTP_TYPE + "://" + IP_ADDRESS + "/" + NRLM_STATUS + "/services/";

    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 1000;


    private static Gson getGson() {
        Gson gson = new Gson();
        gson.serializeNulls();
        return gson;
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(getHttpLoggingInterceptor()).build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(okHttpClient)
            .build();

    private static ApiServices apiServices = retrofit.create(ApiServices.class);

    public static ApiServices getApiServices() {
        return apiServices;
    }
}

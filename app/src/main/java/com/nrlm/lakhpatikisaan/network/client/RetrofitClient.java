package com.nrlm.lakhpatikisaan.network.client;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.nrlm.lakhpatikisaan.utils.AppConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

   //private static final String server = "local";

  //   private static final String server ="demo";
  private static final String server ="live";

    private static final int CONNECTION_TIMEOUT = 6000;
    private static final int READ_TIMEOUT = 6000;


    private static String getBaseUrl(String server) {


        String baseURL = "";
        String HTTP_TYPE, IP_ADDRESS, NRLM_STATUS;
        switch (server) {
            case "local":
                HTTP_TYPE = "http";
                IP_ADDRESS = "10.197.183.105";
                NRLM_STATUS = ":8989";
                baseURL = HTTP_TYPE + "://" + IP_ADDRESS + NRLM_STATUS + "/lakhpatishg/";
                break;

            case "demo":
                HTTP_TYPE = "https";
                IP_ADDRESS = "nrlm.gov.in";
                NRLM_STATUS = "lakhpatishgDemo";
                baseURL = HTTP_TYPE + "://" + IP_ADDRESS + "/" + NRLM_STATUS + "/lakhpatishg/";
                break;

            case "live":
                HTTP_TYPE = "https";
                IP_ADDRESS = "nrlm.gov.in";
                NRLM_STATUS = "lakhpatishg";
                baseURL = HTTP_TYPE + "://" + IP_ADDRESS + "/" + NRLM_STATUS + "/lakhpatishg/";
                break;
        }
        return baseURL;
    }


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


    private static OkHttpClient getOkHttpClient(int connTimeout,int readTimeout,boolean retryOnFailure){
        OkHttpClient okHttpClient;
        if (AppConstant.wantToShow){
            okHttpClient= new OkHttpClient.Builder()
                    .connectTimeout(connTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)

                    .retryOnConnectionFailure(retryOnFailure)
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {

                            return chain.proceed(chain.request().newBuilder()
                                    .header("securityToken", "n{j5Y[<!Ps*HWCWg").build());
                        }
                    })
                    .addInterceptor(getHttpLoggingInterceptor()).build();
        }else {
            okHttpClient= new OkHttpClient.Builder()
                    .connectTimeout(connTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(retryOnFailure)
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {

                            return chain.proceed(chain.request().newBuilder()
                                    .header("securityToken", "n{j5Y[<!Ps*HWCWg")
                                    .addHeader("Connection","close").build());
                        }
                    })
               .build();
        }

        return okHttpClient;
    }



    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getBaseUrl(RetrofitClient.server))
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(getOkHttpClient(RetrofitClient.CONNECTION_TIMEOUT,RetrofitClient.READ_TIMEOUT,true))
            .build();

    private static ApiServices apiServices = retrofit.create(ApiServices.class);

    public static ApiServices getApiServices() {
        return apiServices;
    }
}

package com.gk.myapp.interfaces;

import com.gk.myapp.utils.C;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gk on 19-02-2017.
 */
public class  ApiClient {
    public static final String BASE_URL = "http://www.tiwana.in/admin/v1/"; //new 10jUly
    //    public static final String BASE_URL = "http://tiwana.in/admin/admin/tiwana/"; //old
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(C.API_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(C.API_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(C.API_TIMEOUT, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

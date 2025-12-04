package com.example.duan1.utils;

import android.util.Log;

import com.example.duan1.model.Product;
import com.example.duan1.model.ProductTypeAdapter;
import com.example.duan1.services.ApiServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static RetrofitClient instance = null;
    private ApiServices apiServices;

    private RetrofitClient() {
        // Tạo HttpLoggingInterceptor để debug API calls
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Tạo OkHttpClient với interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Tạo Gson với custom TypeAdapter cho Product
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Product.class, new ProductTypeAdapter())
                .create();

        // Tạo Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServices.Url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        
        apiServices = retrofit.create(ApiServices.class);
        
        Log.d(TAG, "RetrofitClient initialized with base URL: " + ApiServices.Url);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiServices getApiServices() {
        return apiServices;
    }
}


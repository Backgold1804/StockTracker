package com.example.stocktracker;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
//                .baseUrl("http://13.124.91.123/")
                .baseUrl("http://192.168.219.104/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}

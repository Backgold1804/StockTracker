package com.example.stocktracker;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://3.39.100.254//")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}

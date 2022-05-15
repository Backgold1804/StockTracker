package com.example.stocktracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST("/login")
    Call<Data> getUser(@Query("id") String id, @Query("password") String password);

    @POST("/join")
    Call<Data> setUser(@Query("id") String id,
                       @Query("password") String password,
                       @Query("phone") String phone,
                       @Query("findId") String findId,
                       @Query("nickname") String nickname);

    @GET("/findId")
    Call<Data> findId(@Query("phone") String phone, @Query("findId") String findId);

    @GET("/findPassword")
    Call<Data> findPassword(@Query("id") String id, @Query("phone") String phone, @Query("findId") String findId);
}

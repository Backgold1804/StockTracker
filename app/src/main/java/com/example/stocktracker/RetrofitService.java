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

    @GET("/selectStockList")
    Call<ListData> selectStockList(@Query("cust_uid") int cust_uid);

    @GET("/selectTradingList")
    Call<ListData> selectTradingList(@Query("my_stock_uid") int my_stock_uid);

    @GET("/getStockList")
    Call<ListData> getStockList();

    @POST("/putNewStock")
    Call<Data> putNewStock(@Query("cust_uid") int cust_uid,
                        @Query("stock_name") String stock_name,
                        @Query("price") int price,
                        @Query("amount") int amount,
                        @Query("date") String date,
                        @Query("time") String time);

    @POST("/putTrading")
    Call<Data> putTrading(@Query("cust_uid") int cust_uid,
                           @Query("stock_name") String stock_name,
                           @Query("trading") String trading,
                           @Query("price") int price,
                           @Query("amount") int amount,
                           @Query("date") String date,
                           @Query("time") String time);

    @GET("/deleteUser")
    Call<Data> deleteUser(@Query("uid") int uid);

    @GET("/setUser")
    Call<Data> setUser(@Query("uid") int uid);

    @POST("/updateUser")
    Call<Data> updateUser(@Query("uid") int id,
                       @Query("password") String password,
                       @Query("phone") String phone,
                       @Query("find_id") String findId,
                       @Query("nickname") String nickname);

    @POST("/addFriend")
    Call<Data> addFriend(@Query("uid") int uid, @Query("nickname") String nickname);

    @GET("/selectFriend")
    Call<ListData> selectFriend(@Query("uid") int uid);

    @GET("/deleteFriend")
    Call<Data> deleteFriend(@Query("uid") int uid, @Query("nickname") String nickname);

    @GET("/deleteTrading")
    Call<Data> deleteTrading(@Query("uid") int uid);
}

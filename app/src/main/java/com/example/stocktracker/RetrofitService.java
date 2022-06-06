package com.example.stocktracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {

    //  로그인
    @POST("/login")
    Call<Data> getUser(@Query("id") String id, @Query("password") String password);

    //  회원가입
    @POST("/join")
    Call<Data> setUser(@Query("id") String id,
                       @Query("password") String password,
                       @Query("phone") String phone,
                       @Query("findId") String findId,
                       @Query("nickname") String nickname);

    //  아이디 찾기
    @GET("/findId")
    Call<Data> findId(@Query("phone") String phone, @Query("findId") String findId);

    //  비밀번호 찾기
    @GET("/findPassword")
    Call<Data> findPassword(@Query("id") String id, @Query("phone") String phone, @Query("findId") String findId);

    //  User가 들고 있는 종목 내역 가져오기
    @GET("/selectStockList")
    Call<ListData> selectStockList(@Query("cust_uid") int cust_uid);

    //  User가 들고 있는 종목의 매매내역 가져오기
    @GET("/selectTradingList")
    Call<ListData> selectTradingList(@Query("my_stock_uid") int my_stock_uid);

    //  전체 종목 이름 가져오기
    @GET("/getStockList")
    Call<ListData> getStockList();

    //  User에 새로운 종목 추가하기
    @POST("/putNewStock")
    Call<Data> putNewStock(@Query("cust_uid") int cust_uid,
                        @Query("stock_name") String stock_name,
                        @Query("price") int price,
                        @Query("amount") int amount,
                        @Query("date") String date,
                        @Query("time") String time);

    //  매매내역 추가하기
    @POST("/putTrading")
    Call<Data> putTrading(@Query("cust_uid") int cust_uid,
                           @Query("stock_name") String stock_name,
                           @Query("trading") String trading,
                           @Query("price") int price,
                           @Query("amount") int amount,
                           @Query("date") String date,
                           @Query("time") String time);

    //  회원 탈퇴
    @GET("/deleteUser")
    Call<Data> deleteUser(@Query("uid") int uid);

    //  User 정보 고치기 전 정보 불러오기
    @GET("/setUser")
    Call<Data> setUser(@Query("uid") int uid);

    //  User 정보 고치기
    @POST("/updateUser")
    Call<Data> updateUser(@Query("uid") int id,
                       @Query("password") String password,
                       @Query("phone") String phone,
                       @Query("find_id") String findId,
                       @Query("nickname") String nickname);

    //  친구 추가
    @POST("/addFriend")
    Call<Data> addFriend(@Query("uid") int uid, @Query("nickname") String nickname);

    //  친구 조회
    @GET("/selectFriend")
    Call<ListData> selectFriend(@Query("uid") int uid);

    //  친구 삭제
    @GET("/deleteFriend")
    Call<Data> deleteFriend(@Query("uid") int uid, @Query("friend_uid") int friend_uid);

    //  매매내역 삭제
    @GET("/deleteTrading")
    Call<Data> deleteTrading(@Query("uid") int uid);

    //  친구와 내 종목 불러오기
    @GET("/selectFriendStockList")
    Call<ListData> selectFriendStockList(@Query("uid") int uid);
}

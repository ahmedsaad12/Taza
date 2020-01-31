package com.elkhelj.taza.services;

import com.elkhelj.taza.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {
    
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("kayWord") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("name") String name,
                           @Field("shop_name") String shop_name,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("phone") String phone,
                           @Field("phone_code") String phone_code,

                           @Field("type") String type,
                           @Field("shop_for") String shop_for,
                           @Field("country") String country,
                           @Field("city") String city

    );

   
}
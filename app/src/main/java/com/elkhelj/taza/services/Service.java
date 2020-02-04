package com.elkhelj.taza.services;

import com.elkhelj.taza.models.App_Data_Model;
import com.elkhelj.taza.models.NotificationDataModel;
import com.elkhelj.taza.models.Order_Model;
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
    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("subject") String subject,
                                   @Field("message") String message
    );
    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> Logout(@Field("id") String id

    );
    @GET("api/condtions")
    Call<App_Data_Model> getterms();

    @GET("api/aboutUs")
    Call<App_Data_Model> getabout();
    @FormUrlEncoded
    @POST("api/my_notification")
    Call<List<NotificationDataModel>> getNotifications(@Field("user_id") int user_id
    );
    @FormUrlEncoded
    @POST("api/my_orders")
    Call<List<Order_Model>> getMyAds(
            @Field("user_id") String user_id,
            @Field("type") String type


    );

   
}
package com.elkhelj.karam.services;

import com.elkhelj.karam.models.Add_Order_Model;
import com.elkhelj.karam.models.App_Data_Model;
import com.elkhelj.karam.models.Catogries_Model;
import com.elkhelj.karam.models.Cities_Model;
import com.elkhelj.karam.models.NotificationDataModel;
import com.elkhelj.karam.models.Order_Model;
import com.elkhelj.karam.models.PlaceGeocodeData;
import com.elkhelj.karam.models.PlaceMapDetailsData;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;

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
import retrofit2.http.Query;

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
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("phone") String phone,
                           @Field("phone_code") String phone_code,

                           @Field("type") String type,
                           @Field("city") String city,
                           @Field("is_agree") String is_agree,
                           @Field("notion_id") String notion_id


    );

    @Multipart
    @POST("api/register")
    Call<UserModel> signUp(@Part("name") RequestBody name,
                           @Part("email") RequestBody email,
                           @Part("password") RequestBody password,
                           @Part("phone") RequestBody phone,
                           @Part("phone_code") RequestBody phone_code,
                           @Part("type") RequestBody type,
                           @Part("city") RequestBody city,
                           @Part("is_agree") RequestBody is_agree,
                           @Part("notion_id") RequestBody notion_id,
                           @Part("shop_name") RequestBody shop_name,
                           @Part MultipartBody.Part image);
    @Multipart
    @POST("api/register")
    Call<UserModel> signUp(@Part("name") RequestBody name,
                           @Part("email") RequestBody email,
                           @Part("password") RequestBody password,
                           @Part("phone") RequestBody phone,
                           @Part("phone_code") RequestBody phone_code,
                           @Part("type") RequestBody type,
                           @Part("city") RequestBody city,
                           @Part("is_agree") RequestBody is_agree,
                           @Part("notion_id") RequestBody notion_id,
                           @Part MultipartBody.Part image);

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
    Call<List<App_Data_Model>> getterms();

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

    @GET("api/all_categories")
    Call<List<Catogries_Model>> getDepartment(


    );

    @GET("api/height_sel_products")
    Call<List<Product_Model>> getAds(


    );
    @FormUrlEncoded
    @POST("api/filter_by_cat")
    Call<List<Product_Model>> getAdss(
            @Field("category_id") String category_id
    );
    @FormUrlEncoded
    @POST("api/all_Subscription")
    Call<List<Product_Model>> getSubscribe(
            @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/my_subscribs")
    Call<List<Product_Model>> getmySubscribe(
            @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/add_subscribs")
    Call<ResponseBody> setSubscribe(
            @Field("user_id") String user_id,
            @Field("product_id") String product_id

    );

    @FormUrlEncoded
    @POST("api/accept_order")
    Call<ResponseBody> Accept(
            @Field("user_id") String user_id,
            @Field("order_id") String order_id

    );

    @FormUrlEncoded
    @POST("api/canceling_order")
    Call<ResponseBody> Cancell(
            @Field("user_id") String user_id,
            @Field("order_id") String order_id

    );

    @FormUrlEncoded
    @POST("api/search")
    Call<List<Product_Model>> getAds(
            @Field("key_word") String key_word

    );

    @GET("api/all_cities")
    Call<List<Cities_Model>> getAllCities();

    @FormUrlEncoded
    @POST("api/single_product")
    Call<Product_Model> getSingleAds(

            @Field("product_id") String product_id

    );

    @POST("api/add_order")
    Call<ResponseBody> accept_orders(@Body Add_Order_Model add_order_model);
    @FormUrlEncoded
    @POST("api/add_oute_order")
    Call<ResponseBody> accept_orders(@Field("name") String name,
                                @Field("des") String des,

                                @Field("amount") String amount,
                                @Field("user_id") int user_id
    );
    @FormUrlEncoded
    @POST("api/update_profile")
    Call<UserModel> editprofile(@Field("name") String name,
                                @Field("mobile") String mobile,

                                @Field("email") String email,
                                @Field("city") String city,
                                @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/update_profile")
    Call<UserModel> editprofile(@Field("password") String name,

                                @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/canRest")
    Call<UserModel> forget(@Field("kayWord") String kayWord
    );
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
}
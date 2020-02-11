package com.elkhelj.karam.activities_fragments.activity_my_order_detials;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.models.Order_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.ActivityOrderDetialsBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private int NUM_PAGES,current_page=0;
private int amount=1;
private int totalamount;
    private Order_Model product_model;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detials);
        getdatafromintent();
        initView();
       // setdata();

//change_slide_image();


    }
    private void getdatafromintent() {
        if (getIntent().getSerializableExtra("productid") != null) {
            product_model = (Order_Model) getIntent().getSerializableExtra("productid");
        }
    }



    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");
        if(product_model!=null){
binding.setProductmodel(product_model);}
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        if(product_model!=null){
        if(product_model.getStatus().equals("0")){
if(userModel.getType().equals("1")){
    binding.btnSend.setVisibility(View.GONE);
}
}
        else {
            binding.btnSend.setVisibility(View.GONE);
            binding.btncancel.setVisibility(View.GONE);

        }}
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Accept();

            }
        });
        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancell();
            }
        });
    }

    public void Accept()

        {
            //   Common.CloseKeyBoard(homeActivity, edt_name);
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            // rec_sent.setVisibility(View.GONE);
            try {


                Api.getService( Tags.base_url)
                        .Accept(userModel.getId()+"",product_model.getId()+"")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dialog.dismiss();

                                //  binding.progBar.setVisibility(View.GONE);
                                if (response.isSuccessful() && response.body() != null ) {
                                    //binding.coord1.scrollTo(0,0);

//getsingleads();
                                    Intent intent=new Intent(OrderDetialsActivity.this, MyOrdersActivity.class);
                                    startActivity(intent);

                                } else {


                                    //Toast.makeText(activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                try {

                                    dialog.dismiss();

                                    //Toast.makeText(FollowingActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    Log.e("error", t.getMessage());
                                } catch (Exception e) {
                                }
                            }
                        });
            }catch (Exception e){
                Log.e("ffff",e.getMessage());
                dialog.dismiss();
            }


        }

    public void Cancell()

    {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .Cancell(userModel.getId()+"",product_model.getId()+"")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null ) {
                                //binding.coord1.scrollTo(0,0);

//getsingleads();
                                Intent intent=new Intent(OrderDetialsActivity.this, MyOrdersActivity.class);
                                startActivity(intent);

                            } else {


                                //Toast.makeText(activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                //Toast.makeText(FollowingActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){
            Log.e("ffff",e.getMessage());
            dialog.dismiss();
        }


    }
    @Override
    public void back() {
        finish();
    }



}

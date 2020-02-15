package com.elkhelj.karam.activities_fragments.activity_product_detials;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_add_ads.CompleteOrderActivity;
import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.databinding.ActivityAddUnkonwnBinding;
import com.elkhelj.karam.databinding.ActivityProductDetialsBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.models.Add_Order_Model;
import com.elkhelj.karam.models.Orders_Cart_Model;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUnkownActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAddUnkonwnBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private int NUM_PAGES,current_page=0;
private String product_id;
private int amount=1;
private int totalamount;
    private Product_Model product_model;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_unkonwn);
        initView();
       // setdata();

//change_slide_image();


    }



    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.imageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount>1){
                    binding.tvAmount.setText((amount-1)+"");
                    amount-=1;
                }

            }
        });
        binding.imageIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(amount<totalamount){
                binding.tvAmount.setText((amount+1)+"");
                amount+=1;
//            }
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            //    addtocart();
                checkdata();

            }
        });
    }
    private void accept_order(String name,String desc) {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(name,desc,amount+"",userModel.getUser_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                    finish();
                } else {
                    //  Common.CreateDialogAlert(CartActivity.this, getString(R.string.failed));
                    Log.e("Error_code", response.code() + "_" + response.message());

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
                    //    Toast.makeText(CompleteOrderActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void checkdata() {
        String name=binding.edtAdname.getText().toString();
        String desc=binding.edtdes.getText().toString();
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(desc)){
            binding.edtAdname.setError(null);
            binding.edtdes.setError(null);
accept_order(name,desc);
        }
        else {
            if(TextUtils.isEmpty(name)){
                binding.edtAdname.setError(getResources().getString(R.string.field_req));
            }
            else {
                binding.edtAdname.setError(null);

            }
            if(TextUtils.isEmpty(desc)){
                binding.edtdes.setError(getResources().getString(R.string.field_req));
            }
            else {
                binding.edtdes.setError(null);

            }
        }
    }


    @Override
    public void back() {
        finish();
    }



}

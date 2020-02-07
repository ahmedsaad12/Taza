package com.elkhelj.taza.activities_fragments.activity_product_detials;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.elkhelj.taza.R;
import com.elkhelj.taza.databinding.ActivityProductDetialsBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.Orders_Cart_Model;
import com.elkhelj.taza.models.Product_Model;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductDetialsBinding binding;

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
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
        getdatafromintent();
        initView();
        getSingleproduct();
       // setdata();

//change_slide_image();


    }
    private void getdatafromintent() {
        if (getIntent().getStringExtra("productid") != null) {
            product_id = getIntent().getStringExtra("productid");
        }
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


                addtocart();

            }
        });
    }



    private void addtocart() {
        List<Orders_Cart_Model> orders_cart_models;
        if(preferences.getUserOrder(this)!=null){
            orders_cart_models=preferences.getUserOrder(this);
        }
        else {
            orders_cart_models=new ArrayList<>();
        }
        Orders_Cart_Model orders_cart_model=new Orders_Cart_Model();

        orders_cart_model.setImage(product_model.getImage());
        orders_cart_model.setPrice(product_model.getPrice()+"");
        orders_cart_model.setAr_name(product_model.getAr_title());
        orders_cart_model.setEn_name(product_model.getEn_title());
        orders_cart_model.setProduct_id(product_model.getId());
        orders_cart_model.setAmount(amount);
        orders_cart_models.add(orders_cart_model);
        preferences.create_update_order(this,orders_cart_models);
        Toast.makeText(ProductDetialsActivity.this,getResources().getString(R.string.suc),Toast.LENGTH_LONG).show();
    }

    @Override
    public void back() {
        finish();
    }
    public void getSingleproduct() {
        //  binding.progBar.setVisibility(View.VISIBLE);
        Log.e("pr",product_id);
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getSingleAds(product_id)
                .enqueue(new Callback<Product_Model>() {
                    @Override
                    public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                        dialog.dismiss();
                        // binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            updateddata(response.body());

                        } else {

                        //    Toast.makeText(ProductDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product_Model> call, Throwable t) {
                        try {
dialog.dismiss();

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updateddata(Product_Model body) {
        this.product_model=body;
        binding.setProductmodel(product_model);
        totalamount=product_model.getAmount();
    }


}

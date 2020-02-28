package com.elkhelj.karam.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.elkhelj.karam.activities_fragments.activity_add_unkowon.AddUnkownActivity;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.adapters.Products_Adapter;
import com.elkhelj.karam.databinding.FragmentSearchBinding;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {

    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private int NUM_PAGES,current_page=0;
    private String product_id;
    private int amount=1;
    private int totalamount;
    private Product_Model product_model;

    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false);
        initView();

        return binding.getRoot();
    }
    private void initView() {
        activity=(HomeActivity)getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
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
                if(userModel!=null){
                    checkdata();}
                else {
                    Intent intent=new Intent(activity, SignInActivity.class);
                    startActivity(intent);
                    activity.finish();
                }

            }
        });
    }
    private void accept_order(String name,String desc) {
        Log.e("lllll",userModel.getId()+"");
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(name,desc,amount+"",userModel.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                  //  finish();
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



}

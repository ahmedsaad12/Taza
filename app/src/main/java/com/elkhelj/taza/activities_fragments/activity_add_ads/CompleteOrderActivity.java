package com.elkhelj.taza.activities_fragments.activity_add_ads;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.taza.databinding.ActivityCompleteorderBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.Add_Order_Model;
import com.elkhelj.taza.models.Order_Upload_Model;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCompleteorderBinding binding;

    private String lang;

    private Order_Upload_Model order_upload_model;
    private Preferences preferences;
    private UserModel userModel;
    private String city_id, type_id, cat_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "en")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_completeorder);
        initView();

        //getDepartments();

    }




    private void initView() {
        order_upload_model = new Order_Upload_Model();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setOrderModel(order_upload_model);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_upload_model.isDataValidStep1(CompleteOrderActivity.this)) {
                    if (userModel != null) {
                        CompleteOrderActivity.this.checkdata();
                    } else {
                        //   Common.CreateNoSignAlertDialog(this);
                    }

                }
            }
        });

    }
    private void checkdata() {
        Add_Order_Model order_model=new Add_Order_Model();
order_model.setAddress(order_upload_model.getAddress());
order_model.setName(order_upload_model.getName());
        if(preferences.getUserOrder(this)!=null){
            order_model.setUser_id(userModel.getId());
            order_model.setOrder_detials(preferences.getUserOrder(this));
            accept_order(order_model);

        }

    }

    private void accept_order(Add_Order_Model order_model) {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(order_model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                    Intent intent=new Intent(CompleteOrderActivity.this, MyOrdersActivity.class);
                    startActivity(intent);
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



    @Override
    public void back() {
        finish();
    }


}

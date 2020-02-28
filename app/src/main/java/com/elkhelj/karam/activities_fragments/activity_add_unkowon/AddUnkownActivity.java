package com.elkhelj.karam.activities_fragments.activity_add_unkowon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_add_ads.CompleteOrderActivity;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Search;
import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.adapters.Products_Adapter;
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
    private GridLayoutManager manager;
    private UserModel userModel;

    private Products_Adapter ads_adapter;
    private List<Product_Model> advesriment_data_list;
    private String query;
    private String lang;

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



    private void initView() {
        advesriment_data_list = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setBackListener(this);
        manager = new GridLayoutManager(this,2);
        ads_adapter = new Products_Adapter(advesriment_data_list, this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
//       binding.progBar.setVisibility(View.GONE);
        binding.llNoStore.setVisibility(View.GONE);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(ads_adapter);
        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query = binding.edtSearch.getText().toString();
                    if (!TextUtils.isEmpty(query)) {
                        Common.CloseKeyBoard(AddUnkownActivity.this, binding.edtSearch);
                  getAds(query);
                        return false;
                    } else {
                        binding.edtSearch.setError(getResources().getString(R.string.field_req));
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void getAds(String query) {
        advesriment_data_list.clear();
        ads_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getAds(query)
                    .enqueue(new Callback<List<Product_Model>>() {
                        @Override
                        public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("Error_code", response.code() + "_" + response.body().toString());

                                advesriment_data_list.clear();
                                advesriment_data_list.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoStore.setVisibility(View.GONE);
                                    ads_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    ads_adapter.notifyDataSetChanged();

                                    binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {
                                ads_adapter.notifyDataSetChanged();

                                binding.llNoStore.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);


                                //     Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void back() {
        finish();
    }


    public void showdetials(int id) {
        Intent intent=new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("productid",id+"");
        startActivity(intent);
    }
}

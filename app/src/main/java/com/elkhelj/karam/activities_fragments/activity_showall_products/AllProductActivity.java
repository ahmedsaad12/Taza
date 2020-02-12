package com.elkhelj.karam.activities_fragments.activity_showall_products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elkhelj.karam.R;

import com.elkhelj.karam.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.elkhelj.karam.adapters.Products_Adapter;
import com.elkhelj.karam.databinding.ActivityMarketsBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketsBinding binding;

    private Preferences preferences;
    private UserModel userModel;

    private Products_Adapter ads_adapter;
    private List<Product_Model> advesriment_data_list;
    private String lang;
    private String search_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_markets);
        getdatafromintent();

        initView();

    }
    private void getAds() {
        advesriment_data_list.clear();
        ads_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getAdss(search_id)
                    .enqueue(new Callback<List<Product_Model>>() {
                        @Override
                        public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
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

    private void getdatafromintent() {
        if(getIntent().getIntExtra("search",-1)!=0){
            search_id=getIntent().getIntExtra("search",-1)+"";
        }
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        advesriment_data_list = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
binding.progBar.setVisibility(View.GONE);
        ads_adapter = new Products_Adapter(advesriment_data_list, this);
        binding.recMarket.setItemViewCacheSize(25);
        binding.recMarket.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recMarket.setDrawingCacheEnabled(true);
//       binding.progBar.setVisibility(View.GONE);
        binding.llNoStore.setVisibility(View.GONE);

        binding.recMarket.setAdapter(ads_adapter);
binding.setLang(lang);
binding.setBackListener(this);

    }

    public void showdetials(int id) {
        Intent intent=new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("productid",id+"");
        startActivity(intent);


    }



    @Override
    public void back() {
        finish();
    }
}

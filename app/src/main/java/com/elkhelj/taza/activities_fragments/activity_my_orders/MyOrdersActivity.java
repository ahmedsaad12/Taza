package com.elkhelj.taza.activities_fragments.activity_my_orders;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.elkhelj.taza.R;
import com.elkhelj.taza.adapters.Order_Adapter;
import com.elkhelj.taza.databinding.ActivityMyOrdersBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.Order_Model;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.elkhelj.taza.adapters.My_Orders_Adapter;

public class MyOrdersActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMyOrdersBinding binding;
    private String lang;
    private Order_Adapter ads_adapter;
    private List<Order_Model> advesriment_data_list;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page2 = 1;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "en")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        initView();
        if(userModel!=null){
        getAds();
            }

    }

    private void initView() {
        advesriment_data_list=new ArrayList<>();
        preferences= Preferences.newInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        ads_adapter = new Order_Adapter(advesriment_data_list,this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.llNoStore.setVisibility(View.GONE);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAds();
            }
        });

        //binding.recView.setAdapter(ads_adapter);

    }
    public void getAds() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        advesriment_data_list.clear();
        ads_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .getMyAds(userModel.getId()+"",userModel.getType()+"")
                    .enqueue(new Callback<List<Order_Model>>() {
                        @Override
                        public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                            binding.swipeRefresh.setRefreshing(false);

                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
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
                        public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                            try {
                                binding.swipeRefresh.setRefreshing(false);

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);


                           //     Toast.makeText(MyOrdersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void back() {
        finish();
    }
}

package com.elkhelj.karam.activities_fragments.activity_home.fragments;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.elkhelj.karam.adapters.Catogry_Adapter;
import com.elkhelj.karam.models.Catogries_Model;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.adapters.Products_Adapter;
import com.elkhelj.karam.databinding.FragmentMainBinding;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private static Dialog dialog;
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager2;
    private GridLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    //
    private List<Catogries_Model> dataList;
    private Catogry_Adapter catogries_adapter;
    private Products_Adapter ads_adapter;
    private List<Product_Model> advesriment_data_list;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();

        getDepartments();

        getAds();

        return binding.getRoot();
    }


    private void initView() {
        dataList = new ArrayList<>();
        advesriment_data_list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);


        binding.progBar2.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setNestedScrollingEnabled(false);
        manager = new GridLayoutManager(activity, 2);
        manager2 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        binding.recView.setLayoutManager(manager);
        binding.recViewCategory.setLayoutManager(manager2);
        catogries_adapter = new Catogry_Adapter(dataList, activity, this);
        binding.recViewCategory.setItemViewCacheSize(25);
        binding.recViewCategory.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewCategory.setDrawingCacheEnabled(true);

        binding.recViewCategory.setAdapter(catogries_adapter);
        ads_adapter = new Products_Adapter(advesriment_data_list, activity);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
//       binding.progBar.setVisibility(View.GONE);
        binding.llNoStore.setVisibility(View.GONE);

        binding.recView.setAdapter(ads_adapter);
        binding.recView.setNestedScrollingEnabled(true);

//


    }

    private void getAds() {
        advesriment_data_list.clear();
        ads_adapter.notifyDataSetChanged();
        binding.progBar2.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getAds()
                    .enqueue(new Callback<List<Product_Model>>() {
                        @Override
                        public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                            binding.progBar2.setVisibility(View.GONE);
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
                                binding.progBar2.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);


                                //     Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar2.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }


    //
    public void getDepartments() {
        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<List<Catogries_Model>>() {
                    @Override
                    public void onResponse(Call<List<Catogries_Model>> call, Response<List<Catogries_Model>> response) {
                        //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {

                            dataList.addAll(response.body());
                            if (response.body().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                catogries_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                //  ll_no_order.setVisibility(View.VISIBLE);

                            }
                        } else {

                            //     Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Catogries_Model>> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


}

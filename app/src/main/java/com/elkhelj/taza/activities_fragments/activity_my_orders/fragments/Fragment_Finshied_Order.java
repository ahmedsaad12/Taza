package com.elkhelj.taza.activities_fragments.activity_my_orders.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;


import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.taza.adapters.Order_Adapter;
import com.elkhelj.taza.databinding.FragmentOrdersBinding;
import com.elkhelj.taza.models.Order_Model;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Finshied_Order extends Fragment {
    private MyOrdersActivity activity;
    private FragmentOrdersBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;
    private Order_Adapter adapter;
    private List<Order_Model> orderModelList;
    private LinearLayoutManager manager;
    private boolean isLoading = false;

    public static Fragment_Finshied_Order newInstance()
    {
        return new Fragment_Finshied_Order();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        orderModelList = new ArrayList<>();
        activity = (MyOrdersActivity) getActivity();
        userModel = preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new Order_Adapter(orderModelList,activity);
        binding.recView.setAdapter(adapter);

        getAds();
    }


    public void getAds() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        orderModelList.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .getMyAds(userModel.getId()+"",userModel.getType()+"")
                    .enqueue(new Callback<List<Order_Model>>() {
                        @Override
                        public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {

                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                orderModelList.clear();
                                orderModelList.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);

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

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);


                                //     Toast.makeText(MyOrdersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }

  
}
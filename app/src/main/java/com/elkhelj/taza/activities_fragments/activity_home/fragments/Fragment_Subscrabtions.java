package com.elkhelj.taza.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.taza.adapters.Products_Adapter;
import com.elkhelj.taza.adapters.SubScribe_Adapter;
import com.elkhelj.taza.databinding.FragmentSubscrabtionBinding;
import com.elkhelj.taza.models.Product_Model;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Subscrabtions extends Fragment {

    private HomeActivity activity;
    private FragmentSubscrabtionBinding binding;
    private LinearLayoutManager manager;
   private List<Product_Model> product_models;
    private SubScribe_Adapter subScribe_adapter;
    private Preferences preferences;
    private UserModel userModel;

    public static Fragment_Subscrabtions newInstance() {
        return new Fragment_Subscrabtions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscrabtion,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        product_models=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences=Preferences.newInstance();
        userModel=preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        subScribe_adapter = new SubScribe_Adapter(product_models, activity,this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
//       binding.progBar.setVisibility(View.GONE);
        binding.llNoProduct.setVisibility(View.GONE);

        binding.recView.setAdapter(subScribe_adapter);
getAds();



    }
    private void getAds() {
        product_models.clear();
        subScribe_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getSubscribe()
                    .enqueue(new Callback<List<Product_Model>>() {
                        @Override
                        public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                product_models.clear();
                                product_models.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoProduct.setVisibility(View.GONE);
                                    subScribe_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    subScribe_adapter.notifyDataSetChanged();

                                    binding.llNoProduct.setVisibility(View.VISIBLE);

                                }
                            } else {
                                subScribe_adapter.notifyDataSetChanged();

                                binding.llNoProduct.setVisibility(View.VISIBLE);

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
                                binding.llNoProduct.setVisibility(View.VISIBLE);


                                //     Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoProduct.setVisibility(View.VISIBLE);

        }
    }


    public void setsubscribe(int layoutPosition) {

      {
            //   Common.CloseKeyBoard(homeActivity, edt_name);
            final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            // rec_sent.setVisibility(View.GONE);
            try {


                Api.getService( Tags.base_url)
                        .setSubscribe(userModel.getId()+"",product_models.get(layoutPosition).getId()+"")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dialog.dismiss();

                                //  binding.progBar.setVisibility(View.GONE);
                                if (response.isSuccessful() && response.body() != null ) {
                                    //binding.coord1.scrollTo(0,0);

//getsingleads();




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


    }}
}

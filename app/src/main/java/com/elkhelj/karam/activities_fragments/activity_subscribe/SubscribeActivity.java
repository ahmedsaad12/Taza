package com.elkhelj.karam.activities_fragments.activity_subscribe;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.adapters.SubScribe_Adapter;
import com.elkhelj.karam.databinding.ActivityNotificationBinding;
import com.elkhelj.karam.language.LanguageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityNotificationBinding binding;
    private LinearLayoutManager manager;
    private List<Product_Model> product_models;
    private SubScribe_Adapter subScribe_adapter;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
    }


    private void initView() {
        product_models = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        subScribe_adapter = new SubScribe_Adapter(product_models, this, null);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
//       binding.progBar.setVisibility(View.GONE);
        binding.tvNoNotification.setVisibility(View.GONE);

        binding.recView.setAdapter(subScribe_adapter);
        getAds();


    }

    private void getAds() {
        product_models.clear();
        subScribe_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getmySubscribe(userModel.getId() + "")
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

                                    binding.tvNoNotification.setVisibility(View.GONE);
                                    subScribe_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    subScribe_adapter.notifyDataSetChanged();

                                    binding.tvNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                subScribe_adapter.notifyDataSetChanged();

                                binding.tvNoNotification.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                binding.tvNoNotification.setVisibility(View.VISIBLE);


                                //     Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoNotification.setVisibility(View.VISIBLE);

        }
    }

    public void setsubscribe(final int layoutPosition) {

        {
            //   Common.CloseKeyBoard(homeActivity, edt_name);
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            // rec_sent.setVisibility(View.GONE);
            try {


                Api.getService(Tags.base_url)
                        .setSubscribe(userModel.getId() + "", product_models.get(layoutPosition).getId() + "")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dialog.dismiss();

                                //  binding.progBar.setVisibility(View.GONE);
                                if (response.isSuccessful() && response.body() != null) {
                                    //binding.coord1.scrollTo(0,0);

//getsingleads();
                                    product_models.remove(layoutPosition);
                                    subScribe_adapter.notifyDataSetChanged();
                                } else {


                                    //Toast.makeText(this.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
            } catch (Exception e) {
                Log.e("ffff", e.getMessage());
                dialog.dismiss();
            }


        }
    }


    @Override
    public void back() {
        finish();
    }
}

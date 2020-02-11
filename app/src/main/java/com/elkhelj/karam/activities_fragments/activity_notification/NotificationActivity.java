package com.elkhelj.karam.activities_fragments.activity_notification;

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

import com.elkhelj.karam.models.NotificationDataModel;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.adapters.NotificationAdapter;
import com.elkhelj.karam.databinding.ActivityNotificationBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityNotificationBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<NotificationDataModel> notificationModelList;
    private NotificationAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isLoading = false;
    private int current_page = 1;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        notificationModelList = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        adapter = new NotificationAdapter(this,notificationModelList);
        binding.recView.setAdapter(adapter);

        getNotification();
    }


    public void getNotification() {

        try {

            Api.getService(Tags.base_url)
                    .getNotifications(userModel.getId())
                    .enqueue(new Callback<List<NotificationDataModel>>() {
                        @Override
                        public void onResponse(Call<List<NotificationDataModel>> call, Response<List<NotificationDataModel>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null ) {
                                notificationModelList.clear();
                                notificationModelList.addAll(response.body());
                                if (notificationModelList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoNotification.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                   // Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<NotificationDataModel>> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //    Toast.makeText(NotificationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }



    @Override
    public void back() {
        finish();
    }
}

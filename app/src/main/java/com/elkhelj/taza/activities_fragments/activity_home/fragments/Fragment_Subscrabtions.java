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
import com.elkhelj.taza.databinding.FragmentSubscrabtionBinding;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;

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
//    private List<NotificationDataModel.NotificationModel> notificationModelList;
//    private Notification_Adapter notification_adapter;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isLoading = false;
    private int current_page2 = 1;
    public static Fragment_Subscrabtions newInstance() {
        return new Fragment_Subscrabtions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscrabtion,container,false);
        initView();
//        getnotification();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences=Preferences.newInstance();
        userModel=preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
//notificationModelList=new ArrayList<>();
//notification_adapter=new Notification_Adapter(notificationModelList,activity);
//binding.recView.setAdapter(notification_adapter);




    }
//    private void getnotification() {
//        notificationModelList.clear();
//        notification_adapter.notifyDataSetChanged();
//        binding.progBar.setVisibility(View.VISIBLE);
//
//        try {
//
//
//            Api.getService(Tags.base_url)
//                    .getnotification(1, userModel.getUser().getId()+"")
//                    .enqueue(new Callback<NotificationDataModel>() {
//                        @Override
//                        public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
//                            binding.progBar.setVisibility(View.GONE);
//                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
//                                notificationModelList.clear();
//                                notificationModelList.addAll(response.body().getData());
//                                if (response.body().getData().size() > 0) {
//                                    // rec_sent.setVisibility(View.VISIBLE);
//                                    //  Log.e("data",response.body().getData().get(0).getAr_title());
//
//                                    binding.llNoNotification.setVisibility(View.GONE);
//                                    notification_adapter.notifyDataSetChanged();
//                                    //   total_page = response.body().getMeta().getLast_page();
//
//                                } else {
//                                    notification_adapter.notifyDataSetChanged();
//
//                                    binding.llNoNotification.setVisibility(View.VISIBLE);
//
//                                }
//                            } else {
//                                notification_adapter.notifyDataSetChanged();
//
//                                binding.llNoNotification.setVisibility(View.VISIBLE);
//
//                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                try {
//                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<NotificationDataModel> call, Throwable t) {
//                            try {
//                                binding.progBar.setVisibility(View.GONE);
//                                binding.llNoNotification.setVisibility(View.VISIBLE);
//
//
//                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                Log.e("error", t.getMessage());
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            binding.progBar.setVisibility(View.GONE);
//            binding.llNoNotification.setVisibility(View.VISIBLE);
//
//        }
//    }

}

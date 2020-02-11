package com.elkhelj.karam.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.elkhelj.karam.activities_fragments.activity_edit_profile.Edit_Profile_Activity;
import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.activities_fragments.activity_notification.NotificationActivity;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_subscribe.SubscribeActivity;
import com.elkhelj.karam.databinding.FragmentProfileBinding;
import com.elkhelj.karam.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (userModel != null) {
            binding.setUsermodel(userModel);
            if(!userModel.getType().equals("1")){
                binding.llsubs.setVisibility(View.GONE);
            }
        }

        binding.setLang(lang);
        binding.llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, Edit_Profile_Activity.class);
                startActivity(intent);}
                else {
                    activity.NavigateToSignInActivity();
                }
            }
        });
//        binding.llfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, FollowingActivity.class);
//                startActivity(intent);
//            }
//        });
        binding.llnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, NotificationActivity.class);
                startActivity(intent);}
                else {
activity.NavigateToSignInActivity();
                }
            }
        });
        binding.llsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, SubscribeActivity.class);
                startActivity(intent);}


                else {
                activity.NavigateToSignInActivity();
            }}
        });
        binding.lllang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.CreateLanguageDialog();
            }
        });
//        binding.lladd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, AddPoductActivity.class);
//                startActivity(intent);
//            }
//        });
//        binding.llwish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, WishlistActivity.class);
//                startActivity(intent);
//            }
//        });
        binding.llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MyOrdersActivity.class);
                startActivity(intent);
            }
        });
//        binding.llpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, MarketProfileActivity.class);
//                intent.putExtra("id", userModel.getId() + "");
//                startActivity(intent);
//                // activity.finish();
//            }
//        });
    }


}
package com.elkhelj.taza.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.taza.databinding.FragmentMoreBinding;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;
public class Fragment_More extends Fragment {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    public static Fragment_More newInstance() {

        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel = preferences.getUserData(activity);
//        if (userModel != null) {
//            // Picasso.with(activity).load(Tags.IMAGE_user_URL + userModel.getUser().getAvatar()).placeholder(R.drawable.user_profile).fit().into(binding.image);
//            // binding.tvName.setText(userModel.getUser().getName());
//        }
//        if(userModel==null){
//            // binding.tvLogout.setText(activity.getResources().getString(R.string.login));
//        }
//
//        binding.llterms.setOnClickListener(view -> {
//            Intent intent = new Intent(activity, TermsActivity.class);
//            startActivity(intent);
//        });
//
//        binding.llabout.setOnClickListener(view -> {
//            Intent intent = new Intent(activity, AboutActivity.class);
//            startActivity(intent);
//        });
//
//
//        binding.llContact.setOnClickListener(view -> {
//            Intent intent = new Intent(activity, ContactActivity.class);
//            startActivity(intent);
//        });
//
//        binding.lllgout.setOnClickListener(view -> {
//            if (userModel != null) {
//
//                activity.logout();
//            } else {
//                Common.CreateNoSignAlertDialog(activity);
//            }
//        });


    }




}


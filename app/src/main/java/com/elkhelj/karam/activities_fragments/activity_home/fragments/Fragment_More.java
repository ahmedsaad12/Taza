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


import com.elkhelj.karam.activities_fragments.activity_about.AboutActivity;
import com.elkhelj.karam.activities_fragments.activity_contact.ContactActivity;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_terms.TermsActivity;
import com.elkhelj.karam.databinding.FragmentMoreBinding;
import com.elkhelj.karam.preferences.Preferences;

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
//
        binding.llterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TermsActivity.class);
              startActivity(intent);
            }
        });

        binding.llabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
            }
        });


        binding.llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ContactActivity.class);
                startActivity(intent);
            }
        });

        binding.lllgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {

                    activity.logout();
                } else {
                    // Common.CreateNoSignAlertDialog(activity);
                }
            }
        });


    }




}


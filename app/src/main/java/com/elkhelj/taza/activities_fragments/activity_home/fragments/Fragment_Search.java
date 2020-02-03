package com.elkhelj.taza.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.taza.databinding.FragmentSearchBinding;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {

    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private Preferences preferences;
    private LinearLayoutManager manager;
    private UserModel userModel;


    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);

        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
binding.recView.setItemViewCacheSize(25);
binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
binding.recView.setDrawingCacheEnabled(true);
binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
       // getRooms();
    }
});
    }


}

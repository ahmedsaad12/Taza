package com.elkhelj.taza.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_cart.CartActivity;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_Main;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_More;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_Search;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_Subscrabtions;
import com.elkhelj.taza.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.elkhelj.taza.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.taza.databinding.ActivityHomeBinding;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;


import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Profile fragment_profile;
    private Fragment_Search fragment_search;
    private Fragment_Subscrabtions fragment_subscrabtions;
    private Fragment_More fragment_more;
    private Preferences preferences;
private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "en")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {

            displayFragmentMain();
        }


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
        binding.toolbar.setTitle("");



        setUpBottomNavigation();
        binding.imageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(HomeActivity.this, CartActivity.class);
               startActivity(intent);
            }
        });
    }



    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_user);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_nav_notification);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_user);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("", R.drawable.ic_more);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.icon));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);

        updateBottomNavigationPosition(0);

        binding.ahBottomNav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                     displayFragmentMain();
                        break;
                    case 1:
                        if (userModel != null) {
                           displayFragmentProfile();
                        } else {
                            //  Common.CreateNoSignAlertDialog(this);
                        }
                        break;
                    case 2:
                        if (userModel != null) {
                          displayFragmentSearch();
                        } else {
                            // Common.CreateNoSignAlertDialog(this);

                        }
                        break;
                    case 3:
                      displayFragmentSubscrabtions();
                        break;
                    case 4:
                  displayFragmentMore();
                        break;


                }
                return false;
            }
        });


    }

    private void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);
    }

    private void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_subscrabtions != null && fragment_subscrabtions.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_subscrabtions).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }
            updateBottomNavigationPosition(0);
        } catch (Exception e) {
        }
    }

    private void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_subscrabtions != null && fragment_subscrabtions.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_subscrabtions).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }
            updateBottomNavigationPosition(1);
        } catch (Exception e) {
        }
    }

    private void displayFragmentSearch() {
        try {
            if (fragment_search == null) {
                fragment_search = Fragment_Search.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_subscrabtions != null && fragment_subscrabtions.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_subscrabtions).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_search.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_search).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_search, "fragment_search").addToBackStack("fragment_search").commit();

            }
            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }
    }
    private void displayFragmentSubscrabtions() {
        try {
            if (fragment_subscrabtions == null) {
                fragment_subscrabtions = Fragment_Subscrabtions.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_subscrabtions.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_subscrabtions).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_subscrabtions, "fragment_subscrabtions").addToBackStack("fragment_subscrabtions").commit();

            }
            updateBottomNavigationPosition(3);
        } catch (Exception e) {
        }
    }

    private void displayFragmentMore() {
        try {
            if (fragment_more == null) {
                fragment_more = Fragment_More.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_subscrabtions != null && fragment_subscrabtions.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_subscrabtions).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_more.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_more).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

            }
            updateBottomNavigationPosition(4);
        } catch (Exception e) {
        }
    }


    private void NavigateToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void logout() {
        if (userModel == null) {
            NavigateToSignInActivity();
        } else {
            Logout();
        }
    }




    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {

            if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
                if (userModel == null) {
                    NavigateToSignInActivity();
                } else {
                    finish();
                }
            } else {
                displayFragmentMain();
            }


    }

    public void Logout() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.show();
        Api.getService(Tags.base_url)
                .Logout( "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            /*new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            manager.cancelAll();
                                        }
                                    },1);
                            userSingleTone.clear(ClientHomeActivity.this);*/
                            preferences.create_update_userdata(HomeActivity.this, null);
                            preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }




    public void showdetials(int id) {
        Intent intent=new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("productid",id+"");
        startActivity(intent);


    }




}

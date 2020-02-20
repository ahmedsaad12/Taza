package com.elkhelj.karam.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.elkhelj.karam.activities_fragments.activity_cart.CartActivity;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Main;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_More;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Search;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Subscrabtions;
import com.elkhelj.karam.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.elkhelj.karam.activities_fragments.activity_showall_products.AllProductActivity;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.ActivityHomeBinding;
import com.elkhelj.karam.databinding.DialogLanguageBinding;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.preferences.Preferences;
import com.esotericsoftware.minlog.Log;


import java.util.Locale;

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
    private String current_lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);

        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

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

        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


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

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.profile), R.drawable.ic_user);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.search), R.drawable.ic_search);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getResources().getString(R.string.subscribe), R.drawable.subscribe);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getResources().getString(R.string.more), R.drawable.ic_more);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
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
                           displayFragmentProfile();

                        break;
                    case 2:
                          displayFragmentSearch();

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


    public void NavigateToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }



    public void logout() {
        if (userModel == null) {
            NavigateToSignInActivity();
        } else {
            preferences.create_update_userdata(HomeActivity.this, null);
            preferences.create_update_session(HomeActivity.this, Tags.session_logout);
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void CreateLanguageDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);


        if (current_lang.equals("ar")) {
            binding.rbAr.setChecked(true);

        } else if (current_lang.equals("en")) {
            binding.rbEn.setChecked(true);

        }
        binding.rbAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshActivity("ar");


            }
        });

        binding.rbEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshActivity("en");


            }
        });


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void refreshActivity(String lang) {
        preferences.create_update_language(this, lang);

        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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

    public void showdetialss(int id) {
        Intent intent=new Intent(HomeActivity.this, AllProductActivity.class);
        intent.putExtra("search",id);
        startActivity(intent);

    }



}

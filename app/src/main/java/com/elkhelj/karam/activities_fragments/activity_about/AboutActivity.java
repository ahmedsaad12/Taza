package com.elkhelj.karam.activities_fragments.activity_about;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.karam.databinding.ActivityAboutBinding;
import com.elkhelj.karam.models.App_Data_Model;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAboutBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getAbout();


    }

    private void getAbout() {

        Api.getService(Tags.base_url)
                .getabout()
                .enqueue(new Callback<App_Data_Model>() {
                    @Override
                    public void onResponse(Call<App_Data_Model> call, Response<App_Data_Model> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body()!= null ) {

                            binding.setAppdatamodel(response.body());
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(AboutActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                              //  Toast.makeText(AboutActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<App_Data_Model> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                               //     Toast.makeText(AboutActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AboutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    @Override
    public void back() {
        finish();
    }
}

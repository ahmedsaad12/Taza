package com.elkhelj.taza.activities_fragments.activity_terms;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.taza.R;
import com.elkhelj.taza.databinding.ActivityTermsBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.App_Data_Model;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.tags.Tags;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityTermsBinding binding;
    private String lang;
    private String type;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "en")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms);
        initView();

    }


    private void initView() {
        if(getIntent().getStringExtra("type")!=null){
         type=getIntent().getStringExtra("type");
        }
else {
    binding.btnSend.setVisibility(View.GONE);
        }
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getTerms();
binding.btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        if (intent!=null)
        {
            intent.putExtra("terms","1");
            setResult(RESULT_OK,intent);
        }
        finish();
    }
});

    }

    private void getTerms() {

        Api.getService(Tags.base_url)
                .getterms()
                .enqueue(new Callback<List<App_Data_Model>>() {
                    @Override
                    public void onResponse(Call<List<App_Data_Model>> call, Response<List<App_Data_Model>> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body()!= null ) {

updateterms(response.body());
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                              //  Toast.makeText(TermsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                            //    Toast.makeText(TermsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<App_Data_Model>> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                  //  Toast.makeText(TermsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(TermsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateterms(List<App_Data_Model> body) {
for(int i=0;i<body.size();i++){
    if(body.get(i).getType()==Integer.parseInt(type)){
        binding.setAppdatamodel(body.get(i));
        break;
    }
}
    }

    @Override
    public void back() {
        finish();
    }
}

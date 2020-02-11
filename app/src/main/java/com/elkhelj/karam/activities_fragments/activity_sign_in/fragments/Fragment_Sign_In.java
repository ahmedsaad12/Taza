package com.elkhelj.karam.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.models.LoginModel;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.databinding.FragmentSignInBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.preferences.Preferences;
import com.mukesh.countrypicker.CountryPicker;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener, Listeners.BackListener , Listeners.SkipListener{
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;
    private CountryPicker countryPicker;
    private LoginModel loginModel;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLoginModel(loginModel);
        binding.setLang(current_language);
        binding.setLoginListener(this);

binding.tvfotgot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        activity.DisplayFragmentpass();
    }
});

binding.tvNoAccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        activity.DisplayFragmentSignInSignup();
    }
});
binding.tvSkip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(activity,HomeActivity.class);
        startActivity(intent);
    }
});
    }



    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin( String email, String password) {


       if (loginModel.isDataValid(activity)){

            loginModel.setEmail(email);
       loginModel.setPassword(password);
            binding.setLoginModel(loginModel);

            login(email,password);
        }
      //  navigateToHomeActivity();
    }

    private void login( String email, String password)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .login(email,password)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                preferences.create_update_userdata(activity,response.body());
                                preferences.create_update_session(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            }else
                            {
                                try {
                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.inve), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    //Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==404)
                                {
                                    Toast.makeText(activity, R.string.invp, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                   // Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                       // Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }



    private void navigateToHomeActivity() {
      /*  Intent intent = new Intent(activity, HomeBuyerActivity.class);
        startActivity(intent);
        activity.finish();*/
    }


    @Override
    public void back() {
        activity.Back();
    }

        @Override
        public void skip() {

        }

}


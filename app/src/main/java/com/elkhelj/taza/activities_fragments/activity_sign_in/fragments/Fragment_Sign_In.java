package com.elkhelj.taza.activities_fragments.activity_sign_in.fragments;

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

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.taza.databinding.FragmentSignInBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.models.LoginModel;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;
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




    }



    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin( String phone, String password) {


       if (loginModel.isDataValid(activity))
        {  phone=     String.format(Locale.ENGLISH, "%d", Integer.parseInt(phone));

            if (phone.startsWith("0")) {
                phone = phone.replaceFirst("0", "");
            }
            loginModel = new LoginModel(phone,password);
            binding.setLoginModel(loginModel);

            login(phone,password);
        }
      //  navigateToHomeActivity();
    }

    private void login( String phone, String password)
    {
//        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//
//            Api.getService(Tags.base_url)
//                    .login(phone,password)
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful()&&response.body()!=null)
//                            {
//                                preferences.create_update_userdata(activity,response.body());
//                                preferences.create_update_session(activity, Tags.session_login);
//                                Intent intent = new Intent(activity, HomeStoreActivity.class);
//                                startActivity(intent);
//                                activity.finish();
//
//                            }else
//                            {
//                                try {
//                                    Log.e("error",response.code()+"_"+response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (response.code() == 422) {
//                                    Toast.makeText(activity, getString(R.string.inc_phone_pas), Toast.LENGTH_SHORT).show();
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                }else if (response.code()==404)
//                                {
//                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();
//
//                                }else
//                                {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error",response.code()+"_"+response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage()!=null)
//                                {
//                                    Log.e("error",t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
//                                    {
//                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
//                                    }else
//                                    {
//                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }catch (Exception e){}
//                        }
//                    });
//        }catch (Exception e){
//            dialog.dismiss();
//
//        }
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


package com.elkhelj.karam.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.databinding.FragmentForgetpasswordBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Forgetpass extends Fragment implements Listeners.ShowCountryDialogListener {
    private FragmentForgetpasswordBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgetpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setShowDialogListener(this);


       binding.btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               checkdata();
           }
       });





    }

    private void checkdata() {
        String phone=binding.edtPhone.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            forget(phone);
        }
        else {
            binding.edtPhone.setError(getResources().getString(R.string.field_req));
        }
    }



    private void forget(String phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .forget(phone)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();

                            if (response.isSuccessful()) {
                                //  Log.e("data",response.body().getPassword_token());
                            //    CreateAlertDialog(response.body());
                                if(response.body().getUser_id()!=null){
activity.displayFragmentNewpass(response.body());}
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.inve), Toast.LENGTH_SHORT).show();

                                } else{
                               //     Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                   //     Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
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


    public static Fragment_Forgetpass newInstance() {
        return new Fragment_Forgetpass();
    }






    private void navigateToHomeActivity() {
      /*  Intent intent = new Intent(activity, HomeBuyerActivity.class);
        startActivity(intent);
        activity.finish();*/
    }


    @Override
    public void showDialog() {

    }
}

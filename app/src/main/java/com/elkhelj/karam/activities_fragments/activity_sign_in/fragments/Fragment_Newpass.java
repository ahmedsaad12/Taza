package com.elkhelj.karam.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.databinding.FragmentNewpasswordBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.models.PasswordModel;
import com.elkhelj.karam.preferences.Preferences;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Newpass extends Fragment implements Listeners.PasswordListner {
    private FragmentNewpasswordBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private PasswordModel passwordModel;
    private UserModel userModel;
    private static final String TAG = "DATA";

    public static Fragment_Newpass newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Newpass fragment_newpass = new Fragment_Newpass();
        fragment_newpass.setArguments(bundle);

        return fragment_newpass;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_newpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);
        }
        passwordModel = new PasswordModel();
            activity = (SignInActivity) getActivity();
            Paper.init(activity);
            binding.btnLogin.setText(activity.getString(R.string.login));


        preferences = Preferences.newInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setPassModel(passwordModel);
        binding.setPassListener(this);


    }


    @Override
    public void checkDatapass(String pass) {
            if (passwordModel.isDataValid(activity)) {
                change_pass(pass);
            }


    }


    private void change_pass(String pass) {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .editprofile(pass+ "", userModel.getUser_id()+"")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                preferences.create_update_userdata(activity, response.body());
                                preferences.create_update_session(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();


                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                 //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                       // Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }
    }

}

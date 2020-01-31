package com.elkhelj.taza.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.taza.databinding.FragmentSignUpBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.models.SignUpModel;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignUp extends Fragment implements Listeners.SignUpListener, Listeners.BackListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpBinding binding;
    private CountryPicker countryPicker;
    private SignUpModel signUpModel;
    private Preferences preferences;



    public static Fragment_SignUp newInstance() {
        return new Fragment_SignUp();
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        signUpModel = new SignUpModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(current_language);
        binding.setSignUpListener(this);
        binding.setSignUpModel(signUpModel);
        createCountryDialog();




    }



    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM() != null) {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        } else if (telephonyManager != null && countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        } else if (countryPicker.getCountryByLocale(Locale.getDefault()) != null) {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        } else {
            String code = "+974";
            //   binding.tvCode.setText(code);
            // signUpModel.set(code.replace("+","00"));

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country) {
        // binding.tvCode.setText(country.getDialCode());
        // signUpModel.setPhone_code(country.getDialCode().replace("+","00"));
        //   signUpModel.setPhone_code("00974");
    }

    @Override
    public void checkDataSignUp(String name, String shopname, String phone, String email, String password, String confirmpassword) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        signUpModel.setPhone(phone);
        signUpModel.setShop_name(shopname);
        signUpModel.setName(name);
        signUpModel.setEmail(email);
        signUpModel.setPassword(password);
        signUpModel.setConfirmpassword(confirmpassword);
        if (signUpModel.isDataValid(activity)) {
            signUp(signUpModel);
        }
    }

    @Override
    public void back() {
        activity.Back();
    }

    private void signUp(SignUpModel signUpModel) {
//        try {
//            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
//            dialog.setCancelable(false);
//            dialog.show();
//            Api.getService(Tags.base_url)
//                    .signUp(signUpModel.getName(), signUpModel.getShop_name(), signUpModel.getEmail(), signUpModel.getPassword(), signUpModel.getPhone(), "00974", "2", signUpModel.getGender_id(),activity.getResources().getString(R.string.qatar),signUpModel.getCity_id())
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful() && response.body() != null) {
//                                // activity.displayFragmentCodeVerification(response.body(),2);
//                                preferences.create_update_userdata(activity, response.body());
//                                preferences.create_update_session(activity, Tags.session_login);
//                                Intent intent = new Intent(activity, HomeStoreActivity.class);
//                                startActivity(intent);
//                                activity.finish();
//                            } else {
//                                if (response.code() == 422) {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                    try {
//
//                                        Log.e("error", response.code() + "_" + response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//
//                                } else if (response.code() == 406) {
//                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error", response.code() + "_" + response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//        }
    }

}

package com.elkhelj.karam.activities_fragments.activity_sign_in.fragments;

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

import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.adapters.CityAdapter;
import com.elkhelj.karam.models.Cities_Model;
import com.elkhelj.karam.models.Filter_model;
import com.elkhelj.karam.models.SignUpModel;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_terms.TermsActivity;
import com.elkhelj.karam.adapters.FilterAdapter;
import com.elkhelj.karam.databinding.FragmentSignUpBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.preferences.Preferences;
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

import static android.app.Activity.RESULT_OK;

public class Fragment_SignUp extends Fragment implements Listeners.SignUpListener, Listeners.BackListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpBinding binding;
    private CountryPicker countryPicker;
    private SignUpModel signUpModel;
    private Preferences preferences;

    private List<Filter_model> filter_models;
    private FilterAdapter filterAdapter;
    private Filter_model filter_model1,filter_model2,filter_model3;
    private int gender=1;
    private CityAdapter adapter;
    private List<Cities_Model> dataList;
    private String city_id = "1";

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



        filter_models=new ArrayList<>();
        dataList = new ArrayList<>();

        setfiltermodels();

        filterAdapter=new FilterAdapter(filter_models,activity);
      //  binding.spType.setAdapter(filterAdapter);
        adapter = new CityAdapter(dataList, activity);
        binding.spCity.setAdapter(adapter);

        getCities();

        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";
                    signUpModel.setCity_id(city_id);
                    binding.setSignUpModel(signUpModel);
                } else {
                    city_id = String.valueOf(dataList.get(i).getId());
                    signUpModel.setCity_id(city_id);
                    binding.setSignUpModel(signUpModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i==0){
//
//                    gender=1;
//
//                }
//                else if(i==1){
//                    gender=2;
//                }
//                else if(i==2){
//                    gender=3;
//                }
//                signUpModel.setType_id(gender+"");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });





    }
    private void updateCityAdapter(List<Cities_Model> body) {
        if (current_language.equals("ar")) {
            dataList.add(new Cities_Model("إختر المدينه"));
        } else {
            dataList.add(new Cities_Model("choose city"));
        }

        dataList.addAll(body);
        adapter.notifyDataSetChanged();

    }

    private void getCities() {
        try {
            final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getAllCities()
                    .enqueue(new Callback<List<Cities_Model>>() {
                        @Override
                        public void onResponse(Call<List<Cities_Model>> call, Response<List<Cities_Model>> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                  //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Cities_Model>> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                      //  Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void setfiltermodels() {
        filter_model1=new Filter_model();
        filter_model2=new Filter_model();
        filter_model3=new Filter_model();
        filter_model1.setAr_filter("مستخدم");
        filter_model1.setEn_filter("user");
        filter_model2.setAr_filter("مندوب");
        filter_model2.setEn_filter("representative");
        filter_model3.setAr_filter("شركه");
        filter_model3.setEn_filter("company");
        filter_models.add(filter_model1);
        filter_models.add(filter_model2);
        filter_models.add(filter_model3);
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
    public void checkDataSignUp(String name,  String phone, String email, String password, String confirmpassword) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        signUpModel.setPhone(phone);
       // signUpModel.setNational_id(national_id);
        signUpModel.setName(name);
        signUpModel.setEmail(email);
        signUpModel.setPassword(password);
        signUpModel.setConfirmpassword(confirmpassword);
      //  signUpModel.setShop_name(shop_name);
        if (signUpModel.isDataValid(activity)) {
            navigatetoterms();
         //   signUp(signUpModel);
        }
    }

    private void navigatetoterms() {
        Intent intent=new Intent(activity, TermsActivity.class);
        intent.putExtra("type",gender+"");
        startActivityForResult(intent,1);
    }

    @Override
    public void back() {
        activity.Back();
    }

    private void signUp(SignUpModel signUpModel) {
        try {
            final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .signUp(signUpModel.getName(), signUpModel.getEmail(), signUpModel.getPassword(), signUpModel.getPhone(), "00962", "1",signUpModel.getCity_id(),"1","0")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                // activity.displayFragmentCodeVerification(response.body(),2);
                                preferences.create_update_userdata(activity, response.body());
                                preferences.create_update_session(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();
                            } else {
                                try {

                                    Log.e("errordddd", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                   // Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 406) {
                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();

                                } else {
                                  //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
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
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                  //      Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.getStringExtra("terms")!=null) {
signUp(signUpModel);

            }
        }

    }

}

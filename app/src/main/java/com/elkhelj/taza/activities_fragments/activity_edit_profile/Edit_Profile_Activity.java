package com.elkhelj.taza.activities_fragments.activity_edit_profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.taza.R;
import com.elkhelj.taza.adapters.CityAdapter;
import com.elkhelj.taza.databinding.ActivityEditProfileBinding;
import com.elkhelj.taza.interfaces.Listeners;
import com.elkhelj.taza.language.LanguageHelper;
import com.elkhelj.taza.models.Cities_Model;
import com.elkhelj.taza.models.EditprofileModel;
import com.elkhelj.taza.models.UserModel;
import com.elkhelj.taza.preferences.Preferences;
import com.elkhelj.taza.remote.Api;
import com.elkhelj.taza.share.Common;
import com.elkhelj.taza.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Profile_Activity extends AppCompatActivity implements Listeners.EditprofileListener, Listeners.BackListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private String current_language;
    private ActivityEditProfileBinding binding;
    private CountryPicker countryPicker;
    private EditprofileModel editprofileModel;
    private Preferences preferences;
    private CityAdapter adapter;
    private List<Cities_Model> dataList;
    private UserModel userModel;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "en")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        initView();
        if (userModel != null) {
            updatedata(userModel);
        }
    }

    private void updatedata(UserModel userModel) {
        this.userModel = userModel;
        preferences.create_update_userdata(this, userModel);
        editprofileModel.setCity_id(this.userModel.getCity() + "");
        editprofileModel.setName(this.userModel.getName());
        editprofileModel.setPhone(this.userModel.getPhone());
        editprofileModel.setEmail(this.userModel.getEmail());
        binding.setUsermodel(userModel);
        binding.setViewModel(editprofileModel);
    }

    private String city_id = "";

    private void initView() {
        dataList = new ArrayList<>();
        editprofileModel = new EditprofileModel();
        Paper.init(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setViewModel(editprofileModel);
        binding.setBackListener(this);
        binding.setEditprofilelistner(this);
        binding.setShowDialogListener(this);
        binding.setUsermodel(userModel);
        createCountryDialog();
        adapter = new CityAdapter(dataList, this);
        binding.spinnerCity.setAdapter(adapter);
       /* binding.image.setOnClickListener(view -> CreateImageAlertDialog());*/


        getCities();

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);
                } else {
                    city_id = String.valueOf(dataList.get(i).getId());
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateCityAdapter(List<Cities_Model> body) {
        if (current_language.equals("ar")) {
            dataList.add(new Cities_Model("إختر المدينه"));
        } else {
            dataList.add(new Cities_Model("choose city"));
        }

        dataList.addAll(body);
        adapter.notifyDataSetChanged();
            if (userModel != null) {
                for (int i = 1; i < dataList.size(); i++) {
                    if ( userModel.getCity().equals(dataList.get(i).getId()+"")) {
                        binding.spinnerCity.setSelection(i);
                    }
                }
            }
        }


    private void getCities() {
        try {
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
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
                                    Toast.makeText(Edit_Profile_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                  //  Toast.makeText(Edit_Profile_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//

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
                              //          Toast.makeText(Edit_Profile_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }


    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM() != null) {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        } else if (telephonyManager != null && countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        } else if (countryPicker.getCountryByLocale(Locale.getDefault()) != null) {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        } else {
            String code = "+966";
            // binding.tvPhoneCode.setText(code);
            //  editprofileModel.setPhone_code(code.replace("+","00"));

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country) {
        // binding.tvPhoneCode.setText(country.getDialCode());
        // editprofileModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    @Override
    public void Editprofile(String name, String phone, String email) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        editprofileModel = new EditprofileModel(name, city_id, phone, email);
        binding.setViewModel(editprofileModel);
        if (editprofileModel.isDataValid(this)) {
            signUp(editprofileModel);
        }
    }

    private void signUp(EditprofileModel editprofileModel) {
        try {
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .editprofile(editprofileModel.getName(), editprofileModel.getPhone(), editprofileModel.getEmail(), editprofileModel.getCity_id(), userModel.getId())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                preferences.create_update_userdata(Edit_Profile_Activity.this, response.body());
                                finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                              //  Toast.makeText(Edit_Profile_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //    Toast.makeText(Edit_Profile_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void back() {
        finish();
    }


}

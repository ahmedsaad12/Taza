package com.elkhelj.karam.activities_fragments.activity_sign_in.fragments;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.elkhelj.karam.activities_fragments.activity_terms.TermsActivity;
import com.elkhelj.karam.adapters.CityAdapter;
import com.elkhelj.karam.adapters.FilterAdapter;
import com.elkhelj.karam.databinding.DialogSelectImageBinding;
import com.elkhelj.karam.databinding.FragmentCompanySignUpBinding;
import com.elkhelj.karam.databinding.FragmentRepresintiveSignUpBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.models.Cities_Model;
import com.elkhelj.karam.models.Filter_model;
import com.elkhelj.karam.models.SignUpCompanyModel;
import com.elkhelj.karam.models.SignUpRepresModel;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment_SignUpRepres extends Fragment implements Listeners.SignUReprepreListener, Listeners.BackListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private String current_language;
    private FragmentRepresintiveSignUpBinding binding;
    private CountryPicker countryPicker;
    private SignUpRepresModel signUpModel;
    private Preferences preferences;

    private List<Filter_model> filter_models;
    private FilterAdapter filterAdapter;
    private Filter_model filter_model1,filter_model2,filter_model3;
    private int gender;
    private CityAdapter adapter;
    private List<Cities_Model> dataList;
    private String city_id = "";
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    public static Fragment_SignUpRepres newInstance() {
        return new Fragment_SignUpRepres();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_represintive_sign_up, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        signUpModel = new SignUpRepresModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(current_language);
        binding.setSignUpListener(this);
        binding.setSignUpModel(signUpModel);
        createCountryDialog();


        binding.icon1.setOnClickListener(view -> CreateImageAlertDialog());
        binding.image1.setOnClickListener(view -> CreateImageAlertDialog());

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
    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }


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
    public void checkDataSignUp(String name,  String phone, String email, String password, String confirmpassword,String nation) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        signUpModel.setPhone(phone);
        signUpModel.setNational_id(nation);
        signUpModel.setName(name);
        signUpModel.setEmail(email);
        signUpModel.setPassword(password);
        signUpModel.setConfirmpassword(confirmpassword);
        if (signUpModel.isDataValid(activity)&&imgUri1!=null) {
            navigatetoterms();
         //   signUp(signUpModel);
        }
        else {
            if(imgUri1==null){
                Toast.makeText(activity,activity.getResources().getString(R.string.national_image),Toast.LENGTH_LONG).show();
            }
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

    private void signUp(SignUpRepresModel signUpModel) {
        try {
            final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
            RequestBody phonr_part = Common.getRequestBodyText(signUpModel.getPhone());
            RequestBody phone_code_part = Common.getRequestBodyText("00962");
            RequestBody city_part = Common.getRequestBodyText(signUpModel.getCity_id()+"");
            RequestBody type_part = Common.getRequestBodyText("2");
            RequestBody nation_part = Common.getRequestBodyText(signUpModel.getNational_id());
            RequestBody pass_part = Common.getRequestBodyText(signUpModel.getPassword());
            RequestBody email_part = Common.getRequestBodyText(signUpModel.getType_id());
            RequestBody isagree_part = Common.getRequestBodyText("1");
            MultipartBody.Part image_part = null;
            try {
   image_part= Common.getMultiPart(activity, imgUri1, "national_image");

}
catch (Exception e){

}

            Api.getService(Tags.base_url)
                    .signUp(name_part, email_part, pass_part, phonr_part, phone_code_part, type_part,city_part,isagree_part,nation_part,image_part)
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
            Toast.makeText(activity,activity.getResources().getString(R.string.dont_choose),Toast.LENGTH_LONG).show();
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
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);
         //   editImageProfile(userModel.getUser().getId() + "", imgUri1.toString());


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            imgUri1 = data.getData();
            Picasso.with(activity).load(imgUri1).fit().into(binding.image1);
            binding.icon1.setVisibility(View.GONE);
         //   editImageProfile(userModel.getUser().getId() + "", imgUri1.toString());


        }


    }
    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

}

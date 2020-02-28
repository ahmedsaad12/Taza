package com.elkhelj.karam.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.elkhelj.karam.activities_fragments.activity_edit_profile.Edit_Profile_Activity;
import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.activities_fragments.activity_notification.NotificationActivity;
import com.elkhelj.karam.databinding.DialogSelectImageBinding;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_subscribe.SubscribeActivity;
import com.elkhelj.karam.databinding.FragmentProfileBinding;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (userModel != null) {
            binding.setUsermodel(userModel);
            if(!userModel.getType().equals("1")){
                binding.llsubs.setVisibility(View.GONE);
            }
        }

        binding.setLang(lang);
        binding.llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, Edit_Profile_Activity.class);
                startActivity(intent);}
                else {
                    activity.NavigateToSignInActivity();
                }
            }
        });
        binding.image.setOnClickListener(view -> CreateImageAlertDialog());

//        binding.llfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, FollowingActivity.class);
//                startActivity(intent);
//            }
//        });
        binding.llnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, NotificationActivity.class);
                startActivity(intent);}
                else {
activity.NavigateToSignInActivity();
                }
            }
        });
        binding.llsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                Intent intent = new Intent(activity, SubscribeActivity.class);
                startActivity(intent);}


                else {
                activity.NavigateToSignInActivity();
            }}
        });
        binding.lllang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.CreateLanguageDialog();
            }
        });
//        binding.lladd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, AddPoductActivity.class);
//                startActivity(intent);
//            }
//        });
//        binding.llwish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, WishlistActivity.class);
//                startActivity(intent);
//            }
//        });
        binding.llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MyOrdersActivity.class);
                startActivity(intent);
            }
        });
//        binding.llpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, MarketProfileActivity.class);
//                intent.putExtra("id", userModel.getId() + "");
//                startActivity(intent);
//                // activity.finish();
//            }
//        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);
            editImageProfile(userModel.getId() + "", imgUri1.toString());


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            imgUri1 = data.getData();
            editImageProfile(userModel.getId() + "", imgUri1.toString());


        }

    }

    private void editImageProfile(String user_id, String image) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
try {


        RequestBody id_part = Common.getRequestBodyText(String.valueOf(user_id));

        MultipartBody.Part image_part = Common.getMultiPart(activity, Uri.parse(image), "image");

        Api.getService(Tags.base_url)
                .editUserImage(id_part, image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            updatedata(response.body());


                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                            //    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                          //  Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });}
catch (Exception e){

}
    }

    private void updatedata(UserModel body) {
        preferences.create_update_userdata(activity,body);
        this.userModel=userModel;
        binding.setUsermodel(userModel);
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

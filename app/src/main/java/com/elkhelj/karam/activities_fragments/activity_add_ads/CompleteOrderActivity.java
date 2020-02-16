package com.elkhelj.karam.activities_fragments.activity_add_ads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.databinding.ActivityCompleteorderBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.models.Add_Order_Model;
import com.elkhelj.karam.models.Order_Upload_Model;
import com.elkhelj.karam.models.PlaceGeocodeData;
import com.elkhelj.karam.models.PlaceMapDetailsData;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;
import com.elkhelj.karam.remote.Api;
import com.elkhelj.karam.share.Common;
import com.elkhelj.karam.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderActivity extends AppCompatActivity implements Listeners.BackListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback {
    private ActivityCompleteorderBinding binding;

    private String current_lang;

    private Order_Upload_Model order_upload_model;
    private Preferences preferences;
    private UserModel userModel;
    private String formated_address;
    private double lat, lang;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private boolean stop = false;
    private float zoom = 15.6f;
    private Marker marker;
    private GoogleMap mMap;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_completeorder);
        updateUI();
        CheckPermission();

        initView();

        //getDepartments();

    }
    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{gps_perm}, gps_req);
        } else {
            initGoogleApiClient();

        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.
                Builder(this).
                addOnConnectionFailedListener(this).
                addConnectionCallbacks(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1255) {
            if (requestCode == Activity.RESULT_OK) {
                startLocationUpdate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGoogleApiClient();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        lat = location.getLatitude();
        lang = location.getLongitude();
        getGeoData(lat, lang);
        AddMarker(lat, lang);

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        if (locationRequest != null) ;
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(CompleteOrderActivity.this, 1255);
                        } catch (Exception e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
//    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lang = latLng.longitude;
                    //  Log.e("nnn",lat+"  "+lng);
                    getGeoData(lat, lang);
                    // AddMarker(lat, lang);
                }
            });

        }
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void AddMarker(double lat, double lang) {
        this.lat = lat;
        this.lang = lang;
        if (marker == null) {

            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lang)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lang));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));
        }
    }

    private void Search(String query) {

        // image_pin.setVisibility(View.GONE);
        //progBar.setVisibility(View.VISIBLE);
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, current_lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            /*image_pin.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
*/
                            //    Fragment_Add_Order_To_Cart.placeMapDetailsData = response.body();
                            if (response.body().getCandidates().size() > 0) {

                                formated_address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                binding.edtAddress.setText(formated_address);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_codess", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {
dialog.dismiss();

                            Log.e("Errorss", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getGeoData(final double lat, final double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, current_lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                formated_address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                // address.setText(formatedaddress);
                                binding.edtAddress.setText(formated_address);
                                AddMarker(lat, lng);
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                //   Log.e("kkk", formatedaddress);
                            }
                        } else {
                            Log.e("error_codess", response.errorBody() + " " + response.code());

                            try {
                                Log.e("error_codess", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {
                            Log.e("error_codess", t.getMessage());


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }




    private void initView() {
        order_upload_model = new Order_Upload_Model();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        current_lang= Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_lang);
        binding.setBackListener(this);
        binding.setOrderModel(order_upload_model);
        binding.edtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String query = binding.edtAddress.getText().toString();
                    if (!TextUtils.isEmpty(query)) {
                        Search(query);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_upload_model.isDataValidStep1(CompleteOrderActivity.this)) {
                    if (userModel != null) {
                        checkdata();
                    } else {
                        //   Common.CreateNoSignAlertDialog(this);
                    }

                }
            }
        });

    }
    private void checkdata() {
        Add_Order_Model order_model=new Add_Order_Model();
order_model.setAddress(order_upload_model.getAddress());
order_model.setName(order_upload_model.getName());
order_model.setLatitude(lat);
order_model.setLongitude(lang);
order_model.setAddress(formated_address);
        if(preferences.getUserOrder(this)!=null){
            order_model.setUser_id(userModel.getId());
            order_model.setOrder_detials(preferences.getUserOrder(this));
            accept_order(order_model);

        }

    }

    private void accept_order(Add_Order_Model order_model) {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(order_model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                    preferences.create_update_order(CompleteOrderActivity.this,null);
                    Intent intent=new Intent(CompleteOrderActivity.this, MyOrdersActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //  Common.CreateDialogAlert(CartActivity.this, getString(R.string.failed));
                    Log.e("Error_code", response.code() + "_" + response.message());

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                //    Toast.makeText(CompleteOrderActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }



    @Override
    public void back() {
        finish();
    }


}

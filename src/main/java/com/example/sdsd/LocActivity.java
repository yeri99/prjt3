package com.example.sdsd;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocActivity extends AppCompatActivity {
    private static final String TAG = LocActivity.class.getSimpleName();
    private static final int GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE = 101;

    public static final int DEFAULT_LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final long DEFAULT_LOCATION_REQUEST_INTERVAL = 20000L;
    public static final long DEFAULT_LOCATION_REQUEST_FAST_INTERVAL = 10000L;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double longitude, latitude;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("위치를 설정 중입니다.");
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    private void checkLocationPermission() {
        int accessLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessLocation == PackageManager.PERMISSION_GRANTED) {
            checkLocationSetting();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationSetting();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                        builder.setTitle("위치 권한이 꺼져있습니다.");
                        builder.setMessage("[권한] 설정에서 위치 권한을 허용해야 합니다.");
                        builder.setPositiveButton("설정으로 가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    break;
                }
            }
        }
    }

    private void checkLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(DEFAULT_LOCATION_REQUEST_PRIORITY);
        locationRequest.setInterval(DEFAULT_LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_LOCATION_REQUEST_FAST_INTERVAL);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocActivity.this);
                        checkLocationPermission();
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                })
                .addOnFailureListener(LocActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocActivity.this, GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.w(TAG, "unable to start resolution for result due to " + sie.getLocalizedMessage());
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "설정에서 위치 정보 접근을 허용을 해주세요.";
                                Log.e(TAG, errorMessage);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkLocationSetting();
            } else {
                finish();
            }
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            longitude = locationResult.getLastLocation().getLongitude();
            latitude = locationResult.getLastLocation().getLatitude();
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

            Intent intent = new Intent();
            intent.putExtra("addText", getCurrentAddress(latitude, longitude));
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Log.i(TAG, "onLocationAvailability - " + locationAvailability);
        }
    };

    public String getCurrentAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        }catch(IOException ioException){
            Toast.makeText(this, "서비스 사용 불가", Toast.LENGTH_LONG).show();
            return "서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "잘못된 GPS 좌표입니다.", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표입니다.";
        }

        if (addresses == null || addresses.size() == 0){
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";
    }
}

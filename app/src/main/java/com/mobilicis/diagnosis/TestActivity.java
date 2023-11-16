package com.mobilicis.diagnosis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    ScriptGroup.Binding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    static private String backCameraWorking;
    static private String backCameraRecording;
    static private String frontCameraWorking ;
    static private String frontCameraRecording ;
    static private String primaryMicrophoneWorking;
    static private String secondaryMicrophoneWorking;
    static private String bluetoothWorking ;
    static private String bluetoothWorking2 ;
    static public String rootedStatus;
    static private String accelerometerWorking;
    static private String gpsWorking;
    static private String gpsWorking2;
    static private String fingerprintWorking;
    static private String isAutoTest;
    static private String GyroscopeWorking;
    static private String ProximityWorking;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    Button Export;
    ImageButton Back;
    LinearLayout AutoTest, backCamera, frontCamera, Bluetooth, Gyroscope, Proximity, GPS, Fingerprint;
    CardView Auto_cv, backCamera_cv, frontCamera_cv, Bluetooth_cv, Gyroscope_cv, Proximity_cv, GPS_cv, Fingerprint_cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findId();
        Intent intent = getIntent();
        if (intent!=null) {
            String from = intent.getStringExtra("source");
            if ("autoIntent".equals(from)) {
                backCameraWorking = intent.getStringExtra("backCameraWorking");
                frontCameraWorking = intent.getStringExtra("frontCameraWorking");
                primaryMicrophoneWorking = intent.getStringExtra("primaryMicrophoneWorking");
                secondaryMicrophoneWorking = intent.getStringExtra("secondaryMicrophoneWorking");
                bluetoothWorking = intent.getStringExtra("bluetoothWorking");
                rootedStatus = intent.getStringExtra("rootedStatus");
                accelerometerWorking = intent.getStringExtra("accelerometerWorking");
                gpsWorking = intent.getStringExtra("gpsWorking");
                isAutoTest = intent.getStringExtra("isAutoTest");
            } else if ("backIntent".equals(from)) {
                backCameraRecording = intent.getStringExtra("backCameraRecording");
            } else if ("frontIntent".equals(from)) {
                frontCameraRecording = intent.getStringExtra("frontCameraRecording");
            }else if ("gyroscopeIntent".equals(from)) {
                GyroscopeWorking = intent.getStringExtra("GyroscopeWorking");
            }else if ("proximityIntent".equals(from)) {
                ProximityWorking = intent.getStringExtra("ProximityWorking");
            }else if ("fingerprintIntent".equals(from)) {
                fingerprintWorking = intent.getStringExtra("fingerprintWorking");
            }
        }
        setUI();
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AutoTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAutoTest==null) {
                    Intent intent = new Intent(TestActivity.this, AutoDiagnosisActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        backCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(isAutoTest, ("Done"))){
                    Intent intent = new Intent(TestActivity.this, CameraActivity.class);
                    intent.putExtra("cameraSelector","back");
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Auto Diagnosis..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frontCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backCameraRecording != null){
                    Intent intent = new Intent(TestActivity.this, CameraActivity.class);
                    intent.putExtra("cameraSelector","front");
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Back Camera Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frontCameraRecording != null && (bluetoothWorking2 == null || Objects.equals(bluetoothWorking2, "Bluetooth is DISABLED.") || Objects.equals(bluetoothWorking2, "Bluetooth not supported on this device"))){
                    //Check bluetooth status of device
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (bluetoothAdapter == null) {
                        bluetoothWorking2 = "Bluetooth not supported on this device";
                    }
                    else {
                        bluetoothWorking2 = bluetoothAdapter.isEnabled() ? "Bluetooth is ENABLED." : "Bluetooth is DISABLED.";
                        if (!bluetoothAdapter.isEnabled()){
                            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intent);
                        }
                        bluetoothWorking2 = bluetoothAdapter.isEnabled() ? "Bluetooth is ENABLED." : "Bluetooth is DISABLED.";
                    }
                    setUI();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Front Camera Recording.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothWorking != null && GyroscopeWorking == null){
                    //Check Gyroscope working of device
                    Intent intent = new Intent(TestActivity.this, GyroscopeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Bluetooth Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Proximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GyroscopeWorking != null && ProximityWorking == null){
                    //Check Proximity working of device
                    Intent intent = new Intent(TestActivity.this, ProximityActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Gyroscope Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager;
                if (ProximityWorking != null && ( Objects.equals(gpsWorking2, null) || Objects.equals(gpsWorking2, "GPS is disable."))){
                    //Check GPS working of device
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (allPermissionsGranted()) {
                        // Start listening for location updates
                        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        gpsWorking2 = isEnable ? "GPS is enable." : "GPS is disable.";
                        if (!isEnable){
                            buildAlertMessageNoGps();
                        }
                        gpsWorking2 = isEnable ? "GPS is enable." : "GPS is disable.";
                    }
                    setUI();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Proximity Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsWorking2 != null && fingerprintWorking == null){
                    //Check Fingerprint working of device
                    Intent intent = new Intent(TestActivity.this, FingerprintActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run GPS Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backCameraWorking != null || backCameraRecording != null ||frontCameraWorking != null || frontCameraRecording != null ||primaryMicrophoneWorking != null || secondaryMicrophoneWorking != null || bluetoothWorking != null || bluetoothWorking2 != null || rootedStatus != null || accelerometerWorking != null || gpsWorking != null || gpsWorking2 != null || fingerprintWorking != null){
                    Tests tests = new Tests(backCameraWorking, backCameraRecording, frontCameraWorking, frontCameraRecording, primaryMicrophoneWorking, secondaryMicrophoneWorking, bluetoothWorking, bluetoothWorking2, rootedStatus, accelerometerWorking, gpsWorking, gpsWorking2, fingerprintWorking);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Tests");
                    Date currentTime = Calendar.getInstance().getTime();

                    reference.child(String.valueOf(currentTime)).setValue(tests).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(TestActivity.this, "Successfuly Uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(TestActivity.this, "Diagnose given options.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void findId() {
        Back = findViewById(R.id.back_btn);
        AutoTest = findViewById(R.id.ll_autotest);
        Auto_cv = findViewById(R.id.autoTest_cv);
        Export = findViewById(R.id.export);

        backCamera = findViewById(R.id.ll_backcameratest);
        frontCamera = findViewById(R.id.ll_frontcameratest);
        Bluetooth = findViewById(R.id.ll_bttest);
        Gyroscope = findViewById(R.id.ll_gyroscopetest);
        Proximity = findViewById(R.id.ll_proximitytest);
        GPS = findViewById(R.id.ll_gpstest);
        Fingerprint = findViewById(R.id.ll_fingerprinttest);

        backCamera_cv = findViewById(R.id.backcamera_cv);
        frontCamera_cv = findViewById(R.id.frontcamera_cv);
        Bluetooth_cv = findViewById(R.id.bluetooth_cv);
        Gyroscope_cv = findViewById(R.id.gyroscope_cv);
        Proximity_cv = findViewById(R.id.proximity_cv);
        GPS_cv = findViewById(R.id.gps_cv);
        Fingerprint_cv = findViewById(R.id.fingerprint_cv);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setMessage("Do you want to cancel?");

        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent = new Intent(TestActivity.this, MainActivity.class);
            startActivity(intent);
            onDestroy();
            super.onBackPressed();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void setUI(){
        if (isAutoTest != null){
            Auto_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
        }
        else {
            Auto_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
        }

        if (Objects.equals(isAutoTest, ("Done"))) {
            if (backCameraRecording != null) {
                backCamera_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                backCamera_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) && backCameraRecording != null) {
            if (frontCameraRecording != null) {
                frontCamera_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                frontCamera_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }

        if (Objects.equals(isAutoTest, ("Done")) && frontCameraRecording != null) {
            if (bluetoothWorking2 == null || Objects.equals(bluetoothWorking2, "Bluetooth is DISABLED.") || Objects.equals(bluetoothWorking2, "Bluetooth not supported on this device")){
                Bluetooth_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            } else {
                Bluetooth_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) && bluetoothWorking2 != null) {
            if (GyroscopeWorking != null) {
                Gyroscope_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Gyroscope_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) && GyroscopeWorking != null) {
            if (ProximityWorking != null) {
                Proximity_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Proximity_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) && ProximityWorking != null) {
            if (Objects.equals(gpsWorking2, null) || Objects.equals(gpsWorking2, "GPS is disable.")){
                GPS_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            } else {
                GPS_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) && gpsWorking2 != null) {
            if (fingerprintWorking != null) {
                Fingerprint_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Fingerprint_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
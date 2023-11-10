package com.mobilicis.diagnosis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    static public String backCameraWorking;
    static public String frontCameraWorking ;
    static public String primaryMicrophoneWorking;
    static public String secondaryMicrophoneWorking;
    static public String bluetoothWorking ;
    static public String rootedStatus;
    static public String accelerometerWorking;
    static public String gpsWorking;
    static public String fingerprintWorking;
    static public String isAutoTest;
    static public String GyroscopeWorking;
    static public String ProximityWorking;
    Button Export;
    ImageButton Back;
    LinearLayout AutoTest, backCamera, frontCamera, Microphone, RootStatus, Bluetooth, Accelero, Gyroscope, Proximity, GPS, Fingerprint;
    CardView Auto_cv, backCamera_cv, frontCamera_cv, Microphone_cv, RootStatus_cv, Bluetooth_cv, Accelero_cv, Gyroscope_cv, Proximity_cv, GPS_cv, Fingerprint_cv;
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
                fingerprintWorking = intent.getStringExtra("fingerprintWorking");
                isAutoTest = intent.getStringExtra("isAutoTest");
            } else if ("backIntent".equals(from)) {
                backCameraWorking = intent.getStringExtra("backCameraWorking");
            } else if ("frontIntent".equals(from)) {
                frontCameraWorking = intent.getStringExtra("frontCameraWorking");
            } else if ("microIntent".equals(from)) {
                primaryMicrophoneWorking = intent.getStringExtra("primaryMicrophoneWorking");
            } else if ("rootIntent".equals(from)) {
                rootedStatus = intent.getStringExtra("rootedStatus");
            } else if ("btIntent".equals(from)) {
                bluetoothWorking = intent.getStringExtra("bluetoothWorking");
            }else if ("accIntent".equals(from)) {
                accelerometerWorking = intent.getStringExtra("accelerometerWorking");
            }else if ("gyroscopeIntent".equals(from)) {
                GyroscopeWorking = intent.getStringExtra("GyroscopeWorking");
            }else if ("proximityIntent".equals(from)) {
                ProximityWorking = intent.getStringExtra("ProximityWorking");
            }else if ("gpsIntent".equals(from)) {
                gpsWorking = intent.getStringExtra("gpsWorking");
            }else if ("fingerprintIntent".equals(from)) {
                fingerprintWorking = intent.getStringExtra("fingerprintWorking");
            }
        }
        setUI();
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                builder.setMessage("Do you want to cancel?");

                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent intent = new Intent(TestActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                if (Objects.equals(isAutoTest, ("Done")) || Objects.equals(isAutoTest, ("Skip")) && backCameraWorking == null){
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
                if (backCameraWorking != null && frontCameraWorking == null){
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

        Microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frontCameraWorking != null && primaryMicrophoneWorking == null){
                    Intent intent = new Intent(TestActivity.this, MicrophoneActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Front Camera Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RootStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (primaryMicrophoneWorking != null && rootedStatus == null){
                    //Check rooted status of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Microphone Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rootedStatus != null && bluetoothWorking == null){
                    //Check bluetooth status of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Rooted status Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Accelero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothWorking != null && accelerometerWorking == null){
                    //Check accelerometer working of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Bluetooth Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accelerometerWorking != null && GyroscopeWorking == null){
                    //Check Gyroscope working of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Accelerometer Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Proximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GyroscopeWorking != null && ProximityWorking == null){
                    //Check Proximity working of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Gyroscope Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ProximityWorking != null && gpsWorking == null){
                    //Check GPS working of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run Proximity Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsWorking != null && fingerprintWorking == null){
                    //Check Fingerprint working of device
                }
                else {
                    Toast.makeText(TestActivity.this, "Run GPS Test..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backCameraWorking != null || frontCameraWorking != null || primaryMicrophoneWorking != null || secondaryMicrophoneWorking != null || bluetoothWorking != null || rootedStatus != null || accelerometerWorking != null || gpsWorking != null || fingerprintWorking != null){
                    Tests tests = new Tests(backCameraWorking, frontCameraWorking, primaryMicrophoneWorking, secondaryMicrophoneWorking, bluetoothWorking, rootedStatus, accelerometerWorking, gpsWorking, fingerprintWorking);
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
        Microphone = findViewById(R.id.ll_microphonetest);
        RootStatus = findViewById(R.id.ll_roottest);
        Bluetooth = findViewById(R.id.ll_bttest);
        Accelero = findViewById(R.id.ll_accelerometertest);
        Gyroscope = findViewById(R.id.ll_gyroscopetest);
        Proximity = findViewById(R.id.ll_proximitytest);
        GPS = findViewById(R.id.ll_gpstest);
        Fingerprint = findViewById(R.id.ll_fingerprinttest);

        backCamera_cv = findViewById(R.id.backcamera_cv);
        frontCamera_cv = findViewById(R.id.frontcamera_cv);
        Microphone_cv = findViewById(R.id.microphone_cv);
        RootStatus_cv = findViewById(R.id.root_cv);
        Bluetooth_cv = findViewById(R.id.bluetooth_cv);
        Accelero_cv = findViewById(R.id.accelerometer_cv);
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
            finish();
            super.onBackPressed();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setUI(){
        if (Objects.equals(isAutoTest, "Done")){
            Auto_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
        }
        else {
            Auto_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
        }

        if (Objects.equals(isAutoTest, ("Done")) || Objects.equals(isAutoTest, ("Skip"))) {
            if (backCameraWorking != null) {
                backCamera_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                backCamera_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || backCameraWorking != null) {
            if (frontCameraWorking != null) {
                frontCamera_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                frontCamera_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || frontCameraWorking != null) {
            if (primaryMicrophoneWorking != null) {
                Microphone_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Microphone_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || primaryMicrophoneWorking != null) {
            if (rootedStatus != null) {
                RootStatus_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                RootStatus_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || rootedStatus != null) {
            if (bluetoothWorking != null) {
                Bluetooth_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Bluetooth_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || bluetoothWorking != null) {
            if (accelerometerWorking != null) {
                Accelero_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Accelero_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || accelerometerWorking != null) {
            if (GyroscopeWorking != null) {
                Gyroscope_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Gyroscope_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || GyroscopeWorking != null) {
            if (ProximityWorking != null) {
                Proximity_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Proximity_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || ProximityWorking != null) {
            if (gpsWorking != null) {
                GPS_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                GPS_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
        if (Objects.equals(isAutoTest, ("Done")) || gpsWorking != null) {
            if (fingerprintWorking != null) {
                Fingerprint_cv.setCardBackgroundColor(Color.rgb(205, 242, 222));
            } else {
                Fingerprint_cv.setCardBackgroundColor(Color.rgb(242, 205, 205));
            }
        }
    }
}
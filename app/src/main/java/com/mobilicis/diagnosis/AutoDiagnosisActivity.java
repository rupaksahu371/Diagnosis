package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AutoDiagnosisActivity extends AppCompatActivity {

    TextView tests;
    Button cancel;
    ImageButton AutoTest, Back;
    ProgressBar progress;
    RecyclerView rv_tests;
    private RecyclerViewAdapter myAdapter;
    List<RecyclerViewModel> data = new ArrayList<>();
    private static final int REQUEST_AUDIO_PERMISSION = 101;
    private String backCameraWorking;
    private String frontCameraWorking;
    private String primaryMicrophoneWorking;
    private String secondaryMicrophoneWorking;
    private String bluetoothWorking;
    private String rootedStatus;
    private String accelerometerWorking;
    private String gpsWorking;
    private String fingerprintWorking;
    public RecyclerViewModel item;
    public Uri imgUri;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_diagnosis);

        AutoTest = findViewById(R.id.autoTest_btn);
        progress = findViewById(R.id.progressBar);
        rv_tests = findViewById(R.id.rv_tests);
        previewView = findViewById(R.id.camera_preview);
        Back = findViewById(R.id.back_btn1);
        tests = findViewById(R.id.tv_tests);
        cancel = findViewById(R.id.cancel_button);


        myAdapter = new RecyclerViewAdapter(AutoDiagnosisActivity.this, data);
        rv_tests.setAdapter(myAdapter);
        rv_tests.setLayoutManager(new LinearLayoutManager(AutoDiagnosisActivity.this));

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoDiagnosisActivity.this);
                builder.setMessage("Do you want to cancel?");

                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent intent = new Intent(AutoDiagnosisActivity.this, TestActivity.class);
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Rupak", "onClick: "+cancel.getText().toString());
                if (cancel.getText().toString() == "Continue"){
                        Intent intent = new Intent(AutoDiagnosisActivity.this, TestActivity.class);
                        intent.putExtra("source","autoIntent");
                        intent.putExtra("backCameraWorking",backCameraWorking);
                        intent.putExtra("frontCameraWorking",frontCameraWorking);
                        intent.putExtra("primaryMicrophoneWorking",primaryMicrophoneWorking);
                        intent.putExtra("secondaryMicrophoneWorking",secondaryMicrophoneWorking);
                        intent.putExtra("bluetoothWorking",bluetoothWorking);
                        intent.putExtra("rootedStatus",rootedStatus);
                        intent.putExtra("accelerometerWorking",accelerometerWorking);
                        intent.putExtra("gpsWorking",gpsWorking);
                        intent.putExtra("fingerprintWorking",fingerprintWorking);
                        intent.putExtra("isAutoTest","Done");
                        startActivity(intent);
                        finish();
                    }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AutoDiagnosisActivity.this);
                    builder1.setMessage("Do you want to skip Auto Diagnosis?");

                    builder1.setTitle("Alert !");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        Toast.makeText(AutoDiagnosisActivity.this, "Skipped....", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AutoDiagnosisActivity.this, TestActivity.class);
                        intent.putExtra("source","autoIntent");
                        intent.putExtra("isAutoTest","Skip");
                        startActivity(intent);
                        finish();
                    });
                    builder1.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });

                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                }
            }
        });
        AutoTest.setClickable(false);
        progress.setProgress(2);
        CameraTesting();
    }

    @Override
    public void onBackPressed() {
        if (cancel.getText().toString().equals("Continue")){
            super.onBackPressed();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AutoDiagnosisActivity.this);
            builder.setMessage("Do you want to cancel?");

            builder.setTitle("Alert !");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                Intent intent = new Intent(AutoDiagnosisActivity.this, TestActivity.class);
                startActivity(intent);
                super.onBackPressed();
                finish();
            });
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void CameraTesting() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.camera_minimalistic_svgrepo_com));
        item = new RecyclerViewModel("Back camera testing...", true);
        tests.setText("Back camera testing...");
        data.add(item);
        setMyAdapter();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        Log.d("Rupak", "CameraTesting: " + cameraProviderFuture);
        if (allPermissionsGranted()) {
            AutoTest.setVisibility(View.GONE);
            previewView.setVisibility(View.VISIBLE);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, CameraSelector.DEFAULT_BACK_CAMERA, preview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.done_round_svgrepo_com);
                            backCameraWorking = "Back camera is working.";
                            item = new RecyclerViewModel(backCameraWorking, imgUri, false);
                            data.set(0, item);
                            setMyAdapter();
                            AutoTest.setVisibility(View.VISIBLE);
                            previewView.setVisibility(View.GONE);
                            progress.setProgress(20);
                            frontCameraTesting();
                        }
                    }, 3000);
                } catch (Exception e) {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.warning_circle_svgrepo_com);
                    backCameraWorking = "Back camera is not available or working.";
                    item = new RecyclerViewModel(backCameraWorking, imgUri, false);
                    data.set(0, item);
                    setMyAdapter();
                    AutoTest.setVisibility(View.VISIBLE);
                    previewView.setVisibility(View.GONE);
                    ObjectAnimator.ofInt(progress, "progress", 20).start();
                    frontCameraTesting();
                    Log.e("CameraTest", "Error starting camera", e);
                }
            }, ContextCompat.getMainExecutor(this));
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
            restartActivity(AutoDiagnosisActivity.this);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void frontCameraTesting() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.smartphone_with_front_camera_svgrepo_com));
        item = new RecyclerViewModel("Front camera testing...", true);
        tests.setText("Front camera testing...");
        data.add(item);
        setMyAdapter();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        Log.d("Rupak", "CameraTesting: " + cameraProviderFuture);
        if (allPermissionsGranted()) {
            AutoTest.setVisibility(View.GONE);
            previewView.setVisibility(View.VISIBLE);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, CameraSelector.DEFAULT_FRONT_CAMERA, preview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.done_round_svgrepo_com);
                            frontCameraWorking = "Front camera is working.";
                            item = new RecyclerViewModel(frontCameraWorking, imgUri, false);
                            data.set(1, item);
                            setMyAdapter();
                            AutoTest.setVisibility(View.VISIBLE);
                            previewView.setVisibility(View.GONE);
                            progress.setProgress(25);
                            MircrophoneTesting();
                        }
                    }, 3000);
                } catch (Exception e) {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.warning_circle_svgrepo_com);
                    frontCameraWorking = "Front camera is not available or working.";
                    item = new RecyclerViewModel(frontCameraWorking, imgUri, false);
                    data.set(1, item);
                    setMyAdapter();
                    AutoTest.setVisibility(View.VISIBLE);
                    previewView.setVisibility(View.GONE);
                    progress.setProgress(25);
                    MircrophoneTesting();
                    Log.e("CameraTest", "Error starting camera", e);
                }
            }, ContextCompat.getMainExecutor(this));
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void MircrophoneTesting() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.mic_2_svgrepo_com));
        item = new RecyclerViewModel("Microphone testing...", true);
        tests.setText("Microphone testing...");
        data.add(item);
        setMyAdapter();

        if (allPermissionsGranted()) {
            // Test the primary microphone
            Boolean primaryWorking = isMicrophoneWorking(MediaRecorder.AudioSource.MIC);
            // Test the secondary microphone (if available)
            Boolean secondaryWorking = isMicrophoneWorking(MediaRecorder.AudioSource.CAMCORDER);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (primaryWorking ? R.drawable.done_round_svgrepo_com : R.drawable.warning_circle_svgrepo_com));
                    primaryMicrophoneWorking = primaryWorking ? "Primary microphone is working." : "Primary microphone is not working.";
                    item = new RecyclerViewModel(primaryMicrophoneWorking, imgUri, false);
                    data.set(2, item);
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (secondaryWorking ? R.drawable.done_round_svgrepo_com : R.drawable.warning_circle_svgrepo_com));
                    secondaryMicrophoneWorking = secondaryWorking ? "Secondary microphone is working." : "Secondary microphone is not working.";
                    item = new RecyclerViewModel(secondaryMicrophoneWorking, imgUri, false);
                    data.add(item);
                    setMyAdapter();
                    progress.setProgress(35);
                    RootedStatusTest();
                }
            }, 3000);
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);
        }
    }

    private boolean isMicrophoneWorking(int audioSource) {
        final int SAMPLE_RATE = 44100;
        final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO;
        final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
        final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

        try {
            AudioRecord audioRecord = new AudioRecord(audioSource, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
            audioRecord.startRecording();
            int state = audioRecord.getRecordingState();

            if (state == AudioRecord.RECORDSTATE_STOPPED) {
                Log.e("MicrophoneTest", "Microphone is not working");
                return false;
            }
            audioRecord.stop();
            audioRecord.release();

            return true;
        } catch (Exception e) {
            Log.e("MicrophoneTest", "Error testing microphone", e);
            return false;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void RootedStatusTest() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.android_svgrepo_com));
        item = new RecyclerViewModel("Deive rooted status testing...", true);
        tests.setText("Deive rooted status testing...");
        data.add(item);
        setMyAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (isDeviceRooted() ? R.drawable.warning_circle_svgrepo_com : R.drawable.done_round_svgrepo_com));
                rootedStatus = isDeviceRooted() ? "Device is rooted." : "Device is not rooted.";
                item = new RecyclerViewModel(rootedStatus, imgUri, false);
                data.set(4, item);
                setMyAdapter();
                progress.setProgress(40);
                BluetoothTest();
            }
        }, 2000);

    }

    private boolean isDeviceRooted() {
        // Check common root paths
        String[] rootPaths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"};

        for (String path : rootPaths) {
            if (new File(path).exists()) {
                return true;
            }
        }

        // Check for the "su" binary in the PATH environment variable
        try {
            Process process = Runtime.getRuntime().exec("which su");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("/su")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void BluetoothTest() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.bluetooth_wave_svgrepo_com));
        item = new RecyclerViewModel("Bluetooth testing...", true);
        tests.setText("Bluetooth testing...");
        data.add(item);
        setMyAdapter();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (bluetoothAdapter == null) {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.cancel_svgrepo_com);
                    bluetoothWorking = "Bluetooth not supported on this device";
                    item = new RecyclerViewModel(bluetoothWorking, imgUri, false);
                    data.set(5, item);
                    setMyAdapter();
                } else {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (bluetoothAdapter.isEnabled() ? R.drawable.done_round_svgrepo_com : R.drawable.warning_circle_svgrepo_com));
                    bluetoothWorking = bluetoothAdapter.isEnabled() ? "Bluetooth is ENABLED." : "Bluetooth is DISABLED.";
                    item = new RecyclerViewModel(bluetoothWorking, imgUri, false);
                    data.set(5, item);
                    setMyAdapter();
                }
                progress.setProgress(55);
                AccelerometerTest();
            }
        }, 3000);
    }

    private void AccelerometerTest() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.sensor_svgrepo_com));
        item = new RecyclerViewModel("Accelerometer sensor testing...", true);
        tests.setText("Accelerometer sensor testing...");
        data.add(item);
        setMyAdapter();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                if (allPermissionsGranted()) {
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (accelerometerSensor != null ? R.drawable.done_round_svgrepo_com : R.drawable.warning_circle_svgrepo_com));
                    accelerometerWorking = accelerometerSensor != null ? "Accelerometer is Working." : "Accelerometer is Not Working.";
                    item = new RecyclerViewModel(accelerometerWorking, imgUri, false);
                    data.set(6, item);
                    setMyAdapter();
                    progress.setProgress(70);
                    GPSTest();
                } else {
                    ActivityCompat.requestPermissions(AutoDiagnosisActivity.this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);
                }
            }
        }, 3000);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void GPSTest() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.gps_svgrepo_com));
        item = new RecyclerViewModel("GPS sensor testing...", true);
        tests.setText("GPS sensor testing...");
        data.add(item);
        setMyAdapter();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (allPermissionsGranted()) {
                    // Start listening for location updates
                    boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + (isEnable ? R.drawable.done_round_svgrepo_com : R.drawable.warning_circle_svgrepo_com));
                    gpsWorking = isEnable ? "GPS is enable." : "GPS is disable.";
                    item = new RecyclerViewModel(gpsWorking, imgUri, false);
                    data.set(7, item);
                    setMyAdapter();
                    ObjectAnimator.ofInt(progress, "progress", 85).start();
                    fingerprintTest();
                } else {
                    ActivityCompat.requestPermissions(AutoDiagnosisActivity.this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);
                }
            }
        }, 3000);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void fingerprintTest() {
        AutoTest.setImageDrawable(getDrawable(R.drawable.fingerprint_svgrepo_com));
        item = new RecyclerViewModel("Fingerprint sensor testing...", true);
        tests.setText("Fingerprint sensor testing...");
        data.add(item);
        setMyAdapter();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (allPermissionsGranted()) {
                    Context context = AutoDiagnosisActivity.this;
                    FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                    if (!fingerprintManager.isHardwareDetected()) {
                        imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.cancel_svgrepo_com);
                        fingerprintWorking = "Fingerprint sensor is not available.";
                        item = new RecyclerViewModel(fingerprintWorking, imgUri, false);
                        data.set(8, item);
                        setMyAdapter();
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.warning_circle_svgrepo_com);
                        fingerprintWorking = "Fingerprint sensor is working \n but not enrolled.";
                        item = new RecyclerViewModel(fingerprintWorking, imgUri, false);
                        data.set(8, item);
                        setMyAdapter();
                    } else {
                        imgUri = Uri.parse("android.resource://com.mobilicis.diagnosis/" + R.drawable.done_round_svgrepo_com);
                        fingerprintWorking = "Fingerprint sensor is working.";
                        item = new RecyclerViewModel(fingerprintWorking, imgUri, false);
                        data.set(8, item);
                        setMyAdapter();
                    }
                    progress.setProgress(100);
                } else {
                    ActivityCompat.requestPermissions(AutoDiagnosisActivity.this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);

                }
                AutoTest.setImageDrawable(getDrawable(R.drawable.done_round_svgrepo_com));
                tests.setText("test completed!");
                cancel.setText("Continue");
                cancel.setTextAppearance(R.style.buttonStyle);
                cancel.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(18, 175, 218)));
            }
        }, 3000);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.USE_FINGERPRINT};

    @SuppressLint("NotifyDataSetChanged")
    private void setMyAdapter() {
        rv_tests.setAdapter(myAdapter);
        rv_tests.setLayoutManager(new LinearLayoutManager(AutoDiagnosisActivity.this));
        myAdapter.notifyDataSetChanged();
    }

    public static void restartActivity(Activity activity) {
        activity.recreate();
    }

}
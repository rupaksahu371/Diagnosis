package com.mobilicis.diagnosis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private ImageCapture imageCapture;
    private String showUri;
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private Executor executor = Executors.newSingleThreadExecutor();
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    CameraSelector cameraSelector;
    TextView instruction;
    ImageButton Click;
    Class<?> IntentClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Click = findViewById(R.id.takeImage);
        instruction = findViewById(R.id.instruction_tv);
        previewView = findViewById(R.id.camera_preview);

        Intent intent = getIntent();
        if (intent!=null){
            cameraSelector = Objects.equals(intent.getStringExtra("cameraSelector"), "back") ? CameraSelector.DEFAULT_BACK_CAMERA : CameraSelector.DEFAULT_FRONT_CAMERA;
            IntentClass = Objects.equals(intent.getStringExtra("cameraSelector"), "back") ? BackCameraActivity.class : FrontCameraActivity.class ;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                instruction.setVisibility(View.GONE);
            }
        }, 2000);


        if(allPermissionsGranted()){
            startCamera();
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();
                    Preview preview = new Preview.Builder().build();
                    ImageCapture.Builder builder = new ImageCapture.Builder();

                    imageCapture = new ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build();

                    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    ImageCapture.OnImageCapturedCallback onImageCapturedCallback = null;
                    Click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageCapture.OutputFileOptions outputFileOptions =
                                    new ImageCapture.OutputFileOptions.Builder(getNoBackupFilesDir()).build();
                            imageCapture.takePicture(outputFileOptions, executor,
                                    new ImageCapture.OnImageSavedCallback() {
                                        @Override
                                        public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                                            // insert your code here.
                                            showUri = String.valueOf(outputFileResults.getSavedUri());
                                            Intent intent = new Intent(CameraActivity.this, IntentClass);
                                            intent.putExtra("showUri", showUri);
                                            startActivity(intent);
                                            finish();
                                        }
                                        @Override
                                        public void onError(ImageCaptureException error) {
                                            // insert your code here.
                                            Toast.makeText(CameraActivity.this, "Can't take a photo", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    });
                } catch (ExecutionException | InterruptedException e) {}
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CameraActivity.this, TestActivity.class);
        startActivity(intent);
        finish();
    }
}
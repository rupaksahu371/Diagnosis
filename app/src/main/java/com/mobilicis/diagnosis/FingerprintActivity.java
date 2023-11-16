package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class FingerprintActivity extends AppCompatActivity {
    ImageView iv_fingerprint;
    ImageButton Back;
    Button Continue;
    private TextView fingerprintStatusTextView;
    private static final int REQUEST_AUDIO_PERMISSION = 1201;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.USE_FINGERPRINT};
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        executor = ContextCompat.getMainExecutor(this);

        fingerprintStatusTextView = findViewById(R.id.fingerprintStatusTextView);
        iv_fingerprint = findViewById(R.id.fingerprint_iv);
        Continue = findViewById(R.id.continue_btn2);
        Back = findViewById(R.id.back_btn4);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gyroscopeStatus = String.valueOf(fingerprintStatusTextView.getText());
                Intent intent = new Intent(FingerprintActivity.this, TestActivity.class);
                intent.putExtra("fingerprintWorking", gyroscopeStatus);
                intent.putExtra("source", "fingerprintIntent");
                startActivity(intent);
                finish();
            }
        });

        if (allPermissionsGranted()) {
            Context context = FingerprintActivity.this;
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // not available
                iv_fingerprint.setImageDrawable(getDrawable(R.drawable.cancel_svgrepo_com));
                fingerprintStatusTextView.setText("Fingerprint is not available.");
                Continue.setVisibility(View.VISIBLE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // not enrolled
                iv_fingerprint.setImageDrawable(getDrawable(R.drawable.warning_circle_svgrepo_com));
                fingerprintStatusTextView.setText("Fingerprint is not Enrolled.");
                Continue.setVisibility(View.VISIBLE);
            } else {
                //working
                iv_fingerprint.setImageDrawable(getDrawable(R.drawable.done_round_svgrepo_com));
                fingerprintStatusTextView.setText("Fingerprint is Working.");
                Continue.setVisibility(View.VISIBLE);
            }
        } else {
            ActivityCompat.requestPermissions(FingerprintActivity.this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FingerprintActivity.this, TestActivity.class);
        startActivity(intent);
        finish();
    }
}
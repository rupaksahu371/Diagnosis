package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;

public class FingerprintActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION = 1201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        if (allPermissionsGranted()) {
            Context context = FingerprintActivity.this;
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // not available
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // not enrolled
            } else {
                //working
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

    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.USE_FINGERPRINT};
}
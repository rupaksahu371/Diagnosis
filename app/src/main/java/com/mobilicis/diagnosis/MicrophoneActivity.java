package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MicrophoneActivity extends AppCompatActivity {
    private static final int REQUEST_AUDIO_PERMISSION = 101;
    private boolean primaryMicrophoneWorking = false;
    private boolean secondaryMicrophoneWorking = false;
    private TextView resultsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);

        resultsTextView = findViewById(R.id.resultsTextView);
        Button testMicrophoneButton = findViewById(R.id.testMicrophoneButton);

        testMicrophoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allPermissionsGranted()) {
                    testMicrophones();
                } else {
                    ActivityCompat.requestPermissions(MicrophoneActivity.this, REQUIRED_PERMISSIONS, REQUEST_AUDIO_PERMISSION);
                }
            }
        });
    }

    private void testMicrophones() {
        // Test the primary microphone
        if (isMicrophoneWorking(MediaRecorder.AudioSource.MIC)) {
            primaryMicrophoneWorking = true;
        } else {
            primaryMicrophoneWorking = false;
        }

        // Test the secondary microphone (if available)
        if (isMicrophoneWorking(MediaRecorder.AudioSource.CAMCORDER)) {
            secondaryMicrophoneWorking = true;
        } else {
            secondaryMicrophoneWorking = false;
        }

        displayResults();
    }

    private boolean isMicrophoneWorking(int audioSource) {
        final int SAMPLE_RATE = 44100;
        final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO;
        final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
        final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

        try {
            AudioRecord audioRecord = new AudioRecord(audioSource, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);

            // Start recording and check for state
            audioRecord.startRecording();
            int state = audioRecord.getRecordingState();

            if (state == AudioRecord.RECORDSTATE_STOPPED) {
                Log.e("MicrophoneTest", "Microphone is not working");
                return false;
            }

            // Stop recording
            audioRecord.stop();
            audioRecord.release();

            return true;
        } catch (Exception e) {
            Log.e("MicrophoneTest", "Error testing microphone", e);
            return false;
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

    private void displayResults() {
        String results = "Results:\n";
        results += "Primary Microphone: " + (primaryMicrophoneWorking ? "Working" : "Not Working") + "\n";
        results += "Secondary Microphone: " + (secondaryMicrophoneWorking ? "Working" : "Not Working");

        resultsTextView.setText(results);
    }

    private static final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.RECORD_AUDIO};
}
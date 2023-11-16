package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    ImageView iv_gyroscope;
    Button Continue;
    private TextView gyroscopeStatusTextView;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private boolean isGyroscopeWorking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        gyroscopeStatusTextView = findViewById(R.id.gyroscopeStatusTextView);
        iv_gyroscope = findViewById(R.id.gyroscope_iv);
        Continue = findViewById(R.id.continue_btn);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (allPermissionsGranted()) {
            checkGyroscope();
        }

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gyroscopeStatus = String.valueOf(gyroscopeStatusTextView.getText());
                Intent intent = new Intent(GyroscopeActivity.this, TestActivity.class);
                intent.putExtra("GyroscopeWorking", gyroscopeStatus);
                intent.putExtra("source", "gyroscopeIntent");
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkGyroscope() {
        // Check for gyroscope sensor
        if (gyroscopeSensor != null) {
            isGyroscopeWorking = true;
            gyroscopeStatusTextView.setText("Rotate the phone.");
        } else {
            iv_gyroscope.setImageDrawable(getDrawable(R.drawable.rotate_exclamation_svgrepo_com));
            gyroscopeStatusTextView.setText("Gyroscope Status: Not Working");
            Continue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register gyroscope sensor listener
        if (isGyroscopeWorking) {
            sensorManager.registerListener((SensorEventListener) GyroscopeActivity.this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister gyroscope sensor listener
        if (isGyroscopeWorking) {
            sensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Detect device rotation based on gyroscope data
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // You can define a threshold for rotation detection
        double angularSpeed = Math.sqrt(x * x + y * y + z * z);

        // You can adjust this threshold as needed
        double threshold = 0.5;

        if (angularSpeed > threshold) {
            // Device is being rotated, and the gyroscope is working
            iv_gyroscope.setImageDrawable(getDrawable(R.drawable.done_round_svgrepo_com));
            gyroscopeStatusTextView.setText("Gyroscope Status: Working");
            Continue.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // This method is called when sensor accuracy changes.
    }

    private boolean allPermissionsGranted() {
        return true; // Add any necessary permission checks here if required
    }
}
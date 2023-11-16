package com.mobilicis.diagnosis;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener {
    ImageView iv_proximity;
    ImageButton Back;
    Button Continue;
    private TextView proximityStatusTextView;
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        proximityStatusTextView = findViewById(R.id.proximityStatusTextView);
        iv_proximity = findViewById(R.id.proximity_iv);
        Continue = findViewById(R.id.continue_btn1);
        Back = findViewById(R.id.back_btn3);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        checkProximitySensor();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ProximityWorking = String.valueOf(proximityStatusTextView.getText());
                Intent intent = new Intent(ProximityActivity.this, TestActivity.class);
                intent.putExtra("ProximityWorking", ProximityWorking);
                intent.putExtra("source", "proximityIntent");
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkProximitySensor() {
        // Check for the proximity sensor
        if (proximitySensor != null) {
            // Register the sensor listener
            proximityStatusTextView.setText("Put your Hand in Screen");
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            proximityStatusTextView.setText("Proximity Sensor Status: Not Working");
            iv_proximity.setImageDrawable(getDrawable(R.drawable.cancel_svgrepo_com));
            Continue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener to save power
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Detect if an object is in close proximity
        float distance = event.values[0];

        if (distance < proximitySensor.getMaximumRange()) {
            iv_proximity.setImageDrawable(getDrawable(R.drawable.done_round_svgrepo_com));
            proximityStatusTextView.setText("Proximity Sensor Status: Working (Object Detected)");
            Continue.setVisibility(View.VISIBLE);
        } else {
            iv_proximity.setImageDrawable(getDrawable(R.drawable.warning_circle_svgrepo_com));
            proximityStatusTextView.setText("Proximity Sensor Status: Working (No Object Detected)");
            Continue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // This method is called when sensor accuracy changes
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProximityActivity.this, TestActivity.class);
        startActivity(intent);
        finish();
    }
}

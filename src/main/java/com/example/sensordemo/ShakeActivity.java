package com.example.sensordemo;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor shakeSensor;
    long lastUpdated = 0;
    float last_x;
    float last_y;
    float last_z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, shakeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long time = System.currentTimeMillis();
        long diff = time - lastUpdated;
        if(diff > 100) {
            lastUpdated = time;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // distance / time = speed
            float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diff * 10000;

            if(speed > 800) {
                Toast.makeText(this, "You made a cocktail!", Toast.LENGTH_SHORT).show();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}

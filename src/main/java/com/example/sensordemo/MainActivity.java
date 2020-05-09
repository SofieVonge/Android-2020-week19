package com.example.sensordemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor gyroSensor;
    private SeekBar seekBar;
    private float seekbarMax = 100;
    private float currentSeekbar = 10;
    private ImageView button;
    float amplifier = 4.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.star);
        seekBar.setMax((int) seekbarMax);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR); //no magnetic field
    }

    public void cocktail(View view) {
        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }


    public void startPay(View view) {
        startMotionSensor();
    }

    private void startMotionSensor() {
        sm.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL); //the more we listen, the more power we use

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float y =  event.values[1];
        if(y > 0.01) {
            currentSeekbar = seekbarMax * y * amplifier; //y is always less than 1
            seekBar.setProgress((int) currentSeekbar);

            if(currentSeekbar > seekbarMax) {
                button.setVisibility(View.VISIBLE);
                sm.unregisterListener(this); // stops listening
            }
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this); //this will stop listening when the app is paused
    }
}

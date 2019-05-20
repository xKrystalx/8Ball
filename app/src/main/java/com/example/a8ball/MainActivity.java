package com.example.a8ball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity
implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 1000;

    public Ball ballObj;
    public SensorManager sensorManager;
    float coord_x,coord_y,coord_z, last_x, last_y, last_z;
    long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballObj = new Ball(getApplicationContext());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                coord_x = event.values[0];
                coord_y = event.values[1];
                coord_z = event.values[2];

                float speed = Math.abs(coord_x+coord_y+coord_z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    updateBall(coord_x - last_x, coord_y - last_y, speed);
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                }
                last_x = event.values[0];
                last_y = event.values[1];
                last_z = event.values[2];
            }
        }
    }

    public void updateBall(float diff_x, float diff_y, float speed){
        ImageView ball = findViewById(R.id.ball_Image);
        TextView option = findViewById(R.id.shakeResult);
        Random randomizer = new Random();
        int idx = randomizer.nextInt(ballObj.options.length);
        String result = ballObj.options[idx];
        ball.setImageResource(R.drawable.hw3ball_empty);
        option.setText(result);
        option.setVisibility(View.VISIBLE);
        float origin_x = ball.getX();
        float origin_y = ball.getY();
        Log.d("coordinates", "X:" + origin_x + " Y: " + origin_y);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        ball.startAnimation(shake);
        //option.startAnimation(shake);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.example.inzynierka;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements SensorEventListener {

    private final float[] accelerometerData = new float[3];
    private final float[] magnetometerData = new float[3];
    private final double[] locationData = new double[2];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    //double[] linear_acceleration = new double[3];

    public static final String EXTRA_MESSAGE_ORIENTATION = "com.example.inzynierka.MESSAGE_ORIENTATION";
    public static final String EXTRA_MESSAGE_LOCATION = "com.example.inzynierka.MESSAGE_LOCATION";
    public static final String EXTRA_MESSAGE_ACCELERATION = "com.example.inzynierka.MESSAGE_ACCELERATION";

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity);
        ImageButton cameraBtn = findViewById(R.id.backgroundImageButton);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        ActivityCompat.requestPermissions(StartActivity.this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationTracker locationTracker = new LocationTracker(getApplicationContext());
                Location location = locationTracker.getLocation();
                if(location == null){
                    Toast.makeText(getApplicationContext(), "GPS unable to get value. \n\tTurn on GPS.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    locationData[0] = location.getLatitude();
                    locationData[1] = location.getLongitude();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                updateOrientationAngles();
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values, 0, accelerometerData, 0, accelerometerData.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values, 0, magnetometerData, 0, magnetometerData.length);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null){
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null){
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void updateOrientationAngles(){
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(this, DataActivition.class);

        orientationAngles[0] = (float)(Math.toDegrees(orientationAngles[0])+360)%360;
        orientationAngles[1] = (float)Math.toDegrees(orientationAngles[1]);
        orientationAngles[2] = (float)Math.toDegrees(orientationAngles[2]);

        intent.putExtra(EXTRA_MESSAGE_ORIENTATION, orientationAngles);
        intent.putExtra(EXTRA_MESSAGE_LOCATION, locationData);
        intent.putExtra(EXTRA_MESSAGE_ACCELERATION, accelerometerData);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
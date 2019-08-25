package com.example.inzynierka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DataActivition extends AppCompatActivity {
    float[] horizontalCoo;
    double[] locationCoo;
    //float[] h_coo;

    public static final String EXTRA_MSG_OBS_DIRECTION = "com.example.inzynierka.MSG_OBS_DIRECTION";
    public static final String EXTRA_MSG_OBS_LOCATION = "com.example.inzynierka.MSG_OBS_LOCATION";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);
        Intent intent = getIntent();
        horizontalCoo = intent.getFloatArrayExtra(StartActivity.EXTRA_MESSAGE_ORIENTATION);
        locationCoo = intent.getDoubleArrayExtra(StartActivity.EXTRA_MESSAGE_LOCATION);

        TextView textView1 = findViewById(R.id.dataTextView1);
        textView1.setText(horizontalCoo[0]*180/Math.PI + " degrees");    // azimuth zeby zaokraglic .setText(String.format("%.2f ", data)
        TextView textView2 = findViewById(R.id.dataTextView2);
        textView2.setText(horizontalCoo[1]*180/Math.PI + " degrees");    // pitch
        TextView textView3 = findViewById(R.id.dataTextView3);
        textView3.setText(horizontalCoo[2]*90/Math.PI + " degrees");     // roll

        // choose roll or pitch


        TextView textView4 = findViewById(R.id.dataTextView4);
        textView4.setText(String.valueOf(locationCoo[0]));              //latitude
        TextView textView5 = findViewById(R.id.dataTextView5);
        textView5.setText(String.valueOf(locationCoo[1]));              //longitude

        Date currentTime = Calendar.getInstance().getTime();

        TextView textView6 = findViewById(R.id.dataTextView6);
        textView6.setText(String.valueOf(currentTime));

        Button calculateBtn = findViewById(R.id.calculateBtn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalculatePosition.class);
                intent.putExtra(EXTRA_MSG_OBS_DIRECTION, horizontalCoo);
                intent.putExtra(EXTRA_MSG_OBS_LOCATION, locationCoo);
                startActivity(intent);
            }
        });
    }
}

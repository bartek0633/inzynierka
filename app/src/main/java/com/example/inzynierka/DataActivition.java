package com.example.inzynierka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DataActivition extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);
        Intent intent = getIntent();
        float [] data_orientation = intent.getFloatArrayExtra(StartActivity.EXTRA_MESSAGE_ORIENTATION);
        double [] data_gps = intent.getDoubleArrayExtra(StartActivity.EXTRA_MESSAGE_LOCATION);


        TextView textView1 = findViewById(R.id.dataTextView1);
        textView1.setText(data_orientation[0]*90/Math.PI + " degrees");
        TextView textView2 = findViewById(R.id.dataTextView2);
        textView2.setText(data_orientation[1]*90/Math.PI + " degrees");
        TextView textView3 = findViewById(R.id.dataTextView3);
        textView3.setText(data_orientation[2]*90/Math.PI + " degrees");

        TextView textView4 = findViewById(R.id.dataTextView4);
        textView4.setText(String.valueOf(data_gps[0]));
        TextView textView5 = findViewById(R.id.dataTextView5);
        textView5.setText(String.valueOf(data_gps[1]));

        Date currentTime = Calendar.getInstance().getTime();

        TextView textView6 = findViewById(R.id.dataTextView6);
        textView6.setText(String.valueOf(currentTime));

    }
}

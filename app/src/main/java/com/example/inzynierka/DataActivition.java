package com.example.inzynierka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DataActivition extends AppCompatActivity {
    float[] horizontalCoo;
    float[] horizontalCooCorrect = new float[2];

    double[] locationCoo;
    float[] linearAcceleration;

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
        linearAcceleration = intent.getFloatArrayExtra(StartActivity.EXTRA_MESSAGE_ACCELERATION);

        selectAngles(horizontalCoo, linearAcceleration);

        TextView textView1 = findViewById(R.id.dataTextView1);
        textView1.setText(horizontalCooCorrect[0] + " degrees");
        TextView textView2 = findViewById(R.id.dataTextView3);
        textView2.setText(horizontalCooCorrect[1] + " degrees");

        TextView textView4 = findViewById(R.id.dataTextView4);
        textView4.setText(String.valueOf(locationCoo[0]));
        TextView textView5 = findViewById(R.id.dataTextView5);
        textView5.setText(String.valueOf(locationCoo[1]));

        Date currentTime = Calendar.getInstance().getTime();

        TextView textView6 = findViewById(R.id.dataTextView6);
        textView6.setText(String.valueOf(currentTime));

        Button calculateBtn = findViewById(R.id.calculateBtn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalculatePosition.class);
                intent.putExtra(EXTRA_MSG_OBS_DIRECTION, horizontalCooCorrect);
                intent.putExtra(EXTRA_MSG_OBS_LOCATION, locationCoo);
                startActivity(intent);
            }
        });
    }

    private void selectAngles(float[] hCoo, float[] lAcc){
        if (Math.abs(lAcc[1]) > Math.abs(lAcc[0])){                 //horizontal
            if(lAcc[1]>=0){                                         //+
                if(hCoo[0]<180)
                    horizontalCooCorrect[0] = hCoo[0] + 180 ;
                else
                    horizontalCooCorrect[0] = hCoo[0] - 180;
                if(lAcc[2] > 0)
                    horizontalCooCorrect[1] = hCoo[1];
                else
                    horizontalCooCorrect[1] = hCoo[1] + 90;
            } else{                                                 //-
                horizontalCooCorrect[0] = hCoo[0];
                horizontalCooCorrect[1] = 90 - hCoo[1];
            }
        }
        else if(Math.abs(lAcc[0]) >= Math.abs(lAcc[1])){             //vertical
            if(lAcc[0]>=0){                                          //+
                horizontalCooCorrect[0] = hCoo[0] + 90;
                horizontalCooCorrect[1] = hCoo[2];
            } else {                                                 //-
                horizontalCooCorrect[0] = hCoo[0];
                horizontalCooCorrect[1] = hCoo[2] + 180;
            }
        }
    }
}
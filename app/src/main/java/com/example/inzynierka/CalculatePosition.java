package com.example.inzynierka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalculatePosition extends AppCompatActivity {
    double[] obsCoo;
    float[] obsDir;
    Date currentDate;

    private List<AstronomicalObject> astronomicalObjects = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position_activity);
        Intent intent = getIntent();

        obsCoo = intent.getDoubleArrayExtra(DataActivition.EXTRA_MSG_OBS_LOCATION);
        obsDir = intent.getFloatArrayExtra(DataActivition.EXTRA_MSG_OBS_DIRECTION);
        currentDate = Calendar.getInstance().getTime();
        try {
            readCsv();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // wypisac liste

        for (int i = 0; i < astronomicalObjects.size(); i++){
            //astronomicalObjects.get(i).calculate(obsCoo[0]);
            System.out.println(astronomicalObjects.get(i).gethCoo(0));
        }
    }

    private void readCsv(){
        InputStream is = getResources().openRawResource(R.raw.hygdata_v3_sorted_cut);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        try {
            reader.readLine();
            while(((line = reader.readLine()) != null)){
                String[] tokens = line.split(";");

                AstronomicalObject object = new AstronomicalObject();
                object.setName(tokens[0]);
                object.setRA(Double.parseDouble(tokens[1]));
                object.setDec(Double.parseDouble(tokens[2]));
                object.setDistance(Double.parseDouble(tokens[3]));
                object.calculate(obsCoo[0]);
                astronomicalObjects.add(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

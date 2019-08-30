package com.example.inzynierka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalculatePosition extends AppCompatActivity {
    double[] obsCoo;
    float[] obsDir;
    Date currentDate;
    ListView listView;
    ArrayAdapter adapter;
    double JD;

    private List<AstronomicalObject> astronomicalObjects = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position_activity);
        Intent intent = getIntent();
        listView = findViewById(R.id.listView);

        obsCoo = intent.getDoubleArrayExtra(DataActivition.EXTRA_MSG_OBS_LOCATION);
        obsDir = intent.getFloatArrayExtra(DataActivition.EXTRA_MSG_OBS_DIRECTION);
        currentDate = Calendar.getInstance().getTime();
        calculateJD();
        try {
            readCsv();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();

        for (int i = 0; i < astronomicalObjects.size(); i++){
            astronomicalObjects.get(i).inFrame(obsDir);
            if(astronomicalObjects.get(i).isVisible)
                list.add(astronomicalObjects.get(i).getName());
        }
        if(list.size() == 0){
            TextView textView = findViewById(R.id.textView2);
            textView.setText(R.string.noObj);
        }
        else {
            adapter = new ArrayAdapter<>(this, android.R.layout.list_content, list);
            listView.setAdapter(adapter);
        }


        for (int i = 0; i < astronomicalObjects.size(); i++){
            System.out.print((int)astronomicalObjects.get(i).gethCoo(0) + "     ");
            System.out.println((int)astronomicalObjects.get(i).gethCoo(1));
        }
    }

    private void readCsv(){
        InputStream is = getResources().openRawResource(R.raw.hygdata_v3);
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
                object.setMu_ra(Double.parseDouble(tokens[3]));
                object.setMu_d(Double.parseDouble(tokens[4]));
                object.calculate(obsCoo[0], JD);
                astronomicalObjects.add(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateJD(){
        Calendar localTime = Calendar.getInstance();
        localTime.setTimeZone(TimeZone.getTimeZone("POLAND"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        String julianDate = "01-01-2000 12:00:00";
        long JD_1;
        double JD_2;
        try {
            Date julianTime = sdf.parse(julianDate);
            JD_1 = currentDate.getTime() - julianTime.getTime();
            JD_1 = TimeUnit.DAYS.convert(JD_1, TimeUnit.MILLISECONDS);
            JD_2 = (double)localTime.get(Calendar.HOUR_OF_DAY)/24+
                    (double)localTime.get(Calendar.MINUTE)/1440+
                    (double)localTime.get(Calendar.SECOND)/86400;
            JD = JD_1 + JD_2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
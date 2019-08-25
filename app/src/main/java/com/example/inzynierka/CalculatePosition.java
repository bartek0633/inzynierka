package com.example.inzynierka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.lang.Math.*;

public class CalculatePosition extends AppCompatActivity {
    private static double x, y, z, d_xy;                                       // cartesian coordinates [parsec]
    private static double rightAscension, declination;                         // actual stellar position
    private static double hCoo[][];                                            // actual stellar position in horizontal coordinates
    double obsCoo[] = new double[2];
    float obsDir[] = new float[2];
    Date currentDate;

    private static String[] name;
    private static double[][] data;

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
        hCoo = new double[data.length][2];
        for (int i = 0; i < data.length; i++){
            calculate(data[i][0], data[i][1], data[i][2], i, obsCoo[0]);
        }
    }

    private static void calculate(double alpha, double delta, double d, int i, double latitude){
        x = d * cos(delta) * cos(alpha);
        y = d * cos(delta) *  sin(alpha);
        z = d *  sin(delta);

        d_xy = sqrt(pow(x, 2) + pow(y, 2));
        rightAscension = pow(tan(z/d_xy), -1);
        declination = pow(tan(y/x), -1);

        convert(latitude, rightAscension, declination, i);
    }

    private static void convert(double latitude, double rA, double dec, int i){

        //rozwiązać problem obliczania GMST

        double cupsOfAries = 23+42/60+3/3600;       //punkt równonocy wiosennej
        double hourAngle = cupsOfAries - rA;

        double azimuth_sin, azimuth_cos, azimuth, altitude;
        double dec_r, hourAngle_r, latitude_r;
        dec_r = Math.toRadians(dec);
        hourAngle_r = Math.toRadians(hourAngle);
        latitude_r = Math.toRadians(latitude);

        altitude = sin(dec_r)*sin(latitude_r)+cos(dec_r)*cos(latitude_r)*cos(hourAngle_r);
        altitude = asin(altitude);
        altitude = Math.toDegrees(altitude);

        azimuth_cos = sin(dec_r)*cos(latitude_r)-cos(dec_r)*sin(latitude_r)*cos(hourAngle_r);
        azimuth_sin = -cos(dec_r)*sin(hourAngle_r);

        azimuth = atan(azimuth_sin/azimuth_cos);
        azimuth = Math.toDegrees(azimuth);

        if(azimuth_sin > 0 && azimuth_cos >0) {
            if (azimuth < 0)
                azimuth += 90;
        }
        else if(azimuth_sin > 0 && azimuth_cos < 0){
            if(azimuth<0)
                azimuth+= 180;
            else if(azimuth>0)
                azimuth+= 90;
        }
        else if(azimuth_sin < 0 && azimuth_cos < 0){
            if(azimuth<0)
                azimuth+= 270;
            else if(azimuth>0)
                azimuth+= 180;
        }
        else if(azimuth_sin < 0 && azimuth_cos > 0){
            if(azimuth<0)
                azimuth+= 360;
            else if(azimuth>0)
                azimuth+= 270;
        }

        hCoo[i][0] = azimuth;
        hCoo[i][1] = altitude;
    }

    private static void readCsv() throws Exception{
        Class.forName("org.relique.jdbc.csv.CsvDriver");
        String url = "jdbc:relique:csv:" + "C:\\Users\\barte\\IdeaProjects\\untitled1" + "?" + "separator=;" + "&" + "fileExtension=.csv";
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet results;
        results = stmt.executeQuery("SELECT PROPER, RA, DEC, DIST FROM hygdata_v3_sorted");

        results.last();
        int size = results.getRow();
        results.first();

        name = new String[size];
        data = new double[size][3];
        for (int i = 1; i <= size; i++){
            for (int j = 1; j <= 4; j++) {
                if(j-1 == 0)
                    name[i-1] = results.getString(1);
                else
                    data[i-1][j-2] = results.getDouble(j);
            }
            results.next();
        }
        conn.close();
    }
}

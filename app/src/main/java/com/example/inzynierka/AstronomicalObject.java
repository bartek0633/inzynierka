package com.example.inzynierka;

import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

public class AstronomicalObject {
    private String name;                                //
    private double rA;                                  //
    private double dec;                                 // } from csv file
    private double distance;                            //

    private static double rightAscension, declination; // calculated actual right ascension and declination
    private static double x, y, z, d_xy;               // coordinates in cartesian

    private double[] hCoo = new double[2];

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public double getRA() {
        return rA;
    }

    void setRA(double rA) {
        this.rA = rA;
    }

    public double getDec() {
        return dec;
    }

    void setDec(double dec) {
        this.dec = dec;
    }

    public double getDistance() {
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }

    double gethCoo(int i) {
        return hCoo[i];
    }

    public void sethCoo(double[] hCoo) {
        this.hCoo = hCoo;
    }

    void calculate(double latitude){
        dec = Math.toRadians(dec);
        rA = Math.toRadians(rA);
        x = distance * cos(dec) * cos(rA);
        y = distance * cos(dec) *  sin(rA);
        z = distance *  sin(dec);

        d_xy = sqrt(pow(x, 2) + pow(y, 2));
        rightAscension = pow(tan(z/d_xy), -1);
        declination = pow(tan(y/x), -1);
        rightAscension = Math.toDegrees(rightAscension);

        dec = Math.toDegrees(dec);
        rA = Math.toDegrees(rA);

        convert(latitude, rightAscension, declination);
    }

    private void convert(double latitude, double rA, double dec_r){

        //rozwiązać problem obliczania GMST

        double cupsOfAries = 23.0+42.0/60+3.0/3600;       //punkt równonocy wiosennej
        double hourAngle = cupsOfAries - rA;

        double azimuth_sin, azimuth_cos, azimuth, altitude;
        double hourAngle_r, latitude_r;
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

        hCoo[0] = azimuth;
        hCoo[1] = altitude;
    }


}

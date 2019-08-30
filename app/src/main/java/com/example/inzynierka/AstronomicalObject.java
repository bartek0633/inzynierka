package com.example.inzynierka;

import android.util.Log;

import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AstronomicalObject {
    private String name;
    private double rA;
    private double dec;

    private double mu_ra;
    private double mu_d;

    private double[] hCoo = new double[2];

    boolean isVisible = false;

    String getName() {
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

    public double getMu_ra() {
        return mu_ra;
    }

    void setMu_ra(double mu_ra) {
        this.mu_ra = mu_ra;
    }

    public double getMu_d() {
        return mu_d;
    }

    void setMu_d(double mu_d) {
        this.mu_d = mu_d;
    }

    double gethCoo(int i) {
        return hCoo[i];
    }

    public void sethCoo(double[] hCoo) {
        this.hCoo = hCoo;
    }

    void calculate(double latitude, double JD){
        if(JD > 0 && latitude > 0) {
            rA *= 15;

            double t = JD / 365.256363;

            mu_ra /= 3600000;
            mu_d /= 3600000;

            rA = rA * mu_ra * t;
            dec = dec * mu_d * t;

            convert(latitude, rA, dec, JD);
        }
        else if (JD < 0)
            Log.e("AstObj.calculate", "JD lower then 0.");
        else if (latitude < 0)
            Log.e("AstObj.calculate", "Latitude lower then 0.");
    }

    private void convert(double latitude, double rA, double dec, double JD) {
        if (latitude > 0 && JD > 0) {
            double era = 2.0 * Math.PI * (0.7790572732640 + 1.00273781191135448 * JD);

            double hourAngle = era - latitude - rA;

            double azimuth_sin, azimuth_cos, azimuth, altitude;
            double hourAngle_r, latitude_r, dec_r;
            hourAngle_r = Math.toRadians(hourAngle);
            latitude_r = Math.toRadians(latitude);
            dec_r = Math.toRadians(dec);

            altitude = sin(dec_r) * sin(latitude_r) + cos(dec_r) * cos(latitude_r) * cos(hourAngle_r);
            altitude = asin(altitude);
            altitude = Math.toDegrees(altitude);

            azimuth_cos = sin(dec_r) * cos(latitude_r) - cos(dec_r) * sin(latitude_r) * cos(hourAngle_r);
            azimuth_sin = -cos(dec_r) * sin(hourAngle_r);

            azimuth = atan(azimuth_sin / azimuth_cos);
            azimuth = Math.toDegrees(azimuth);

            if (azimuth_sin > 0 && azimuth_cos > 0) {
                if (azimuth < 0)
                    azimuth += 90;
            } else if (azimuth_sin > 0 && azimuth_cos < 0) {
                if (azimuth < 0)
                    azimuth += 180;
                else if (azimuth > 0)
                    azimuth += 90;
            } else if (azimuth_sin < 0 && azimuth_cos < 0) {
                if (azimuth < 0)
                    azimuth += 270;
                else if (azimuth > 0)
                    azimuth += 180;
            } else if (azimuth_sin < 0 && azimuth_cos > 0) {
                if (azimuth < 0)
                    azimuth += 360;
                else if (azimuth > 0)
                    azimuth += 270;
            }
            hCoo[0] = azimuth;
            hCoo[1] = altitude;
        } else if (JD < 0)
            Log.e("AstObj.convert", "JD lower then 0.");
        else if (latitude < 0)
            Log.e("AstObj.convert", "Latitude lower then 0.");
    }

    void inFrame(float[] obsHorCoo){
        if((int)hCoo[0]==(int)obsHorCoo[0] && (int)hCoo[1] == (int)obsHorCoo[1])
            isVisible = true;
    }
}
package com.example.inzynierka;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DataTest {
    private AstronomicalObject astronomicalObject = new AstronomicalObject();

    @Test
    public void latitudeLower(){
        astronomicalObject.calculate(-1.0, 1.0);
    }
    @Test
    public void jdLower(){
        astronomicalObject.calculate(1.0, -1.0);
    }
}

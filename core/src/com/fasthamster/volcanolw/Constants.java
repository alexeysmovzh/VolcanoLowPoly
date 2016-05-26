package com.fasthamster.volcanolw;

import java.util.Random;

/**
 * Created by alex on 19.05.16.
 */
public class Constants {

    public final static float FRUSTUM = 12f;

    public static final boolean WIND_RIGHT_TO_LEFT = false;                     // wind direction
    public static final boolean WIND_LEFT_TO_RIGHT = true;

    public static Random rand = new Random();
    public static final float DEG_TO_RAD = 0.017453292f;           // Pi/180
    public static final int FPS = 30;
    public static final String TAG = VolcanoLowPoly.class.getName();

    public final static int FAR = -6;
    public final static int NEAR = 0;
    public final static int TOP = 14;
    public final static int BOTTOM = 8;


}

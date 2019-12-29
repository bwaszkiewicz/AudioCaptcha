package com.github.bwaszkiewicz.audiocaptcha.generator;

import android.graphics.Color;
import android.util.Log;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;

import java.security.SecureRandom;
import java.util.Random;


public class ColorGenerator {

    private static final Random RAND = new SecureRandom();
    private static final String TAG = ColorGenerator.class.getName();

    private static ColorGenerator instance;
    private Configuration configuration;

    private int backgroundColor;
    private int textColor;

    private ColorGenerator() {
        configuration = Configuration.getInstance();
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static ColorGenerator getInstance() {
        if (instance == null) {
            instance = new ColorGenerator();
        }
        return instance;
    }

    public void generateColors() {
        backgroundColor = Color.argb(255, RAND.nextInt(255), RAND.nextInt(255), RAND.nextInt(255));

        checkBackgroundSaturation(backgroundColor);


        double contrastRatio;
        do {
            textColor = Color.argb(255, RAND.nextInt(255), RAND.nextInt(255), RAND.nextInt(255));
            contrastRatio = checkContrastRatio(backgroundColor, textColor);

        } while (contrastRatio < configuration.getMinColorContrastRatio() && contrastRatio > configuration.getMaxColorContrastRatio());
    }

    private double checkContrastRatio(int backgroundColor, int textColor) {

        double bgIntensity = 0.21 * ((backgroundColor >> 16) & 0xff) + 0.72 * ((backgroundColor >> 8) & 0xff) + 0.07 * (backgroundColor & 0xff);
        double textIntensity = 0.21 * ((textColor >> 16) & 0xff) + 0.72 * ((textColor >> 8) & 0xff) + 0.07 * (textColor & 0xff);
        if (bgIntensity > textIntensity)
            return (bgIntensity + 0.05) / (textIntensity + 0.05);
        else
            return (textIntensity + 0.05) / (bgIntensity + 0.05);
//        (0.21 × R) + (0.72 × G) + (0.07 × B)
//        (L1 + 0.05) / (L2 + 0.05)
    }

    private void checkBackgroundSaturation(int backgroundColor) {
        int r, g, b;
        r = (backgroundColor >> 16) & 0xff;
        g = (backgroundColor >> 8) & 0xff;
        b = backgroundColor & 0xff;

        double luminosity = 0.5 * (maxRGB(r, g, b) / 255d + minRGB(r, g, b) / 255d);
        double saturation=0;
        if (luminosity < 1)
            saturation = (maxRGB(r, g, b)/255d - minRGB(r,g,b)/255d) / (1 - Math.abs(2*luminosity -1));
        if(luminosity == 1)
            saturation = 0;

        Log.println(Log.DEBUG, TAG, "Saturation = " + saturation);
    }

    private int maxRGB(int r, int g, int b) {
        return Math.max(r, Math.max(g, b));
    }

    private int minRGB(int r, int g, int b) {
        return Math.min(r, Math.min(g, b));
    }


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}

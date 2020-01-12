package com.github.bwaszkiewicz.audiocaptcha.generator;

import android.graphics.Color;
import android.util.Log;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;

import java.security.SecureRandom;
import java.util.Random;


public class ColorGenerator {

    private static final Random RAND = new SecureRandom();

    private static ColorGenerator instance;
    private Configuration configuration;

    private int backgroundColor;
    private int textColor;

    private ColorGenerator(Configuration configuration) {
        this.configuration = configuration;
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static ColorGenerator getInstance(Configuration configuration) {
        if (instance == null) {
            instance = new ColorGenerator(configuration);
        }
        instance.setConfiguration(configuration);
        return instance;
    }

    public void generateColors() {
        backgroundColor = Color.argb(255, RAND.nextInt(255), RAND.nextInt(255), RAND.nextInt(255));

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


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    private void setConfiguration(Configuration configuration){
        this.configuration = configuration;
    }
}

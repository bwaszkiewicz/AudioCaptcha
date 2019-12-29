package com.github.bwaszkiewicz.audiocaptcha.text.producer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.security.SecureRandom;
import java.util.Random;

public class DefaultTextImgProducer implements TextImgProducer {

    private static final Random RAND = new SecureRandom();

    @Override
    public Canvas getText(int width, int height, String text, Canvas canvas, int textColor) {
        Paint pText = new Paint();

        int tSize;
        int tAngle;
        int startX;
        int startY;
        double xPrim;
        double yPrim;
        do {
            startX = RAND.nextInt(45) + 10;
            startY = RAND.nextInt(35) + 10;
            tSize= RAND.nextInt(25+1)+20;
            tAngle = RAND.nextBoolean() ? RAND.nextInt(15) : -RAND.nextInt(15);

            pText.setColor(textColor);
            pText.setTextSize(tSize);

            Rect rect = new Rect();
            float[] ary = new float[text.length()];
            pText.getTextBounds(text, 0, text.length(), rect);
            pText.getTextWidths(text, 0, text.length(), ary);
            float textWidth = sumArray(ary);

            startX += textWidth;
            startY += tSize;
            xPrim = startX * Math.cos(Math.toRadians(tAngle)) - startY * Math.sin(Math.toRadians(tAngle));
            yPrim = startX * Math.sin(Math.toRadians(tAngle)) + startY * Math.cos(Math.toRadians(tAngle)) + tSize;


        } while(xPrim < width && yPrim < height && xPrim > 0 && yPrim > 0 );

        System.out.println("xPrim = " +xPrim);
        System.out.println("yPrim = " +yPrim);

        float textsize = pText.measureText(text, 0, text.length());
        canvas.drawText(text, width / 2 - textsize / 2, height / 2, pText);

        canvas.save();
        canvas.rotate(tAngle);
        return canvas;
    }

    private float sumArray(float[] array) {
        float sum = 0;
        for (float value : array) {
            sum += value;
        }
        return sum;
    }
}

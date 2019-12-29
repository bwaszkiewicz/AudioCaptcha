package com.github.bwaszkiewicz.audiocaptcha.text.noise;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.security.SecureRandom;
import java.util.Random;

public class RectangleNoiseProducer implements NoiseProducer {

    private static final Random RAND = new SecureRandom();

    @Override
    public Canvas getNoise(int width, int height, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int a, offsetX, offsetY;
        canvas.restore();
        for (int i = 0; i < RAND.nextInt(30)+15; i++) {
            a = RAND.nextInt(15)+5;
            offsetX = RAND.nextInt(width - a);
            offsetY = RAND.nextInt(height - a);


            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(0.6f);
            paint.setColor(Color.GRAY);


            canvas.drawRect( offsetX, offsetY, offsetX + a, offsetY + a, paint);
        }

        return canvas;
    }
}

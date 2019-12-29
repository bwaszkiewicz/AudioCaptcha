package com.github.bwaszkiewicz.audiocaptcha.text.backgrounds;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;

public class BlurBackgroundProducer  implements BackgroundProducer{

    @Override
    public Canvas getBackground(int width, int height, Canvas canvas, int backgroundColor) {
        MaskFilter mask = new BlurMaskFilter(0.4f, BlurMaskFilter.Blur.INNER);
        Paint paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(backgroundColor);
        paint.setMaskFilter(mask);

        canvas.drawRect(0, 0, width, height, paint);
        return canvas;
    }
}

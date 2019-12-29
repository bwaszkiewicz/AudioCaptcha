package com.github.bwaszkiewicz.audiocaptcha.text.backgrounds;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FlatColorBackgroundProducer implements BackgroundProducer {

    @Override
    public Canvas getBackground(int width, int height, Canvas canvas, int textColor) {
        Paint paint = new Paint();
        paint.setColor(textColor);

        canvas.drawRect(0, 0, width, height, paint);
        return canvas;
    }
}

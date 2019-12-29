package com.github.bwaszkiewicz.audiocaptcha.text.producer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;

import java.security.SecureRandom;
import java.util.Random;

public class TriangleTextImgProducer implements TextImgProducer {

    private static final Random RAND = new SecureRandom();

    @Override
    public Canvas getText(int width, int height, String text, Canvas canvas, int textColor) {
        float strokeWidth = 1.5f;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        PathEffect dash = new PathDashPathEffect(getTriangle(strokeWidth), strokeWidth, 0.5f, PathDashPathEffect.Style.ROTATE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.6f);
        paint.setPathEffect(dash);
        Path path = new Path();

        int tSize;
        int tAngle;
        int startX;
        int startY;
        double xPrim;
        double yPrim;
        do {
            startX = RAND.nextInt(45)+10;
            startY = RAND.nextInt(35)+10;
            tSize= RAND.nextInt(25+1)+20;
            tAngle = RAND.nextBoolean() ? RAND.nextInt(15) : -RAND.nextInt(15);

            paint.setColor(textColor);
            paint.setTextSize(tSize);
            Rect rect = new Rect();

            float[] ary = new float[text.length()];
            paint.getTextBounds(text, 0, text.length(), rect);
            paint.getTextWidths(text, 0, text.length(), ary);
            float textWidth = sumArray(ary);

            path.reset();
            paint.getTextPath(text, 0, text.length(), startX, startY, path);
            path.close();

            startX += textWidth;
            startY += tSize;
            xPrim = startX * Math.cos(Math.toRadians(tAngle)) - startY * Math.sin(Math.toRadians(tAngle));
            yPrim = startX * Math.sin(Math.toRadians(tAngle)) + startY * Math.cos(Math.toRadians(tAngle)) + tSize;

        } while(xPrim < width && yPrim < height && xPrim > 0 && yPrim > 0 );

        System.out.println("xPrim = " +xPrim);
        System.out.println("yPrim = " +yPrim);

        canvas.save();
        canvas.rotate(tAngle);
        canvas.translate(paint.getStrokeWidth() / 2, 0);
        canvas.drawPath(path, paint);
        canvas.translate(-paint.getStrokeWidth() / 2, 0);
        return canvas;
    }

    private Path getTriangle(float size) {
        Path path = new Path();
        float half = size / 2;
        path.moveTo(-half, -half);
        path.lineTo(half, -half);
        path.lineTo(0, half);
        path.close();
        return path;
    }

    private float sumArray(float[] array) {
        float sum = 0;
        for (float value : array) {
            sum += value;
        }
        return sum;
    }
}

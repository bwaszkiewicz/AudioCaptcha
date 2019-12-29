package com.github.bwaszkiewicz.audiocaptcha.text.backgrounds;

import android.graphics.Canvas;

public interface BackgroundProducer {
    public Canvas getBackground(int width, int height, Canvas canvas, int backgroundColor);
}

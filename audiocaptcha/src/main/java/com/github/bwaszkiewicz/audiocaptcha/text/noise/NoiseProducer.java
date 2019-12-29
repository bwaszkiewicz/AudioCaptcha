package com.github.bwaszkiewicz.audiocaptcha.text.noise;

import android.graphics.Canvas;

public interface NoiseProducer {
    Canvas getNoise(int width, int height, Canvas canvas);
}

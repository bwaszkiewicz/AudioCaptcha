package com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.factory;


import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.BackgroundProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.BlurBackgroundProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.FlatColorBackgroundProducer;

public class BackgroundFactory {
    public BackgroundProducer getBackgroundProducer(BackgroundType type) {
        switch (type) {
            case BLUR:
                return new BlurBackgroundProducer();
            case FLAT:
                return new FlatColorBackgroundProducer();
            default:
                return new FlatColorBackgroundProducer();
        }
    }
}

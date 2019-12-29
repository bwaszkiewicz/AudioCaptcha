package com.github.bwaszkiewicz.audiocaptcha.text.producer.factory;


import com.github.bwaszkiewicz.audiocaptcha.text.producer.BlurTextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.DashTextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.DefaultTextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.HollowTextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.TextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.TriangleTextImgProducer;

public class TextImgFactory {
    public TextImgProducer getTextImgProducer(TextImgType type) {
        switch (type) {
            case HOLLOW:
                return new HollowTextImgProducer();
            case DASH:
                return new DashTextImgProducer();
            case TRIANGLE:
                return new TriangleTextImgProducer();
            case BLUR:
                return new BlurTextImgProducer();
            default:
                return new DefaultTextImgProducer();
        }
    }
}

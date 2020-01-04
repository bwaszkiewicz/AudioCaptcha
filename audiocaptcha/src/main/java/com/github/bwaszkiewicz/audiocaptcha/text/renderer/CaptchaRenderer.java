package com.github.bwaszkiewicz.audiocaptcha.text.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;
import com.github.bwaszkiewicz.audiocaptcha.generator.ColorGenerator;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.BackgroundProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundFactory;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundType;
import com.github.bwaszkiewicz.audiocaptcha.text.noise.NoiseProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.noise.RectangleNoiseProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.TextImgProducer;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.factory.TextImgFactory;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.factory.TextImgType;


public class CaptchaRenderer extends View {

    private int width;
    private int height;

    private ColorGenerator colorGenerator;
    private BackgroundProducer backgroundProducer;
    private TextImgProducer textImgProducer;
    private NoiseProducer noiseProducer;
    private String code;

    public CaptchaRenderer(Context context, int width, int height, BackgroundType backgroundType, TextImgType textImgType, String code, Configuration configuration) {
        super(context);
        BackgroundFactory backgroundFactory = new BackgroundFactory();
        TextImgFactory textImgFactory = new TextImgFactory();

        this.width = width;
        this.height = height;
        this.backgroundProducer = backgroundFactory.getBackgroundProducer(backgroundType);
        this.textImgProducer = textImgFactory.getTextImgProducer(textImgType);
        this.noiseProducer = new RectangleNoiseProducer();
        this.code = code;
        this.colorGenerator = ColorGenerator.getInstance(configuration);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        colorGenerator.generateColors();
        canvas = backgroundProducer.getBackground(width, height, canvas, colorGenerator.getBackgroundColor());
        canvas = textImgProducer.getText(width, height, code, canvas, colorGenerator.getTextColor());
        noiseProducer.getNoise(width,height,canvas);
    }
}

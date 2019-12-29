package com.github.bwaszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;
import com.github.bwaszkiewicz.audiocaptcha.R;
import com.github.bwaszkiewicz.audiocaptcha.controller.AudioThread.AudioThreadHandler;
import com.github.bwaszkiewicz.audiocaptcha.generator.CodeGenerator;
import com.github.bwaszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundType;
import com.github.bwaszkiewicz.audiocaptcha.text.producer.factory.TextImgType;
import com.github.bwaszkiewicz.audiocaptcha.text.renderer.CaptchaRenderer;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class CaptchaViewController extends AppCompatActivity implements ViewController {


    private View captchaLayout;
    private ImageView imageView;
    private EditText inputEditText;
    private Button submitButton;
    private Button refreshButton;
    private Button playButton;

    private CodeGenerator codeGenerator;
    private static final Random RAND = new SecureRandom();
    private Configuration configuration;
    private TextToSpeech mTextToSpeech;

    private String code;
    private Boolean isChecked = false;


    private AudioThreadHandler audioThreadHandler;

    private static final String TAG = CaptchaViewController.class.getName();

    public CaptchaViewController(View layout, Configuration configuration){
        this.captchaLayout = layout;
        this.imageView = captchaLayout.findViewById(R.id.img_captcha);
        this.inputEditText = captchaLayout.findViewById(R.id.et_captcha_input);
        this.submitButton = captchaLayout.findViewById(R.id.btn_submit);
        this.refreshButton = captchaLayout.findViewById(R.id.btn_refresh);
        this.playButton = captchaLayout.findViewById(R.id.btn_play);
        this.configuration = configuration;

        this.codeGenerator = CodeGenerator.getInstance();
        code = codeGenerator.getSequence();
        draw();
    }

    @Override
    public void init() {

        switch (configuration.getUseVersion()){
            case mix:
                initMixVersion();
                break;
            case text:
                initTextVersion();
                break;
            case audio:
                initAudioVersion();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
    }

    @Override
    public Boolean isChecked() {
        return isChecked;
    }

    @Override
    public void play() {
        if (audioThreadHandler == null || !audioThreadHandler.getVoice()) {
            checkAudio();
            playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_stop));
            audioThreadHandler = new AudioThreadHandler(captchaLayout.getContext(), mTextToSpeech, code, playButton);
            audioThreadHandler.play();
        }
        else
            audioThreadHandler.stop();
    }

    @Override
    public void submit() {
        String test = this.code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
        if (test.equals(inputEditText.getText().toString())) {
            Toast.makeText(captchaLayout.getContext(), "Correct", Toast.LENGTH_SHORT).show();
            captchaLayout.setVisibility(captchaLayout.GONE);
            isChecked = true;
        } else {
            Toast.makeText(captchaLayout.getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void submit(String code) {
        String test = this.code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
        if (test.equals(inputEditText.getText().toString())) {
            Toast.makeText(captchaLayout.getContext(), "Correct", Toast.LENGTH_SHORT).show();
            captchaLayout.setVisibility(captchaLayout.GONE);
            isChecked = true;
        } else {
            Toast.makeText(captchaLayout.getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refresh() {
        if(audioThreadHandler != null && audioThreadHandler.getVoice()) {
            audioThreadHandler.stop();
        }
        code = codeGenerator.getSequence();
        draw();
    }

    @Override
    public void clean(){
        if(mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
    }

    private void draw(){
        View v = new CaptchaRenderer(imageView.getContext(), 200, 60, BackgroundType.FLAT, drawTextImageType(), code);
        Bitmap bitmap = Bitmap.createBitmap(200,60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        imageView.setImageBitmap(bitmap);
    }

    private void initMixVersion(){

        mTextToSpeech = new TextToSpeech(captchaLayout.getContext(), new TextToSpeech.OnInitListener() {
            int result;

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = mTextToSpeech.setLanguage(configuration.getUseSpeakLanguage());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                } else {
                    playButton.setEnabled(true);
                }
            }
        });

        if(isUseOnlyNumbers()) inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(isUseOnlyUpperCase())inputEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(null);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private void initTextVersion(){
        if(isUseOnlyNumbers()) inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(isUseOnlyUpperCase())inputEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(null);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        playButton.setVisibility(View.GONE);
    }

    private void initAudioVersion(){

        mTextToSpeech = new TextToSpeech(captchaLayout.getContext(), new TextToSpeech.OnInitListener() {
            int result;

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = mTextToSpeech.setLanguage(Locale.UK);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                } else {
                    playButton.setEnabled(true);
                }
            }
        });

        imageView.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(null);
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private TextImgType drawTextImageType(){

        List<TextImgType> textImgTypes = new ArrayList<>();

        if(configuration.getUseBlurTextFilter()) textImgTypes.add(TextImgType.BLUR);
        if(configuration.getUseDashTextFilter()) textImgTypes.add(TextImgType.DASH);
        if(configuration.getUseDefaultTextFilter()) textImgTypes.add(TextImgType.DEFAULT);
        if(configuration.getUseHollowTextFilter()) textImgTypes.add(TextImgType.HOLLOW);
        if(configuration.getUseTriangleTextFilter()) textImgTypes.add(TextImgType.TRIANGLE);

        return textImgTypes.get(RAND.nextInt(textImgTypes.size()));
    }

    private void checkAudio() {
        AudioManager audio = (AudioManager) captchaLayout.getContext().getSystemService(Context.AUDIO_SERVICE);

        switch (audio.getStreamVolume(AudioManager.STREAM_MUSIC)) {
            case 0:
                Toast.makeText(captchaLayout.getContext(), "You have a muted sound.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(captchaLayout.getContext(), "You have very low volume.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Boolean isUseOnlyNumbers(){
        return !configuration.getGenerateLowerCases() && !configuration.getGenerateUpperCases() && configuration.getGenerateNumbers();
    }

    private Boolean isUseOnlyUpperCase(){
        return !configuration.getGenerateLowerCases() && !configuration.getGenerateUpperCases();
    }

}

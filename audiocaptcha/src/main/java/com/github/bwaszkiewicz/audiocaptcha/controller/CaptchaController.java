package com.github.bwaszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;
import com.github.bwaszkiewicz.audiocaptcha.controller.AudioThread.AudioThreadHandler;
import com.github.bwaszkiewicz.audiocaptcha.generator.CodeGenerator;

public class CaptchaController implements ViewController {

    private View activityView;

    private CodeGenerator codeGenerator;
    private Configuration configuration;
    private TextToSpeech mTextToSpeech;

    private String code;
    private Boolean isChecked = false;

    private static final String TAG = CaptchaController.class.getName();

    private AudioThreadHandler audioThreadHandler;

    public CaptchaController(View activityView, Configuration configuration){

        this.activityView = activityView;
        this.configuration = configuration;

        this.codeGenerator = CodeGenerator.getInstance();
        code = codeGenerator.getSequence();

    }

    @Override
    public void init() {

        mTextToSpeech = new TextToSpeech(activityView.getContext(), new TextToSpeech.OnInitListener() {
            int result;

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = mTextToSpeech.setLanguage(configuration.getUseSpeakLanguage());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                } else {
                    Log.e("TTS", "Language supported");
                }
            }
        });

    }

    @Override
    public Boolean isChecked() {
        return null;
    }

    @Override
    public void clean() {
        if(mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
    }

    @Override
    public void refresh() {
        if(audioThreadHandler != null && audioThreadHandler.getVoice()) {
            audioThreadHandler.stop();
        }
        code = codeGenerator.getSequence();
    }

    @Override
    public void play() {
        if (audioThreadHandler == null || !audioThreadHandler.getVoice()) {
            checkAudio();
            audioThreadHandler = new AudioThreadHandler(activityView.getContext(), mTextToSpeech, code, null);
            audioThreadHandler.play();
        }
        else
            audioThreadHandler.stop();
    }

    @Override
    public void submit() throws Exception {
        throw new Exception("Cannot use this method to no gui version");
    }

    @Override
    public void submit(String code) {
        String test = this.code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
        if (test.equals(code)){
            Toast.makeText(activityView.getContext(), "Correct", Toast.LENGTH_SHORT).show();
            isChecked = true;
        } else {
            Toast.makeText(activityView.getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAudio() {
        AudioManager audio = (AudioManager) activityView.getContext().getSystemService(Context.AUDIO_SERVICE);

        switch (audio.getStreamVolume(AudioManager.STREAM_MUSIC)) {
            case 0:
                Toast.makeText(activityView.getContext(), "You have a muted sound.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(activityView.getContext(), "You have very low volume.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

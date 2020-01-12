package com.github.bwaszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;
import com.github.bwaszkiewicz.audiocaptcha.audio.AudioVolume;
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

        this.codeGenerator = new CodeGenerator(configuration);
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
        return isChecked;
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
            audioThreadHandler = new AudioThreadHandler(activityView.getContext(), mTextToSpeech, code, null, configuration);
            audioThreadHandler.play();
        }
        else
            audioThreadHandler.stop();
    }

    @Override
    public void submit() throws Exception {
        throw new Exception("You cannot use this method for the controller version without GUI");
    }

    @Override
    public void submit(String code) {
        String test = this.code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
        if (test.equals(code)){
            if(configuration.getUseToastMessage()) {
                Toast.makeText(activityView.getContext(), "Correct", Toast.LENGTH_SHORT).show();
            }
            isChecked = true;
        } else {
            if(configuration.getUseToastMessage()) {
                Toast.makeText(activityView.getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public AudioVolume checkAudio() {
        AudioManager audioManager = (AudioManager) activityView.getContext().getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Double currentVolumePercentage = 100.0 * currentVolume/maxVolume;

        AudioVolume audioVolume;
        if(currentVolumePercentage <=0){
            audioVolume = AudioVolume.MUTE;
        } else  if (currentVolumePercentage <= 10) audioVolume = AudioVolume.VERY_LOW;
            else if (currentVolumePercentage <=25) audioVolume = AudioVolume.LOW;
            else if (currentVolumePercentage <= 50) audioVolume = AudioVolume.NORMAL;
            else audioVolume = AudioVolume.LOUD;

        if(configuration.getUseToastMessage()) {
            switch (audioVolume) {
                case MUTE:
                    Toast.makeText(activityView.getContext(), "You have a muted sound.", Toast.LENGTH_SHORT).show();
                    break;
                case VERY_LOW:
                    Toast.makeText(activityView.getContext(), "You have very low volume.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return audioVolume;
    }
    @Override
    public Button getSubmitBtn(){
        return null;
    }

    @Override
    public Button getRefreshBtn() { return  null; }

    @Override
    public Button getPlayBtn() { return null; }

    @Override
    public EditText getInputEditText(){
        return null;
    }

}

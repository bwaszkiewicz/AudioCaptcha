package com.github.bwaszkiewicz.audiocaptcha.controller.AudioThread;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;
import com.github.bwaszkiewicz.audiocaptcha.R;
import com.github.bwaszkiewicz.audiocaptcha.audio.EffectsManager;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class AudioThreadHandler {

    private Context context;
    private TextToSpeech mTextToSpeech;
    private MediaPlayer player;
    private String code;
    private Thread speakThread;
    private Configuration configuration;

    private Button playButton;

    private static final Random RAND = new SecureRandom();
    private volatile Boolean isVoice = false;
    private volatile Boolean isStopVoice = false;

    private static final String TAG = AudioThreadHandler.class.getName();

    public AudioThreadHandler(Context context, TextToSpeech textToSpeech, String code, Button playButton, Configuration configuration){
        this.context = context;
        this.mTextToSpeech = textToSpeech;
        this.code = code;
        this.playButton = playButton;
        this.configuration = configuration;
    }

    public void play(){


        final File outputFile = createTempOutputFile();
        saveNoiseToFile(outputFile);

        mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) { }

            @Override
            public void onDone(String utteranceId) {

                if (player == null) {
                    player = MediaPlayer.create(context, Uri.parse(utteranceId));
                }

                speakThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            isVoice = true;

                            EffectsManager manager = new EffectsManager(player.getAudioSessionId(), configuration);
                            manager.applayEffects();
                            player.start();
                            player.setAuxEffectSendLevel(1.0f);
                            player.setLooping(true);
                            String[] sequence = code.split(" ");

                            Double moduleParam;
                            for (String character : sequence) {
                                if (isStopVoice) {
                                    onStop();
                                    return;
                                }

                                moduleParam = RAND.nextDouble()*2;
                                mTextToSpeech.setPitch(1.0f + (float)Math.sin(moduleParam*Math.PI));
                                mTextToSpeech.setSpeechRate(1.0f);
                                mTextToSpeech.speak(character, TextToSpeech.QUEUE_ADD, null, null);
                                mTextToSpeech.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null);

                            }
                            boolean speakingEnd;
                            do {
                                if (isStopVoice) {
                                    onStop();
                                    return;
                                }
                                speakingEnd = mTextToSpeech.isSpeaking();
                            } while (speakingEnd);
                            if (player != null) {
                                player.release();
                                player = null;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Exception until player running: " + e.getMessage());
                        }

                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               if(playButton != null) playButton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_play));
                            }
                        });
                        isVoice = false;
                    }

                    private void onStop() {
                        mTextToSpeech.stop();
                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        isStopVoice = false;
                        isVoice = false;
                    }
                });
                speakThread.start();
                outputFile.delete();
            }
            @Override
            public void onError(String utteranceId) {
                Log.println(Log.DEBUG, TAG, "Occured error when created synteziedToFile, utteranceId: " + utteranceId);
            }
        });

    }

    public void stop(){
        if (speakThread.isAlive()) {
            isStopVoice = true;
            if (playButton != null) playButton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_play));
        }
    }

    private File createTempOutputFile(){
        final File outputDir = context.getCacheDir();

        File outputFile = null;
        try {
            outputFile = File.createTempFile("tempTTS", ".wav", outputDir);
        } catch (IOException e) {
            Log.println(Log.DEBUG, TAG, "Occured error when created temp file: " + e.getMessage());
        }
        return  outputFile;
    }

    private void saveNoiseToFile(File outputFile){
        Uri uri = Uri.fromFile(outputFile);

        String noiseText;
        StringBuilder noiseTextBuilder = new StringBuilder();
        for(int i=0;i<35;i++) {
            char x = RAND.nextBoolean() ? (char) (RAND.nextInt(26) + 97) : (char) (RAND.nextInt(10));
            noiseTextBuilder.append(x);
        }

        noiseText = noiseTextBuilder.toString();

        mTextToSpeech.setPitch(0.9f);
        mTextToSpeech.setSpeechRate(0.3f);
        mTextToSpeech.synthesizeToFile(noiseText,null, outputFile, uri.toString());
    }

    public Boolean getVoice() {
        return isVoice;
    }

}

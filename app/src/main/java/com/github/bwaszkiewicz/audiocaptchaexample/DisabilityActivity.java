package com.github.bwaszkiewicz.audiocaptchaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.bwaszkiewicz.audiocaptcha.AudioCaptcha;
import com.github.bwaszkiewicz.audiocaptcha.Configuration;

import java.util.Locale;

public class DisabilityActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener  {

    private static final String TAG = DisabilityActivity.class.getName();
    private AudioCaptcha captcha;

    private TextView tvCode;
    private String inputCode = "";
    private Integer counter=0;
    private TextToSpeech mTextToSpeech;

    private GestureDetector gestureDetector;
    public static final int SWIPE_TRESHOLD = 100;
    public static final int SWIPE_VELOCITY_TRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disability);

        tvCode = findViewById(R.id.code_tv);

        gestureDetector = new GestureDetector(this, this);

        findViewById(R.id.disability_activity).setOnTouchListener(this);

        Configuration cnf = Configuration.builder()
                .useGUI(false)
                .build();

        captcha = new AudioCaptcha(findViewById(R.id.disability_activity), cnf);


        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            int result;

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = mTextToSpeech.setLanguage(Locale.UK);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                } else {
                    Log.e("TTS", "Language supported");
                }
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        captcha.clean();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(counter<9)
            counter++;
        Log.d(TAG,"single tap");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if(inputCode.length() > 3){
            inputCode = "";
            tvCode.setText(inputCode);
        } else {
            mTextToSpeech.speak(counter.toString(),TextToSpeech.QUEUE_ADD, null, null);
            inputCode += counter.toString();
            tvCode.setText(inputCode);
            counter = 0;
            Log.d(TAG, "long press");
        }
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if(Math.abs(diffX) > Math.abs(diffY)){
            // right or left swipe

            if(Math.abs(diffX) > SWIPE_TRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_TRESHOLD){
                if(diffX > 0 )
                    onSwipeRight();
                else
                    onSwipeLeft();
            }
        } else {
            // up or down swipe
            if(Math.abs(diffY) > SWIPE_TRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_TRESHOLD){
                if(diffY >0)
                    onSwipeBottom();
                else
                    onSwipeTop();
            }

        }


        return false;
    }

    private void onSwipeRight(){
        captcha.refresh();
    }

    private void onSwipeLeft(){

    }

    private void onSwipeBottom(){

        captcha.submit(inputCode);

        if(captcha.getResult()){
            mTextToSpeech.speak("Success",TextToSpeech.QUEUE_ADD,null, null);
        } else
        {
            mTextToSpeech.speak("Fail",TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    private void onSwipeTop(){
        captcha.play();
    }
}

package com.github.bwaszkiewicz.audiocaptcha.controller;

import android.widget.Button;
import android.widget.EditText;

import com.github.bwaszkiewicz.audiocaptcha.audio.AudioVolume;

public interface ViewController {
    void init();
    Boolean isChecked();
    void clean();

    void refresh();
    void play();
    void submit() throws Exception;
    void submit(String code);
    AudioVolume checkAudio();

    Button getSubmitBtn();
    Button getRefreshBtn();
    Button getPlayBtn();
    EditText getInputEditText();
}

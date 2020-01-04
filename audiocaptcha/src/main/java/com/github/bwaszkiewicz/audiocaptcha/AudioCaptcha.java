package com.github.bwaszkiewicz.audiocaptcha;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.bwaszkiewicz.audiocaptcha.controller.CaptchaController;
import com.github.bwaszkiewicz.audiocaptcha.controller.CaptchaViewController;
import com.github.bwaszkiewicz.audiocaptcha.controller.ViewController;

public class AudioCaptcha {

    private ViewController viewController;

    public AudioCaptcha(View captchaLayout){

        Configuration configuration = new Configuration();

        ViewController viewController = new CaptchaViewController(captchaLayout, configuration);
        viewController.init();

    }

    public AudioCaptcha(View captchaLayout, Configuration configuration){

        if(!configuration.getUseGUI()){
            viewController = new CaptchaController(captchaLayout, configuration);
        } else {
            viewController = new CaptchaViewController(captchaLayout, configuration);
        }
        viewController.init();

    }

    public Boolean getResult(){
        return viewController.isChecked();
    }

    public void clean(){
        viewController.clean();
    }

    public void refresh(){
        viewController.refresh();
    }

    public void play(){
        viewController.play();
    }

    public void submit(String code){
        viewController.submit(code);
    }

    public void submit() {
        try {
        viewController.submit();
        } catch (Exception e){
            Log.e("AudioCaptcha", "Cannot use this method to no gui version" );
        }
    }

    public Button getSubmitButton(){
        return viewController.getSubmitBtn();
    }

    public Button getRefreshButton() { return  viewController.getRefreshBtn(); }

    public Button getPlayButton() { return  viewController.getPlayBtn(); }

    public EditText getInputEditText(){
        return viewController.getInputEditText();
    }
}

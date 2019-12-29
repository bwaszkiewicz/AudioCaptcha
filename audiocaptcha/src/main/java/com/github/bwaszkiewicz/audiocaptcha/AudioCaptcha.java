package com.github.bwaszkiewicz.audiocaptcha;

import android.view.View;
import android.widget.Button;

import com.github.bwaszkiewicz.audiocaptcha.controller.CaptchaController;
import com.github.bwaszkiewicz.audiocaptcha.controller.CaptchaViewController;
import com.github.bwaszkiewicz.audiocaptcha.controller.ViewController;

public class AudioCaptcha {

    private ViewController viewController;

    public AudioCaptcha(View captchaLayout){

        Configuration configuration = Configuration.getInstance();

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
}

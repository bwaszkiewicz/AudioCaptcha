package com.github.bwaszkiewicz.audiocaptchaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.bwaszkiewicz.audiocaptcha.AudioCaptcha;
import com.github.bwaszkiewicz.audiocaptcha.Configuration;

public class MainActivity extends AppCompatActivity {

    private AudioCaptcha captcha1;
    private AudioCaptcha captcha2;
    private AudioCaptcha captcha3;

    private Button disabilityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disabilityBtn = findViewById(R.id.disability_btn);

        disabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DisabilityActivity.class);
                startActivity(i);
            }
        });


        Configuration cnf = Configuration.builder()
                .generateNumbers(true)
                .generateLowerCases(false)
                .generateUpperCases(true)
                .codeLength(4)
                .useTriangleTextFilter(true)
                .useBlurTextFilter(false)
                .useHollowTextFilter(true)
                .useDefaultTextFilter(true)
                .useDashTextFilter(true)
                .useDynamicProcessingEffect(true)
                .usePresetReverbEffect(true)
                .minColorContrastRatio(20.0)
                .useVersion(Configuration.Version.audio)
                .build();

        captcha1 = new AudioCaptcha(findViewById(R.id.audioCaptcha), cnf);

        cnf = Configuration.builder()
                .generateNumbers(true)
                .generateLowerCases(false)
                .generateUpperCases(true)
                .codeLength(5)
                .useTriangleTextFilter(true)
                .useBlurTextFilter(false)
                .useHollowTextFilter(true)
                .useDefaultTextFilter(true)
                .useDashTextFilter(true)
                .useDynamicProcessingEffect(true)
                .usePresetReverbEffect(true)
                .minColorContrastRatio(20.0)
                .useVersion(Configuration.Version.text)
                .useToastMessage(false)
                .build();

        captcha2 = new AudioCaptcha(findViewById(R.id.audioCaptcha2), cnf);
        captcha2.getSubmitButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captcha2.submit();
                if(captcha2.getResult())
                    Toast.makeText(getApplicationContext(), "Twoja odpowiedź jest poprawna", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Twoja odpowiedź jest niepoprawna", Toast.LENGTH_SHORT).show();
            }
        });
        cnf = Configuration.builder()
                .generateNumbers(true)
                .generateLowerCases(false)
                .generateUpperCases(true)
                .codeLength(4)
                .useTriangleTextFilter(true)
                .useBlurTextFilter(false)
                .useHollowTextFilter(true)
                .useDefaultTextFilter(true)
                .useDashTextFilter(true)
                .useDynamicProcessingEffect(true)
                .usePresetReverbEffect(true)
                .minColorContrastRatio(20.0)
                .useVersion(Configuration.Version.mix)
                .build();

        captcha3 = new AudioCaptcha(findViewById(R.id.audioCaptcha3), cnf);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captcha1.clean();
        captcha2.clean();
        captcha3.clean();
    }
}

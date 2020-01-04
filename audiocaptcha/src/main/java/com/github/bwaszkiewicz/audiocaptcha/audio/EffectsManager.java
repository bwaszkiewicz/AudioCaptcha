package com.github.bwaszkiewicz.audiocaptcha.audio;


import com.github.bwaszkiewicz.audiocaptcha.Configuration;

public class EffectsManager {

    private Configuration configuration;
    int audioSessionId;

    public EffectsManager(int audioSessionId, Configuration configuration){
        this.audioSessionId = audioSessionId;
        this.configuration = configuration;
    }

    public void applayEffects(){
        if(configuration.getUseDynamicProcessingEffect()){
            EffectsProducer dynamicsProcessing = new DynamicsProcessingProducer();
            dynamicsProcessing.enableEffect(audioSessionId);
        }

        if(configuration.getUsePresetReverbEffect()){
            EffectsProducer presetReverb = new PresetReverbProducer();
            presetReverb.enableEffect(audioSessionId);
        }
    }


}

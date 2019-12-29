package com.github.bwaszkiewicz.audiocaptcha.audio;

import android.media.audiofx.DynamicsProcessing;

public class DynamicsProcessingProducer implements EffectsProducer {



    @Override
    public void enableEffect(int audioSessionId) {
        DynamicsProcessing.Config.Builder builder = new DynamicsProcessing.Config.Builder(
                DynamicsProcessing.VARIANT_FAVOR_FREQUENCY_RESOLUTION,
                1,
                true,
                8, true, 8,true, 8, true);
        builder.setPreferredFrameDuration(10);
        DynamicsProcessing.Config config = builder.build();

        DynamicsProcessing.Mbc mbc = config.getChannelByChannelIndex(0).getMbc();

        for(int i=0;i<8;i++){
            DynamicsProcessing.MbcBand mbcBand = mbc.getBand(i);
            mbcBand.setAttackTime(10);
            mbcBand.setReleaseTime(200);
            mbcBand.setRatio(3.0f);
            mbcBand.setKneeWidth(0.4f);
            mbcBand.setThreshold(20.0f);
            mbcBand.setNoiseGateThreshold(-30.0f);
            mbcBand.setExpanderRatio(3.0f);
            mbcBand.setPreGain(0);
            mbcBand.setPostGain(0);
        }


        DynamicsProcessing dynamicsProcessing = new DynamicsProcessing(0, audioSessionId, config);
        dynamicsProcessing.setEnabled(true);
    }
}

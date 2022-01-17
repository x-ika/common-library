package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.*;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.util.Arrays;

class AudioClipWrapper {

    private byte[] audio;

    private AudioFormat format;

    private Clip audioClip;

    boolean autoPlayMode;


    AudioClipWrapper(String fileName, boolean autoPlayMode) {
        this.autoPlayMode = autoPlayMode;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(IOUtils.getResource(fileName)));
            format = ais.getFormat();
            audio = new byte[(int) 1e5];
            audio = Arrays.copyOf(audio, ais.read(audio));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void play(boolean loop) {
        ThreadUtils.executeInNewThread(() -> {
            try {
                audioClip = AudioSystem.getClip();
                audioClip.open(format, audio, 0, audio.length);
                audioClip.loop(loop ? Integer.MAX_VALUE : 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void stop() {
        if (audioClip != null) {
            audioClip.stop();
        }
    }

}

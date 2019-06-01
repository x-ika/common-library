package com.simplejcode.commons.misc;

import java.util.*;

public class AudioSystemManager {

    private static AudioSystemManager instance = new AudioSystemManager();

    public static AudioSystemManager getInstance() {
        return instance;
    }

    //-----------------------------------------------------------------------------------
    /*
    class
     */

    private List<AudioClipWrapper> audioClips = new ArrayList<>();

    private boolean soundEnabled;


    private AudioSystemManager() {
    }


    public synchronized int loadResource(String fileName, boolean autoPlayMode) {
        int descriptor = audioClips.size();
        audioClips.add(new AudioClipWrapper(fileName, autoPlayMode));
        return descriptor;
    }

    public int loadResource(String fileName) {
        return loadResource(fileName, false);
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
        for (AudioClipWrapper clip : audioClips) {
            if (clip.autoPlayMode == soundEnabled) {
                if (soundEnabled) {
                    clip.play(true);
                } else {
                    clip.stop();
                }
            }
        }
    }

    public void play(int descriptor) {
        play(descriptor, false);
    }

    public void play(int descriptor, boolean loop) {
        if (!soundEnabled) {
            return;
        }
        audioClips.get(descriptor).play(loop);
    }

    public void stop(int descriptor) {
        audioClips.get(descriptor).stop();
    }

    public void dispose() {
        for (AudioClipWrapper clip : audioClips) {
            clip.stop();
        }
    }

}

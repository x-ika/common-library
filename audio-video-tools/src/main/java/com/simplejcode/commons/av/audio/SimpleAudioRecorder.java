package com.simplejcode.commons.av.audio;

import javax.sound.sampled.*;
import java.io.*;

public class SimpleAudioRecorder extends Thread {

    private final TargetDataLine line;
    private final AudioFileFormat.Type targetType;
    private final AudioInputStream audioInputStream;
    private final File outputFile;

    public SimpleAudioRecorder(TargetDataLine line, AudioFileFormat.Type targetType, File file) {
        this.line = line;
        audioInputStream = new AudioInputStream(line);
        this.targetType = targetType;
        outputFile = file;
    }

    public void start() {
        line.start();
        super.start();
    }

    public void stopRecording() {
        line.stop();
        line.close();
    }

    public void run() {
        try {
            AudioSystem.write(audioInputStream, targetType, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SimpleAudioRecorder create(String filename) {
        AudioFormat audioFormat = new AudioFormat(11025, 16, 1, true, true);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        return new SimpleAudioRecorder(targetDataLine, AudioFileFormat.Type.WAVE, new File(filename));

    }
}

package com.simplejcode.commons.av.videoio;

public interface ImageHandler {

    void init(int w, int h);

    void processImage(int[] image);

}

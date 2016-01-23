package com.stylingandroid.vizualiser.renderer;

import android.graphics.Canvas;

public interface WaveformRenderer {
    void render(Canvas canvas, byte[] waveform);
}

package com.stylingandroid.vizualiser.renderer;

import android.support.annotation.ColorInt;

public class RendererFactory {
    public WaveformRenderer createSimpleWaveformRenderer(@ColorInt int foreground, @ColorInt int background) {
        return SimpleWaveformRenderer.newInstance(background, foreground);
    }
}

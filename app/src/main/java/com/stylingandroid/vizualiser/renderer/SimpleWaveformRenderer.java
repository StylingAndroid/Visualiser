package com.stylingandroid.vizualiser.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;

class SimpleWaveformRenderer implements WaveformRenderer {
    private static final int Y_FACTOR = 0xFF;
    private static final float HALF_FACTOR = 0.5f;

    @ColorInt
    private final int backgroundColour;
    private final Paint foregroundPaint;
    private final Path waveformPath;

    static SimpleWaveformRenderer newInstance(int backgroundColour, int foregroundColour) {
        Paint paint = new Paint();
        paint.setColor(foregroundColour);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Path waveformPath = new Path();
        return new SimpleWaveformRenderer(backgroundColour, paint, waveformPath);
    }

    SimpleWaveformRenderer(int backgroundColour, Paint foregroundPaint, Path waveformPath) {
        this.backgroundColour = backgroundColour;
        this.foregroundPaint = foregroundPaint;
        this.waveformPath = waveformPath;
    }

    @Override
    public void render(Canvas canvas, byte[] waveform) {
        canvas.drawColor(backgroundColour);
        float height = canvas.getHeight();
        waveformPath.reset();
        if (waveform != null) {
            float xIncrement = canvas.getWidth() / (float) (waveform.length);
            float yIncrement = height / Y_FACTOR;
            int halfHeight = (int) (height * HALF_FACTOR);
            waveformPath.moveTo(0, halfHeight);
            for (int i = 1; i < waveform.length; i++) {
                float value = waveform[i] > 0 ? height - (yIncrement * waveform[i]) : -(yIncrement * waveform[i]);
                waveformPath.lineTo(xIncrement * i, value);
            }
            waveformPath.lineTo(canvas.getWidth(), halfHeight);
        } else {
            int y = (int) (height * HALF_FACTOR);
            waveformPath.moveTo(0, y);
            waveformPath.lineTo(canvas.getWidth(), y);
        }
        canvas.drawPath(waveformPath, foregroundPaint);

    }
}

package com.stylingandroid.vizualiser;

import android.Manifest;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stylingandroid.vizualiser.permissions.PermissionsActivity;
import com.stylingandroid.vizualiser.permissions.PermissionsChecker;

public class MainActivity extends AppCompatActivity implements Visualizer.OnDataCaptureListener {

    private static final int CAPTURE_SIZE = 256;
    private static final int REQUEST_CODE = 0;
    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    private Visualizer visualiser;
    private WaveformView waveformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        waveformView = (WaveformView) findViewById(R.id.waveform_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionsChecker checker = new PermissionsChecker(this);

        if (checker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            startVisualiser();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    private void startVisualiser() {
        visualiser = new Visualizer(0);
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualiser.setCaptureSize(CAPTURE_SIZE);
        visualiser.setEnabled(true);
    }

    @Override
    public void onWaveFormDataCapture(Visualizer thisVisualiser, byte[] waveform, int samplingRate) {
        if (waveformView != null) {
            waveformView.setWaveform(waveform);
        }
    }

    @Override
    public void onFftDataCapture(Visualizer thisVisualiser, byte[] fft, int samplingRate) {
        // NO-OP
    }

    @Override
    protected void onPause() {
        if (visualiser != null) {
            visualiser.setEnabled(false);
            visualiser.release();
            visualiser.setDataCaptureListener(null, 0, false, false);
        }
        super.onPause();
    }
}

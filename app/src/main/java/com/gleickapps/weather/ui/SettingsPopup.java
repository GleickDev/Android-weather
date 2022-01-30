package com.gleickapps.weather.ui;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.gleickapps.weather.R;

public class SettingsPopup extends MyPopup {

    RadioButton rbMetric, rbImperial;
    SeekBar skVolume;
    Button btOK;

    private boolean isMetric;
    private float volume;

    public SettingsPopup(MainActivity mainActivity, int resource) {
        super(mainActivity, resource);
    }

    protected void initialization() {
        rbMetric = popupView.findViewById(R.id.rb_metric);
        rbImperial = popupView.findViewById(R.id.rb_imperial);
        skVolume = popupView.findViewById(R.id.sk_volume);
        btOK = popupView.findViewById(R.id.bt_ok);

        isMetric = MainActivity.isMetric;
        volume = MainActivity.volume;

        btOK.setOnClickListener(view -> apply());

        turnMetricImperial();
        turnVolume();
    }

    // Um radio button é selecionado de cada vez
    private void turnMetricImperial() {
        if (isMetric) {
            rbMetric.setChecked(true);
            rbImperial.setChecked(false);
        } else {
            rbMetric.setChecked(false);
            rbImperial.setChecked(true);
        }

        rbMetric.setOnClickListener(v -> {
            isMetric = true;
            rbImperial.setChecked(false);
        });

        rbImperial.setOnClickListener(v -> {
            isMetric = false;
            rbMetric.setChecked(false);
        });
    }

    // O seekbar usa um intervalo inteiro de 0 a 10, mas o soundpool usa um de 0 a 0.1f,
    // por isso as conversões
    private void turnVolume() {
        int intVol = Math.round(volume * 100);
        skVolume.setProgress(intVol);

        skVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = (float) progress / 100;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void apply() {
        MainActivity.isMetric = isMetric;
        MainActivity.volume = volume;

        popupWindow.dismiss();
    }
}

package com.dhilasadrah.catalogmovie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.receiver.DailyReminder;
import com.dhilasadrah.catalogmovie.receiver.ReleaseReminder;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Switch dailySwitch, releaseSwitch;
    CardView languageSetting;
    ReleaseReminder releaseReminder;
    DailyReminder dailyReminder;
    SharedPreferences dailyAlarmPref, releaseAlarmPref;

    private static final String DAILY = "daily";
    private static final String RELEASE = "release";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        languageSetting = findViewById(R.id.language_setting);
        dailySwitch = findViewById(R.id.dailyReminderSwitch);
        releaseSwitch = findViewById(R.id.releaseReminderSwitch);

        dailySwitch.setOnCheckedChangeListener(this);
        releaseSwitch.setOnCheckedChangeListener(this);
        languageSetting.setOnClickListener(this);

        releaseReminder  = new ReleaseReminder();
        dailyReminder = new DailyReminder();

        dailyAlarmPref = getSharedPreferences(DAILY, MODE_PRIVATE);
        dailySwitch.setChecked(dailyAlarmPref.getBoolean(DAILY, false));

        releaseAlarmPref = getSharedPreferences(RELEASE, MODE_PRIVATE);
        releaseSwitch.setChecked(releaseAlarmPref.getBoolean(RELEASE, false));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case (R.id.dailyReminderSwitch):
                if (isChecked) {
                    String time = "07:00";
                    dailyReminder.setDailyAlarm(getApplicationContext(), time);
                    SharedPreferences.Editor editor = getSharedPreferences(DAILY, MODE_PRIVATE).edit();
                    editor.putBoolean(DAILY, true);
                    editor.apply();
                } else {
                    dailyReminder.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(DAILY, MODE_PRIVATE).edit();
                    editor.putBoolean(DAILY, false);
                    editor.apply();
                }
                break;

            case (R.id.releaseReminderSwitch):
                if (isChecked) {
                    String time = "08:00";
                    releaseReminder.setReleaseAlarm(getApplicationContext(), time);
                    SharedPreferences.Editor editor = getSharedPreferences(RELEASE, MODE_PRIVATE).edit();
                    editor.putBoolean(RELEASE, true);
                    editor.apply();
                } else {
                    releaseReminder.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(RELEASE, MODE_PRIVATE).edit();
                    editor.putBoolean(RELEASE, false);
                    editor.apply();
                }
                break;
        }
    }
}

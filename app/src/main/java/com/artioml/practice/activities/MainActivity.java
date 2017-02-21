package com.artioml.practice.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.artioml.practice.fragments.SettingsDialog;
import com.artioml.practice.interfaces.impl.MainSettingsChangeListener;
import com.artioml.practice.fragments.MainSettingsDialog;
import com.artioml.practice.R;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.preferences.DefaultPreferenceManager;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SETTINGS_DIALOG = "mainSettingsDialog";

    private final int ACCELERATION_THRESSHOLD = 20;
    private final int REACTION_THRESSHOLD = 10;

    private Button punchButton;

    private SoundPool soundPool;
    private SparseIntArray soundMap;

    private float currentAcceleration;
    private float maxAcceleration;
    private float reaction;
    private float count;

    private ArrayList<Float> data;
    private SettingsChangeListener settingsChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainSettingsDialog mainSettingsDialog = new MainSettingsDialog();
                mainSettingsDialog.show(getSupportFragmentManager(), SETTINGS_DIALOG);
            }
        });

        settingsChangeListener = new MainSettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settingsChangeListener.fillSettingsPanel();

        createSoundPool();

        soundMap = new SparseIntArray(1);
        soundMap.put(0, soundPool.load(getBaseContext(), R.raw.fire, 1));

        punchButton = (Button) findViewById(R.id.punchButton);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                punchButton.setEnabled(false);
                (new Handler()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        soundPool.play(soundMap.get(0), 1, 1, 1, 0, 1f);

                        reaction = 0.00f;
                        maxAcceleration = 0;
                        count = 0;

                        data = new ArrayList<>();

                        SensorManager sensorManager =
                                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

                        sensorManager.registerListener(sensorEventListener,
                                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                                1000);
                    }
                }, 2000);
            }
        });

        Button historyButton = (Button) findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
            }
        });

        Button communityButton = (Button) findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityIntent = new Intent(MainActivity.this, CommunityActivity.class);
                startActivity(communityIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent licenceIntent = new Intent(this, LicenseActivity.class);
        startActivity(licenceIntent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DefaultPreferenceManager preferenceManager = new DefaultPreferenceManager(this);
        if(preferenceManager.getFirstTimePreference()) {
            Intent licenseIntent = new Intent(this, LicenseActivity.class);
            startActivity(licenseIntent);
            preferenceManager.setFirstTimePreference(false);
        }
    }

    @Override
    protected void onResume() {
        settingsChangeListener.fillSettingsPanel();
        punchButton.setEnabled(true);
        super.onResume();
    }

    @Override
    protected  void onPause() {
        super.onPause();

        SensorManager sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
    }
    private final SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            Log.i(TAG, "onSensorChanged");
            count++;
            float x = event.values[0] ;
            float y = event.values[1];
            float z = event.values[2];

            // Сохранить предыдущие данные ускорения
            //lastAcceleration = currentAcceleration;

            // Вычислить текущее ускорение
            currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);

            if (currentAcceleration > 5)
                data.add(currentAcceleration);

            // Вычислить изменение ускорения
            //acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);

            maxAcceleration = Math.max(maxAcceleration, currentAcceleration);
            //m = Math.max(m, currentAcceleration);

            if (reaction == 0 && currentAcceleration > REACTION_THRESSHOLD)
                reaction = count / 50;

            if (maxAcceleration > ACCELERATION_THRESSHOLD && currentAcceleration < 5) {

                Intent punchResultIntent = new Intent(MainActivity.this, PunchResultActivity.class);

                punchResultIntent.putExtra("speed", countSpeed(data));
                punchResultIntent.putExtra("reaction", reaction);
                punchResultIntent.putExtra("acceleration", maxAcceleration);

                startActivity(punchResultIntent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private float countSpeed(ArrayList<Float> data) {
        float speed = 0.00f;
        Collections.reverse(data);
        if(data.size() > 0) {
            data.remove(0);
        }
        for (Float f : data) {
            speed += f;
            if (f < 3)
                return speed / 50;
        }
        return speed / 50;
    }

    protected void createSoundPool() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    @SuppressWarnings("deprecated")
    protected void createOldSoundPool() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @Override
    public void updateSettings() {
        punchButton.setEnabled(true);
        settingsChangeListener.fillSettingsPanel();
    }
}

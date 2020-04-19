package com.agbalochenterprizes.sgbaloch.knowledgequiz2018;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Activity_Rules extends AppCompatActivity {

    private TextView txtRules;
    private TextView txtRulesSet;
    private Button  btnLetStart;
    private String sql;

    private SoundPool sp;
    private int nowPlaying;
    private int idSound = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__rules);

        Bundle bundle = getIntent().getExtras();
        sql = bundle.getString("sql");

        load_Soundfx();
        loadUI();
        overridePendingTransition(0,0);

        immersiveMode();

        btnLetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.stop(nowPlaying);
                nowPlaying = sp.play(idSound, 0.6f, 0.6f, 10, 0, 1);
                Intent intent = new Intent(Activity_Rules.this, Activity_Quiz.class);
                intent.putExtra("sql", sql);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        immersiveMode();
    }

    @Override
    protected void onStart() {
        super.onStart();

        immersiveMode();
    }

    private void loadUI(){

        txtRules = (TextView) findViewById(R.id.txtHeading);
        txtRulesSet = (TextView) findViewById(R.id.txtRules);
        btnLetStart = (Button) findViewById(R.id.btn_letsStart);
    }

    private void immersiveMode(){

        final View mDecorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 19){

            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else {

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            mDecorView.setSystemUiVisibility(uiOptions);
        }

    }

    private void load_Soundfx(){

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        try{

            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("pop.ogg");
            idSound = sp.load(descriptor, 0);
        }
        catch (IOException e){

            Log.e("error", "failed to load the sound file");
        }

    }
}

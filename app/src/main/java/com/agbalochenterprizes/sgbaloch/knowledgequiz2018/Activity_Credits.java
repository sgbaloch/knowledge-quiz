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

public class Activity_Credits extends AppCompatActivity {

    private TextView txt_title;
    private TextView txt_CreditList;
    private Button btnBack;

    private SoundPool sp;
    private int nowPlaying;
    private int idSound = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__credits);

        load_Soundfx();
        loadUI();
        immersiveMode();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.stop(nowPlaying);
                nowPlaying = sp.play(idSound, 0.6f, 0.6f, 10, 0, 1);
                Intent intent = new Intent(Activity_Credits.this, MainActivity.class);
                startActivity(intent);
                finish();
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

        txt_title = (TextView) findViewById(R.id.txtCredits);
        txt_CreditList = (TextView)findViewById(R.id.txtCredit_list);
        btnBack = (Button) findViewById(R.id.btnBack);
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

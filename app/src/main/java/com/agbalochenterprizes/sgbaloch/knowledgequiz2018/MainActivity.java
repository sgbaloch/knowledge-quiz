package com.agbalochenterprizes.sgbaloch.knowledgequiz2018;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private Animation animBounce;

    private SoundPool sp;
    private int nowPlaying;
    private int idSound = -1;
    private int highScore;

    private SharedPreferences mPrefs;

    private InterstitialAd mInterstitialAd;

    private Button btn_start;
    private Button btn_credits;
    private TextView txt_title;
    private TextView txt_highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        immersiveMode();
        load_Soundfx();
        mPrefs = getSharedPreferences("Knowledge Quiz", MODE_PRIVATE);
        highScore = mPrefs.getInt("HIGH SCORE", 0);

        loadUI();

        overridePendingTransition(0,0);

        MobileAds.initialize(this, "ca-app-pub-6187255009918042~1117058630");

        animBounce = AnimationUtils.loadAnimation(this,R.anim.bounce );
        animBounce.setDuration(2000);
        btn_start.startAnimation(animBounce);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6187255009918042/5053404993");
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        load_adListener();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sp.stop(nowPlaying);
                nowPlaying = sp.play(idSound, 0.6f, 0.6f, 10, 0, 1);
                //startActivityTimer();
                Intent intent = new Intent(MainActivity.this, Activity_Categories.class);
                startActivity(intent);

            }
        });

        btn_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.stop(nowPlaying);
                nowPlaying = sp.play(idSound, 0.6f, 0.6f, 10, 0, 1);
                //startActivityTimer();
                Intent intent = new Intent(MainActivity.this, Activity_Credits.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();

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

    @Override
    public void onBackPressed() {

        if (mInterstitialAd.isLoaded()){

            mInterstitialAd.show();
        }
        else{

            showExitDialog();
        }
    }

    private void loadUI(){

        btn_start = (Button) findViewById(R.id.buttonStart);
        btn_credits = (Button) findViewById(R.id.btnCredits);
        txt_title = (TextView) findViewById(R.id.title);
        txt_highScore = (TextView) findViewById(R.id.highScore);
        txt_highScore.setText("Highscore "+ highScore);

        if (highScore == 0){

            txt_highScore.setVisibility(View.INVISIBLE);
        }
    }

    private void load_adListener(){

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                showExitDialog();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {

//                String error_code = Integer.toString(i) + " onRewardedVideoAdFailedToLoad";
//                Toast.makeText(MainActivity.this, error_code , Toast.LENGTH_SHORT).show();

                //showResult();
            }

            @Override
            public void onAdLeftApplication() {
                //showResult();
            }

            @Override
            public void onAdOpened() {


            }
        });
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

    private void showExitDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                immersiveMode();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

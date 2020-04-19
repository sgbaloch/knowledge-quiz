package com.agbalochenterprizes.sgbaloch.knowledgequiz2018;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

public class Activity_Categories extends AppCompatActivity {

    String[] categoryArray = {"Random","History","Geography","Technology",
            "Science","Sports","Politics","Other General"};

    TextView txt_Category;
    ListView list;

    private SoundPool sp;
    private int nowPlaying;
    private int idSound = -1;

    private String sql;
    private Intent intent;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__categories);

        immersiveMode();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6187255009918042/7296424956");
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        load_adListener();

        load_Soundfx();
        loadUI();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listitem,categoryArray ){

            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

                View view = super.getView(position, convertView, parent);
                switch (position){

                    case 0:
                        view.setBackgroundResource(R.drawable.shape_category1);
                        break;
                    case 1:
                        view.setBackgroundResource(R.drawable.shape_category2);
                        break;
                    case 2:
                        view.setBackgroundResource(R.drawable.shape_category3);
                        break;
                    case 3:
                        view.setBackgroundResource(R.drawable.shape_category4);
                        break;
                    case 4:
                        view.setBackgroundResource(R.drawable.shape_category5);
                        break;
                    case 5:
                        view.setBackgroundResource(R.drawable.shape_category6);
                        break;
                    case 6:
                        view.setBackgroundResource(R.drawable.shape_category7);
                        break;
                    case 7:
                        view.setBackgroundResource(R.drawable.shape_category8);

                        break;

                }
                return view;
            }
        };

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position){

                    case 0:
                        playSoundFx();
                        sql = "SELECT * FROM Questions ORDER BY RANDOM()";
//                        intent = new Intent(Activity_Categories.this, Activity_Rules.class);
//                        intent.putExtra("sql", sql);
//                        startActivity(intent);
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }

                        break;

                    case 1:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'History' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 2:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Geography' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 3:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Technology' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 4:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Science' ORDER BY RANDOM();";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 5:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Sports' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 6:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Politics' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                    case 7:
                        playSoundFx();
                        sql = "SELECT * FROM Questions WHERE Category = 'Other' ORDER BY RANDOM()";
                        if(mInterstitialAd.isLoaded()){

                            mInterstitialAd.show();
                        }
                        else{

                            startQuizActivityWithCategory(sql);
                        }
                        break;

                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        immersiveMode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        immersiveMode();


    }

    void loadUI(){

        txt_Category = (TextView) findViewById(R.id.txtCategories);
        list = (ListView) findViewById(R.id.listView);

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

    private void playSoundFx(){

        sp.stop(nowPlaying);
        nowPlaying = sp.play(idSound, 0.6f, 0.6f, 10, 0, 1);
    }

    public void startQuizActivityWithCategory(String sqlCategory){
        intent = new Intent(Activity_Categories.this, Activity_Rules.class);
        intent.putExtra("sql", sqlCategory);
        startActivity(intent);
    }

    private void load_adListener(){

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                startQuizActivityWithCategory(sql);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {

//                String error_code = Integer.toString(i) + " onRewardedVideoAdFailedToLoad";
//                Toast.makeText(Activity_Categories.this, error_code , Toast.LENGTH_SHORT).show();

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
}

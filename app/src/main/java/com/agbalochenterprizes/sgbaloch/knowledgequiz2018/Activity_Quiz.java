package com.agbalochenterprizes.sgbaloch.knowledgequiz2018;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.IOException;

public class Activity_Quiz extends AppCompatActivity implements RewardedVideoAdListener {

    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;

    private Button btn_watch, btn_thanks;

    private TextView score;
    private TextView question;
    private TextView txtTimer;
    private TextView txtQnumber;
    private TextView noOfLives;
    private ImageView img_heart;

    private int scr;
    private int streak = 0;
    private int lives = 5;
    private int qNmbr = 1;
    private int btn_id_pressed;
    private int btn_id_correct;
    private int highScore;

    private String result = "Game Over";
    private String comments= "Your score is:";

    private Boolean endQuiz = false;
    private Boolean AdShown = false;
    private Boolean isRewardedVideoAdFinished = false;

    private Button btnGreen;
    private Button btn;

    private Animation animFade_in;
    private Animation animFlash;
    private Animation animLeftRight;
    private Animation animRightLeft;
    private Animation animBounce;
    private Animation animRotateRight;

    private SoundPool sp;
    private int nowPlaying;
    private int fxCorrect;
    private int fxIncorrect;
    private int fxClock;
    private int fxBonus = -1;
    private Intent svc;

    private String sql;
    private String correctAnswer;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private TestAdapter mDBadapter;
    private Cursor cursor_data;
    private CountDownTimer cTimer = null;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    private Dialog rewardDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__quiz);

        Bundle bundle = getIntent().getExtras();
        sql = bundle.getString("sql");

        mPrefs = getSharedPreferences("Knowledge Quiz", MODE_PRIVATE);
        mEditor = mPrefs.edit();
        highScore = mPrefs.getInt("HIGH SCORE", 0);

        attachDB();
        immersiveMode();
        loadSoundFx();
        loadUI();
        loadAnimations();

        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-6187255009918042/6419332685");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        load_adListener();


        cursor_data = getCursorData(sql);
        cursor_data.moveToFirst();

        nextQuestion();
        startTimer();

        svc = new Intent(this, BackgroundMusicService.class);
        playMusic();

        mAdView = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("F146EC1404C84BC37D536EFED9D949B0")
                .build();
        mAdView.loadAd(adRequest);


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                buttonClickOff();
                btn_id_pressed = choice1.getId();
                choice1.setBackgroundResource(R.drawable.shape_select);
                choice1.startAnimation(animFlash);
                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        updateScore(choice1.getText().toString());
                        //nmbr++;
                        new CountDownTimer(800, 800) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                //cursor_data.moveToNext();
                                animateAll();

                                choice1.setBackgroundResource(R.drawable.shape_button3);
                                btnGreen.setBackgroundResource(R.drawable.shape_button3);

                                new CountDownTimer(600, 600) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                        if(isGameOver()){

                                            stopMusic();
                                            if (mRewardedVideoAd.isLoaded()) {
                                                showRewardedAdDialogue();
                                            }

                                            else{
                                                setHighScore();
                                                showAd_withResult();
                                                immersiveMode();
                                            }
                                            //cancelTimer();

                                        }

                                        else if(isEndOfQuestions()){

                                            stopMusic();
                                            setHighScore();
                                            showAd_withResult();
                                            immersiveMode();
                                        }
                                        else{
                                            //cursor_data.moveToNext();
                                            nextQuestion();
                                            startTimer();
                                            buttonClickOn();
                                        }
                                    }
                                }.start();

                            }
                        }.start();
                    }
                }.start();


            }
        });


        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                buttonClickOff();
                btn_id_pressed = choice2.getId();
                choice2.setBackgroundResource(R.drawable.shape_select);
                choice2.startAnimation(animFlash);
                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        updateScore(choice2.getText().toString());
                        //nmbr++;
                        new CountDownTimer(800, 800) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                //cursor_data.moveToNext();
                                animateAll();

                                choice2.setBackgroundResource(R.drawable.shape_button3);
                                btnGreen.setBackgroundResource(R.drawable.shape_button3);

                                new CountDownTimer(600, 600) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                        if(isGameOver()){

                                            stopMusic();
                                            if (mRewardedVideoAd.isLoaded()) {
                                                showRewardedAdDialogue();
                                            }

                                            else{
                                                setHighScore();
                                                showAd_withResult();
                                                immersiveMode();
                                            }
                                            //cancelTimer();

                                        }

                                        else if(isEndOfQuestions()){

                                            stopMusic();
                                            setHighScore();
                                            showAd_withResult();
                                            immersiveMode();
                                        }
                                        else{
                                            //cursor_data.moveToNext();
                                            nextQuestion();
                                            startTimer();
                                            buttonClickOn();
                                        }
                                    }
                                }.start();

                            }
                        }.start();

                    }
                }.start();

            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                buttonClickOff();
                btn_id_pressed = choice3.getId();
                choice3.setBackgroundResource(R.drawable.shape_select);
                choice3.startAnimation(animFlash);

                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        updateScore(choice3.getText().toString());
                        //nmbr++;
                        new CountDownTimer(800, 800) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                //cursor_data.moveToNext();
                                animateAll();

                                choice3.setBackgroundResource(R.drawable.shape_button3);
                                btnGreen.setBackgroundResource(R.drawable.shape_button3);

                                new CountDownTimer(600, 600) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                        if(isGameOver()){

                                            stopMusic();
                                            if (mRewardedVideoAd.isLoaded()) {
                                                showRewardedAdDialogue();
                                            }

                                            else{
                                                setHighScore();
                                                showAd_withResult();
                                                immersiveMode();
                                            }
                                            //cancelTimer();

                                        }

                                        else if(isEndOfQuestions()){

                                            stopMusic();
                                            setHighScore();
                                            showAd_withResult();
                                            immersiveMode();
                                        }
                                        else{
                                            //cursor_data.moveToNext();
                                            nextQuestion();
                                            startTimer();
                                            buttonClickOn();
                                        }
                                    }
                                }.start();

                            }
                        }.start();

                    }
                }.start();

            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                buttonClickOff();
                btn_id_pressed = choice4.getId();
                choice4.setBackgroundResource(R.drawable.shape_select);
                choice4.startAnimation(animFlash);
                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        updateScore(choice4.getText().toString());
                        //nmbr++;
                        new CountDownTimer(800, 800) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {


                                animateAll();

                                choice4.setBackgroundResource(R.drawable.shape_button3);
                                btnGreen.setBackgroundResource(R.drawable.shape_button3);

                                new CountDownTimer(600, 600) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                        if(isGameOver()){

                                            stopMusic();
                                            if (mRewardedVideoAd.isLoaded()) {
                                                showRewardedAdDialogue();
                                            }

                                            else{
                                                setHighScore();
                                                showAd_withResult();
                                                immersiveMode();
                                            }
                                            //cancelTimer();

                                        }

                                        else if(isEndOfQuestions()){

                                            stopMusic();
                                            setHighScore();
                                            showAd_withResult();
                                            immersiveMode();
                                        }
                                        else{
                                            //cursor_data.moveToNext();
                                            nextQuestion();
                                            startTimer();
                                            buttonClickOn();
                                        }

                                    }
                                }.start();

                            }
                        }.start();

                    }
                }.start();



            }
        });




    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-6187255009918042/8747037185",
                new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        immersiveMode();
        //nextQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        immersiveMode();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!AdShown){

            startService(svc);
            startTimer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(svc);
        cancelTimer();
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopService(svc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(svc);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to leave this quiz?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Activity_Quiz.this, MainActivity.class);
                startActivity(intent);
                finish();
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

    private void loadUI(){

        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);

        score = (TextView) findViewById(R.id.textview_score);
        question = (TextView) findViewById(R.id.textView_question);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtQnumber = (TextView) findViewById(R.id.textview_qNo);
        img_heart = (ImageView) findViewById(R.id.img_heart);
        noOfLives = (TextView) findViewById(R.id.noflives);



        score.setText("Score " + String.valueOf(scr));
        noOfLives.setText(String.valueOf(lives));
    }

    private  boolean isGameOver(){

        boolean gameOver = false;

        if(lives == 0){

            gameOver = true;
        }

        return gameOver;

    }

    private boolean isEndOfQuestions(){

        boolean endOfQuestions = false;

        if(cursor_data.isAfterLast()){

            endOfQuestions = true;
        }

        return endOfQuestions;

    }

//    private void nextQuestion(){
//        if(cursor_data.isAfterLast()){
//            cancelTimer();
//            endQuiz = true;
//            setHighScore();
//            showAd_withResult();
//        }
//        else if(lives == 0){
//
//            cancelTimer();
//            endQuiz = true;
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(false);
//            builder.setMessage("Watch this video ad to earn 2 extra lives");
//
//            builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //if user pressed "yes", then he is allowed to exit from application
//
//                }
//            });
//            builder.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //if user select "No", just cancel this dialog and continue with app
//
//
//                    setHighScore();
//                    showAd_withResult();
//                    immersiveMode();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//
//
//
//        }
//        else{
//
//            txtQnumber.setText("Question " + String.valueOf(qNmbr));
//
//            question.setText(cursor_data.getString(1));
//
//            choice1.setText(cursor_data.getString(2));
//            choice2.setText(cursor_data.getString(3));
//            choice3.setText(cursor_data.getString(4));
//            choice4.setText(cursor_data.getString(5));
//
//            qNmbr++;
//        }
//
//    }

    private  void nextQuestion()
    {
        txtQnumber.setText("Question " + String.valueOf(qNmbr));

        question.setText(cursor_data.getString(1));

        choice1.setText(cursor_data.getString(2));
        choice2.setText(cursor_data.getString(3));
        choice3.setText(cursor_data.getString(4));
        choice4.setText(cursor_data.getString(5));
        correctAnswer = cursor_data.getString(6).toString();

        qNmbr++;
        cursor_data.moveToNext();
    }

    private void showRewardedAdDialogue(){

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setMessage("Watch this video ad to earn 2 extra lives");
//
//        LayoutInflater inflater = getLayoutInflater();
//        //builder.setView(inflater.inflate(R.layout.custom_dialog), null);
//        //View view = ((Activity) getBaseContext()).getLayoutInflater().inflate(R.layout.custom_dialog, null, false);
//
//        builder.setView(R.layout.custom_dialog);
//
//        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user pressed "yes", then...
//
//
//                if (mRewardedVideoAd.isLoaded()) {
//                    mRewardedVideoAd.show();
//                }
//
//
//            }
//        });
//        builder.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user select "No", just cancel this dialog and continue with app
//
//
//                setHighScore();
//                showAd_withResult();
//                //immersiveMode();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();

        rewardDialog = new Dialog(this);
        rewardDialog.setContentView(R.layout.custom_dialog);
        rewardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rewardDialog.setCanceledOnTouchOutside(false);

        btn_watch = (Button) rewardDialog.findViewById(R.id.btn_watch);
        btn_thanks = (Button) rewardDialog.findViewById(R.id.btn_thanks);

        btn_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardDialog.dismiss();
                mRewardedVideoAd.show();
            }
        });

        btn_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardDialog.dismiss();
                setHighScore();
                showAd_withResult();
            }
        });

        rewardDialog.show();



    }



    private void updateScore(String ans){

        setBtn_id_correct();
        //String cAnswer = cursor_data.getString(6).toString();
        btn = (Button) findViewById(btn_id_pressed);
        btnGreen = (Button) findViewById(btn_id_correct);

        if(ans.equals(correctAnswer)){

            btn.setBackgroundResource(R.drawable.shape_green);
            sp.stop(nowPlaying);
            nowPlaying = sp.play(fxCorrect,0.5f,0.5f,1,0,1);
            scr = scr + Integer.parseInt(txtTimer.getText().toString());
            score.setText("Score " + String.valueOf(scr));
            streak++;
            streakBonus();
        }
        else{

            btn.setBackgroundResource(R.drawable.shape_red);
            sp.stop(nowPlaying);
            nowPlaying = sp.play(fxIncorrect,0.5f,0.5f,1,0,1);
            btnGreen.setBackgroundResource(R.drawable.shape_green);
            lives--;
            noOfLives.setText(String.valueOf(lives));
            streak = 0;

        }

    }

    private void showResult(){

           /* AlertDialog.Builder alertDialogueBuild = new AlertDialog.Builder(Activity_Quiz.this);

            alertDialogueBuild
                    .setMessage("Quiz Complete! Your score is " + scr)
                    .setCancelable(false)
                    .setPositiveButton("Restart",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Intent intent_quiz2 = new Intent(Activity_Quiz.this,Activity_Quiz.class);
                                    startActivity(new Intent(Activity_Quiz.this, Activity_Quiz.class));

                                }
                            })
                    .setNegativeButton("Go To Menu",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    startActivity(new Intent(Activity_Quiz.this, MainActivity.class));
                                }
                            });

            AlertDialog AlertD = alertDialogueBuild.create();
            AlertD.show(); */

            Intent intent = new Intent(Activity_Quiz.this, Activity_Result.class);
            intent.putExtra("title", result);
            intent.putExtra("score", scr);
            intent.putExtra("comments", comments);
            startActivity(intent);

    }

    private void attachDB(){

        mDBadapter = new TestAdapter(Activity_Quiz.this);
        mDBadapter.createDatabase();

    }

    private Cursor getCursorData(String query){

        Cursor c;

        mDBadapter.open();

        c = mDBadapter.getData(query);

        mDBadapter.close();

        return c;
    }

    private void startTimer() {
       cTimer = new CountDownTimer(31000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                sp.stop(nowPlaying);
                txtTimer.setText((String.valueOf (millisUntilFinished / 1000)));
                nowPlaying = sp.play(fxClock,0.6f,0.6f,1,0,1);

            }

            public void onFinish() {
                sp.stop(nowPlaying);
                nowPlaying = sp.play(fxIncorrect,0.5f,0.5f,1,0,1);
                animateAll();
                //cursor_data.moveToNext();
                nextQuestion();
                lives--;
                noOfLives.setText(String.valueOf(lives));
                new CountDownTimer(600, 600) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if(cursor_data.isAfterLast()){
                            endQuiz = true;
                            setHighScore();
                            showAd_withResult();
                        }
                        else if(lives == 0){

                            endQuiz = true;
                            if (mRewardedVideoAd.isLoaded()) {
                                showRewardedAdDialogue();
                            }

                            else{
                                setHighScore();
                                showAd_withResult();
                                immersiveMode();
                            }

                        }
                        else {

                            cTimer.start();
                        }

                    }
                }.start();

            }
        };
        cTimer.start();
    }


    //cancel timer
    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void buttonClickOff(){

        choice1.setClickable(false);
        choice2.setClickable(false);
        choice3.setClickable(false);
        choice4.setClickable(false);
    }

    private void buttonClickOn(){

        choice1.setClickable(true);
        choice2.setClickable(true);
        choice3.setClickable(true);
        choice4.setClickable(true);
    }

    private void setBtn_id_correct(){

        //String correctAnswer = cursor_data.getString(6).toString();
        if(correctAnswer.equals(choice1.getText().toString())){

            btn_id_correct = choice1.getId();

        }
        else if(correctAnswer.equals(choice2.getText().toString())){

            btn_id_correct = choice2.getId();

        }
        else if(correctAnswer.equals(choice3.getText().toString())){

            btn_id_correct = choice3.getId();

        }
        else{

            btn_id_correct = choice4.getId();
        }
    }

    private void loadSoundFx(){

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        try{

            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("arpeggio.ogg");
            fxCorrect = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("serious.ogg");
            fxIncorrect = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep1.ogg");
            fxClock = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("win-spacey.wav");
            fxBonus = sp.load(descriptor, 0);
        }
        catch (IOException e){

            Log.e("error", "failed to load the sound file");
        }

    }

    private void loadAnimations(){

        animFade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        animFade_in.setDuration(3000);
        animFlash = AnimationUtils.loadAnimation(this,R.anim.flash);
        animFlash.setDuration(1000 / 4);
        animLeftRight = AnimationUtils.loadAnimation(this,R.anim.left_right);
        animLeftRight.setDuration(600);
        animRightLeft = AnimationUtils.loadAnimation(this,R.anim.right_left);
        animRightLeft.setDuration(600);
        animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animBounce.setDuration(20000 / 10);
        animRotateRight = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
        animRotateRight.setDuration(1000);
    }

    private void animateAll(){

        choice1.startAnimation(animLeftRight);
        choice2.startAnimation(animRightLeft);
        choice3.startAnimation(animLeftRight);
        choice4.startAnimation(animRightLeft);
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

    private void setHighScore(){

        int mscore = scr;
        if(mscore > highScore){

            mEditor.putInt("HIGH SCORE", mscore);
            mEditor.commit();
            result = "New Highscore";
            comments = "Congratulations! You have set a new highscore!";
        }
    }

    private void streakBonus(){

        if (streak == 4){

            if(lives < 5){

                lives++;
                new CountDownTimer(1500, 1500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        sp.stop(nowPlaying);
                        nowPlaying = sp.play(fxBonus,0.7f,0.7f,1,0,1);
                        noOfLives.startAnimation(animRotateRight);
                        noOfLives.setText(String.valueOf(lives));
                    }
                }.start();

            }

            streak = 0;
        }
    }

    private void showAd_withResult(){

        if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
                AdShown = true;
        }
        else{
            showResult();
        }
    }

    private void load_adListener(){

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                showResult();
            }

            @Override
            public void onAdFailedToLoad(int i) {

//                String error_code = Integer.toString(i) + " mInterstitialFailedToLoad";
//                Toast.makeText(Activity_Quiz.this, error_code , Toast.LENGTH_SHORT).show();
                //showResult();
            }

            @Override
            public void onAdLeftApplication() {
                //showResult();
            }

            @Override
            public void onAdOpened() {

                stopService(svc);
            }
        });
    }

    private void playMusic(){

        new CountDownTimer(100,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                startService(svc);
            }
        }.start();
    }

    private void stopMusic(){

        stopService(svc);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_LONG).show();
        if(isRewardedVideoAdFinished){
            lives = lives + 2;
            noOfLives.setText(String.valueOf(lives));
            nextQuestion();
            buttonClickOn();
            startTimer();
        }

        else{
            showResult();
        }

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Toast.makeText(this, "onRewardedCalled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        //String error_code = Integer.toString(i) + " onRewardedVideoAdFailedToLoad";
        //Toast.makeText(this, error_code , Toast.LENGTH_SHORT).show();

        //loadRewardedVideoAd();

    }

    @Override
    public void onRewardedVideoCompleted() {

//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
        isRewardedVideoAdFinished = true;
    }
}

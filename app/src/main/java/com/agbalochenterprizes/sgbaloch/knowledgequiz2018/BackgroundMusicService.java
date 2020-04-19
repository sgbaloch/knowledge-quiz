package com.agbalochenterprizes.sgbaloch.knowledgequiz2018;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by Sarfaraz on 12/23/2017.
 */

public class BackgroundMusicService extends Service {

    private MediaPlayer mplayer;

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        mplayer = MediaPlayer.create(this, R.raw.lost);
        mplayer.setLooping(true); // Set looping
        mplayer.setVolume(0.9f,0.9f);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mplayer.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        mplayer.start();
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
        mplayer.stop();
    }
    public void onPause() {
        mplayer.pause();
    }
    @Override
    public void onDestroy() {
        mplayer.stop();
        mplayer.release();
    }

    @Override
    public void onLowMemory() {

    }
}

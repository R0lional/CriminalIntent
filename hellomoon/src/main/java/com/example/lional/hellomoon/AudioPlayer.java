package com.example.lional.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by skysoft on 2017/12/18.
 */

public class AudioPlayer extends MediaPlayer {
    private MediaPlayer mPlayer;

    public void play(Context context) {
        stop();
        mPlayer = MediaPlayer.create(context, R.raw.sky_city);
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });
        mPlayer.start();
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}

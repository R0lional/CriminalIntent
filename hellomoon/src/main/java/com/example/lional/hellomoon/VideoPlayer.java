package com.example.lional.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by skysoft on 2017/12/19.
 */

public class VideoPlayer extends MediaPlayer {
    private static final String TAG = "VideoPlayer";
    private MediaPlayer mPlayer = null;

    public VideoPlayer(Context context, SurfaceHolder holder) {
        mPlayer = MediaPlayer.create(context, R.raw.video);
        mPlayer.setDisplay(holder);
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceCreated");
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceDestroyed");
            }
        });
    }

    public void play() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        } else {
            mPlayer.pause();
        }
    }

    public void stop(){
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }
}

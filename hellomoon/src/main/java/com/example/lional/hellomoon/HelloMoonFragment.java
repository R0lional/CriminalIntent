package com.example.lional.hellomoon;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by skysoft on 2017/12/18.
 */

public class HelloMoonFragment extends Fragment{
    private Button mPlayButton;
    private Button mStopButton;
    private SurfaceView mSurfaceView;
    private AudioPlayer mAudioPlayer = new AudioPlayer();
    private VideoPlayer mVideoPlayer = null;

    private void prepare() {
        if (mVideoPlayer == null) {
            mVideoPlayer = new VideoPlayer(getContext(), mSurfaceView.getHolder());
            mVideoPlayer.getmPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    videoStopPlay();
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hello_moon, container, false);

        mSurfaceView = v.findViewById(R.id.vedio_view);
        mPlayButton = v.findViewById(R.id.play_button);
        mStopButton = v.findViewById(R.id.stop_button);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPlayButtonClick();
                //audioPlay();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoStopPlay();
                //audioStop();
            }
        });

        return v;
    }

    private void videoPlayButtonClick() {
        prepare();
        if (!mVideoPlayer.isPlaying()) {
            mVideoPlayer.play();
            mPlayButton.setText(R.string.hello_moon_pause);
        } else {
            mVideoPlayer.pause();
            mPlayButton.setText(R.string.hello_moon_play);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioPlayer.stop();
        videoStopPlay();
    }

    private void videoStopPlay() {
        if (mVideoPlayer != null) {
            mVideoPlayer.stop();
            mVideoPlayer = null;
            mPlayButton.setText(R.string.hello_moon_play);
        }
    }

    private void audioPlay() {
        mAudioPlayer.play(getContext());
    }

    private void audioStop() {
        mAudioPlayer.stop();
    }
}


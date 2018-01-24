package com.example.lional.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by lional on 2018/1/8.
 */

public class CrimeFragmentCamera extends Fragment implements SurfaceHolder.Callback{
    private static final String TAG = "CrimeFragmentCamera";
    public static final String EXTRA_PHOTO_NAME = "com.example.lional.criminalintent.crime_photo";
    public static final int CAMERA_REQUEST_CODE = 0;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private FloatingActionButton mFloatingActionButton;

    public static CrimeFragmentCamera newInstance(UUID crimeID) {

        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimeID);

        CrimeFragmentCamera fragment = new CrimeFragmentCamera();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);

        mFloatingActionButton = v.findViewById(R.id.take_picture_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });
        mSurfaceView = v.findViewById(R.id.crime_camera_surfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

        return v;
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            UUID id = (UUID) getArguments().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
            String fileName = id.toString() + ".jpg";
            FileOutputStream os = null;
            boolean success = true;

            try {
                os = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                os.write(bytes);
            } catch (FileNotFoundException e) {
                success = false;
                e.printStackTrace();
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        success = false;
                        e.printStackTrace();
                    }
                }
            }

            if (success) {
                Toast.makeText(getContext(), "Picture saved: " + fileName, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_NAME, fileName);
                getActivity().setResult(Activity.RESULT_OK, intent);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestSupportSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size result = sizes.get(0);
        int largestArea = 0;
        for (Camera.Size size: sizes) {
            int area = size.width * size.height;
            if (area >= largestArea) {
                result = size;
                largestArea = area;
            }
        }

        Log.d(TAG, "result: " + result.width + " x " + result.height);
        return result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        try {
            if (mCamera == null) {
                mCamera = Camera.open();
            }
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            Log.d(TAG, "IOException e: " + e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged mCamera:" + mCamera);
        if (mCamera == null) return;

        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size supportSize = getBestSupportSize(parameters.getSupportedVideoSizes(), i1, i2);
        parameters.setPreviewSize(supportSize.width, supportSize.height);
        parameters.setPictureSize(supportSize.width, supportSize.height);

        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceChanged");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }


}

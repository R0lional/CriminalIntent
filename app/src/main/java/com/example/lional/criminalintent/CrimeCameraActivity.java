package com.example.lional.criminalintent;

import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Created by skysoft on 2018/1/8.
 */

public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        UUID crimeID = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragmentCamera.newInstance(crimeID);
    }

    @Override
    public void requestHideTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }
}

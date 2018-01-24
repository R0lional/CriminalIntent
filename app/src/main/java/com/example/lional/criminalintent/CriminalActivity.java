package com.example.lional.criminalintent;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CriminalActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        UUID crimeID = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeID, getSupportActionBar());
    }

    @Override
    public void requestHideTitle() {

    }
}

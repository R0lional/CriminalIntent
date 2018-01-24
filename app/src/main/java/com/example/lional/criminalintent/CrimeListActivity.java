package com.example.lional.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new CrimeListFragment(getSupportActionBar());
    }

    @Override
    public void requestHideTitle() {

    }
}

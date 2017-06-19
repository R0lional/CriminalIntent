package com.example.lional.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by lional on 17-5-12.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}

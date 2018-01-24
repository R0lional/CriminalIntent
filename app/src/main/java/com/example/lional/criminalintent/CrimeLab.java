package com.example.lional.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab (Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e);
            e.printStackTrace();
        } finally {
            if (mCrimes == null) {
                mCrimes = new ArrayList<Crime>();
            }
        }
    }

    public static CrimeLab get(Context c) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getmId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public void saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "mCrimes saved to file: " + FILENAME);
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e);
            e.printStackTrace();
        }
    }
}

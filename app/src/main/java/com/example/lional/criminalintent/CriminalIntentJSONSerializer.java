package com.example.lional.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context context, String filename) {
        mContext = context;
        mFileName = filename;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException{
        JSONArray array = new JSONArray();
        for (Crime c: crimes) {
            array.put(c.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream outputStream = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(array.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (writer != null) {
            writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws JSONException, IOException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();

        InputStream inputStream = mContext.openFileInput(mFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        JSONArray array = (JSONArray) new JSONTokener(jsonBuilder.toString()).nextValue();
        for (int i = 0; i < array.length(); i++) {
            crimes.add(new Crime(array.getJSONObject(i)));
        }

        if (reader != null) {
            reader.close();
        }
        return crimes;
    }
}

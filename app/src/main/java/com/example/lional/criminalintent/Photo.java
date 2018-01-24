package com.example.lional.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lional on 2018/1/11.
 */

public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFileName;

    public Photo(String fileName) {
        mFileName = fileName;
    }

    public Photo(JSONObject json) throws JSONException {
        mFileName = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFileName);
        return json;
    }

    public String getFileName() {
        return mFileName;
    }
}

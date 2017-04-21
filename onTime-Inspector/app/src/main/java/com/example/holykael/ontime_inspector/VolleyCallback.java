package com.example.holykael.ontime_inspector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Holykael on 4/17/2017.
 */

public interface VolleyCallback {
    void onSuccess();
    void onSuccess(JSONObject o);
}

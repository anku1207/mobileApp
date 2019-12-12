package com.uav.rof.volley;

import org.json.JSONException;

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response) throws JSONException;
}

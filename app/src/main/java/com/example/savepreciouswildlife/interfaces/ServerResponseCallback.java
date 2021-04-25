package com.example.savepreciouswildlife.interfaces;

import org.json.JSONObject;

public interface ServerResponseCallback {
    void onJSONResponse(JSONObject jsonObject);
//    void onJSONArrayResponse(JSONArray jsonArray);
    void onError(Exception e);
}

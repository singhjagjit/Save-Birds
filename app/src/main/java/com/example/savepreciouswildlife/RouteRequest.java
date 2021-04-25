package com.example.savepreciouswildlife;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.savepreciouswildlife.interfaces.ServerResponseCallback;

import org.json.JSONObject;

public class RouteRequest {
    public void sendPOSTRequestToServer(Context context, final JSONObject jsonObject, String URL, final ServerResponseCallback serverResponseCallback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        serverResponseCallback.onJSONResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RR", "onErrorResponse: ", error);serverResponseCallback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}

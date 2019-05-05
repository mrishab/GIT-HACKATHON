package com.example.vendor;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class JsonRequest {

    public RequestQueue queue;

    public JsonRequest(Context ctx){
        queue = Volley.newRequestQueue(ctx);
    }

    public void request(String url, int method, JSONObject body, Response.Listener<JSONObject> callback, Response.ErrorListener errorListener){
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(method, url, body, callback, errorListener);
        queue.add(jsonObjectRequest);
    }

    public void post(String url, JSONObject body, Response.Listener<JSONObject> callback, Response.ErrorListener errorListener) {
        request(url, POST, body, callback, errorListener);
    }

    public void get(String url, Response.Listener<JSONObject> callback, Response.ErrorListener errorListener){
        request(url, GET, null, callback, errorListener);
    }


}

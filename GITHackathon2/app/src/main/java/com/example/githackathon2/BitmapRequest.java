package com.example.githackathon2;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class BitmapRequest {

    private static final int MAX_HEIGHT = 1000, MAX_WIDTH = 1000;
    private static final ImageView.ScaleType SCALE = ImageView.ScaleType.CENTER_CROP;
    private static final Bitmap.Config CONFIG = Bitmap.Config.RGB_565;

    private RequestQueue queue;

    public BitmapRequest(Context ctx){
        queue = Volley.newRequestQueue(ctx);
    }

    public void request(String url, Response.Listener<Bitmap> callback, Response.ErrorListener errorListener){

        ImageRequest imageRequest = new ImageRequest(url, callback, MAX_WIDTH, MAX_HEIGHT, SCALE, CONFIG, errorListener);
        queue.add(imageRequest);
    }
}

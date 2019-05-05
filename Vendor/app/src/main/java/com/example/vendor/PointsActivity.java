package com.example.vendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PointsActivity extends AppCompatActivity {

    private static final String pointsUrl = "https://iugis.serveo.net/";

    private TextView uuid, points;
    private JSONObject totalPoints;
    private JSONObject oldPoints;
    private ImageView container, cup, cutlery, bag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        initViews();
        String currentUuid = getIntent().getExtras().getString("user_id");
        uuid.setText(currentUuid);
        loadPoints();
        reset(null);
    }

    private void initViews(){
        uuid = findViewById(R.id.uuid);
        points = findViewById(R.id.points);
        container = findViewById(R.id.container);
        cutlery = findViewById(R.id.cutlery);
        bag = findViewById(R.id.bag);
        cup = findViewById(R.id.cup);
    }

    public void loadPoints(){
        JSONObject body = new JSONObject();
        try {
            body.put("user_id", this.uuid.getText().toString());
            new JsonRequest(this).post(pointsUrl + "user_timeline", body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {
                    try {
                        oldPoints = response.getJSONObject("body");
                        oldPoints = test(oldPoints);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Cannot parse points", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Failed to get points", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to make a JSON", Toast.LENGTH_LONG).show();
        }
    }

    public void submit(View v) {
    try {
        JSONObject body = add(totalPoints, oldPoints);
        body.put("user_id", this.uuid.getText().toString());
        reset(null);
        new JsonRequest(this).post(pointsUrl + "save_rewards", body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Points updated successfully!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Cannot connect to server", Toast.LENGTH_LONG).show();
                Log.d("Error", error.networkResponse.statusCode + "");
            }
        });
    } catch (JSONException e) {
        Toast.makeText(getApplicationContext(), "Could not make JSON", Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}

    public void reset(View v) {
        this.totalPoints = new JSONObject();
        this.totalPoints = test(totalPoints);
        this.points.setText("Current Points: " + 0);
    }

    public void incrementPoints(String key, int increment){
        try {
            totalPoints.put(key, totalPoints.getInt(key) + increment);
            points.setText("Current Points: " + total(totalPoints));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to add points for " + key, Toast.LENGTH_LONG).show();
        }
    }

    public void incrementContainer(View v){
        incrementPoints("container", 500);
    }
    public void incrementCup(View v){
        incrementPoints("cup",300);
    }
    public void incrementCutlery(View v){
        incrementPoints("cutlery", 200);
    }
    public void incrementBags(View v){
        incrementPoints("bag", 100);
    }

    private int total (JSONObject ob) {
        int total = 0;
        try {
            Iterator<String> keys = ob.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int value = ob.getInt(key);
                total += value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot add points", Toast.LENGTH_LONG).show();
        }
        return total;
    }

    public JSONObject add(JSONObject ob, JSONObject ob2) {
        JSONObject ret = new JSONObject();
        try {
            Iterator<String> keys = ob.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int value = ob.getInt(key);
                int value2 = ob2.getInt(key);
                ret.put(key, value + value2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot add points", Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public JSONObject test(JSONObject o){
        try {
            test(o, "cutlery");
            test(o, "cup");
            test(o, "bag");
            test(o, "container");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot initialize the points", Toast.LENGTH_LONG).show();
        }
        return o;
    }

    private void test (JSONObject o, String k) throws JSONException {
        if (!o.has(k)) o.put(k, 0);
    }
}

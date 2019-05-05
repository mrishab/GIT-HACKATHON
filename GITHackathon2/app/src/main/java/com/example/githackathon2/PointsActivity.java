package com.example.githackathon2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PointsActivity  extends AppCompatActivity {

    private static final String pointsUrl = "https://iugis.serveo.net/";

    private String uuid;
    private AnyChartView chart;
    private ImageView reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        uuid = getIntent().getExtras().getString("user_id");
        chart = findViewById(R.id.any_chart_view);
        reload = findViewById(R.id.reload);
        reload(null);
    }

    public void getData(){
        JSONObject body = new JSONObject();
        try {
            body.put("user_id", this.uuid);
            new JsonRequest(this).post(pointsUrl + "user_timeline", body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject data = response.getJSONObject("body");
                        Iterator<String> keys = data.keys();
                        List<DataEntry> list = new ArrayList<>();

                        if (!keys.hasNext()) {
                            list.add(new ValueDataEntry("No points", 0));
                        }

                        while (keys.hasNext()) {
                            String key = keys.next();
                            int value = Integer.parseInt(data.get(key).toString());
                            list.add(new ValueDataEntry(key, value));
                        }
                        final Pie pie = AnyChart.pie();
                        pie.data(list);

                        chart.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setVisibility(View.VISIBLE);
                                chart.setChart(pie);
                            }
                        });

                        reload.post(new Runnable() {
                            @Override
                            public void run() {
                                stopAnimation(reload);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Could not parse json response", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Failed to get valid response", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not parse JSON", Toast.LENGTH_SHORT).show();
        }
    }

    public void reload(View view) {
        chart.setVisibility(View.INVISIBLE);
        rotate(reload);
        getData();
    }

    public void stopAnimation(View view){
        view.setVisibility(View.INVISIBLE);
        view.clearAnimation();
    }

    private void rotate(View view){
        view.setVisibility(View.VISIBLE);
        Animation aniRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        view.startAnimation(aniRotate);
    }

    public void openQr(View v) {
        Intent i = new Intent(getApplicationContext(), QRCodeActivity.class);
        i.putExtra("user_id", this.uuid);
        startActivity(i);
    }
}
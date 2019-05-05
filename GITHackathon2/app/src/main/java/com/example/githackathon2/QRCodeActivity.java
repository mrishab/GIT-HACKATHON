package com.example.githackathon2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class QRCodeActivity extends AppCompatActivity {

    private static final String url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";

    private TextView title;
    private ImageView qrCode;
    private Button refreshButton;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initViews();
        this.uuid = getIntent().getExtras().getString("user_id");
        loadQrCode();
    }

    private void initViews(){
        title = findViewById(R.id.title);
        qrCode = findViewById(R.id.qr_code);
        refreshButton = findViewById(R.id.refresh);
        rotate(qrCode);
    }

    public void refresh(View view){
        title.setText("Loading...");
        qrCode.setImageDrawable(getDrawable(R.drawable.loading));
        rotate(qrCode);
        loadQrCode();
    }

    public void loadQrCode(){
        new BitmapRequest(this).request(url + this.uuid, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap response) {
                title.post(new Runnable() {
                    @Override
                    public void run() {
                        title.setText("Ready to Scan");
                    }
                });
                qrCode.post(new Runnable() {
                    @Override
                    public void run() {
                        qrCode.setImageBitmap(response);
                        qrCode.clearAnimation();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRCodeActivity.this, "Failed to load QrCode!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void rotate(View view){
        Animation aniRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        view.startAnimation(aniRotate);
    }
}

package com.example.githackathon2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String url = "https://iugis.serveo.net/user_login";

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void login(View v) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email.getText().toString());
            body.put("password", password.getText().toString());
            new JsonRequest(this).post(url, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String uuid = response.get("user_id").toString();
                        Intent loginSuccess = new Intent(getApplicationContext(), PointsActivity.class);
                        loginSuccess.putExtra("user_id", uuid);
                        startActivity(loginSuccess);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Invalid Response Received", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Unable to login", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e ){
            Toast.makeText(this, "Cannot read values", Toast.LENGTH_LONG).show();;
        }
    }

    public void signup(View v) {
        Intent i = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(i);
    }
}

package com.example.githackathon2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity  extends AppCompatActivity {

    private static final String signupURL   = "https://iugis.serveo.net/create_user";

    private EditText name, age, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }


    private void init(){
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
    }

    public void signup(View v){
        JSONObject object = new JSONObject();
        try {
            object.put("name", name.getText().toString());
            object.put("age", Integer.parseInt(age.getText().toString()));
            object.put("email", email.getText().toString());
            object.put("password", password.getText().toString());

            new JsonRequest(this).post(signupURL, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(SignupActivity.this, "User Account Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignupActivity.this, "Failed to create User Account", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

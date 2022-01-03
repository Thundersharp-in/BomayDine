package com.thundersharp.bombaydine.user.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.thundersharp.bombaydine.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;

public class AcharayaLogin extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acharaya_login);
        webView = findViewById(R.id.web);


        ((AppCompatButton)findViewById(R.id.button)).setOnClickListener(o->{
            try {

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = "https://www.acharyainstitutes.in/index.php?r=student%2Flogin";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("StudentLoginForm[username]", ((EditText)findViewById(R.id.userName)).getText().toString());
                jsonBody.put("StudentLoginForm[password]", ((EditText)findViewById(R.id.password)).getText().toString());
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webView.loadDataWithBaseURL(null, response, "text/html", "utf-8", null);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AcharayaLogin.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){


                    @Override
                    public int getMethod() {
                        return Method.GET;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    public byte[] getBody() {
                        return requestBody.getBytes(StandardCharsets.UTF_8);
                    }

                    @Override
                    public String getPostBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> data = new HashMap<>();
                        data.put("cookie","_csrf=f9448da9476bf149f9d8daf04d925df89a0fc53817e7023ac0e451d9aeacef60a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22-1AKWJuiz8vv8tALwHdu3NTOnsfokAce%22%3B%7D; PHPSESSID=8mdb8e5jtfvla79vhhuvjj59h6; AWSALB=ybe4jxeGr25C9Czj62v3K6Aj+DQcXl6fPAVWBgohVJ0amP4QtkGHQHLrC1QtolCfob3WMWzDM2PHACRbfcgG1ceyB9CepZvYcOoxEU0u2iKCUuMgQgqjkW0f9eUf; AWSALBCORS=ybe4jxeGr25C9Czj62v3K6Aj+DQcXl6fPAVWBgohVJ0amP4QtkGHQHLrC1QtolCfob3WMWzDM2PHACRbfcgG1ceyB9CepZvYcOoxEU0u2iKCUuMgQgqjkW0f9eUf");
                        return data;
                    }
                };


                requestQueue.add(stringRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });



    }
}
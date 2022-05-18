package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;
    String encodedUrl;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);
    }
    public void loadWeather(){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String weatherInfo = response.getString("weather");
//                        String mainInfo = response.getString("main");
                        Log.i("wheather info:", weatherInfo);
                        JSONArray jsonArray = new JSONArray(weatherInfo);
                        JSONObject jsonPart=null;
                        String message="";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonPart = jsonArray.getJSONObject(i);
                            String main=jsonPart.getString("main");
                            String description =jsonPart.getString("description");
                            if(!main.equals("")&&!description.equals("")){
                                message+="Weather: \n"+main+" : "+description+"\r\n";
                            }
                            Log.i("json object:", jsonPart.toString());
                            Log.i("main: ", jsonPart.getString("main"));
                            Log.i("description", jsonPart.getString("description"));
                        }
                        if(!message.equals("")) {
                            resultTextView.setText(message);
                        }else{
                            Toast.makeText(MainActivity.this, "Could not get weather :(", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Could not get weather :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Could not get weather :(", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not get weather :(", Toast.LENGTH_SHORT).show();
        }
    }
    public void getWeather(View view) {
        try {
            encodedUrl = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            url = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedUrl + "&appid=13612e70e7cb96748f4f11d230da37ac";
            loadWeather();
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not get weather :(", Toast.LENGTH_SHORT).show();
        }
    }
}
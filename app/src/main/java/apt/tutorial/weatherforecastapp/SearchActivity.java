package apt.tutorial.weatherforecastapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends  AppCompatActivity {

    RequestQueue requestQueue;
    Button btnSearch;
    EditText inputLocation;
    ArrayList<City> searchResultCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_layout);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnSearch = findViewById(R.id.btnSearch);
        inputLocation = findViewById(R.id.inputLocation);
        searchResultCity = new ArrayList<>();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = inputLocation.getText().toString();
                Log.d("myLog","key: "+key);
                getCityList(key);
            }
        });


    }

    private void getCityList(String key) {
        String[] arrKey = key.split(" ");
        String handledKey = "";
        if(arrKey.length > 1){
            handledKey = handledKey.concat(arrKey[0]);
            for (int i=1 ; i< arrKey.length; i++){
                handledKey = handledKey.concat("%20");
                handledKey = handledKey.concat(arrKey[i]);
            }
        }else{
            handledKey=key;
        }
        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        String url = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=kGTDuDorZs1s2xFRoeQZbwX1DcHMfrDn&q="+handledKey+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray result = new JSONArray(response);
                            for (int i = 0; i <result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                String key = jsonObject.getString("Key");
                                String searchResult = jsonObject.getString("LocalizedName");
                                String country = jsonObject.getJSONObject("Country").getString("LocalizedName");
                                String resultCity = jsonObject.getJSONObject("AdministrativeArea").getString("LocalizedName");
                                City city = new City(key,searchResult,country,resultCity);
                                searchResultCity.add(city);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myLog","error: "+error);
                        Toast.makeText(SearchActivity.this,"Search fail, try again!",Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

}

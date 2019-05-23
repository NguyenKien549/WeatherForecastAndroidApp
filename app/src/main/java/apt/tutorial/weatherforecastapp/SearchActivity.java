package apt.tutorial.weatherforecastapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
    ImageButton btnSearch;
    ImageButton backSearch;
    ListView cityList;
    EditText inputLocation;
    ArrayList<City> searchResultCity;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_layout);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.search_actionbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnSearch = findViewById(R.id.btnSearch);
        backSearch = findViewById(R.id.backSearch);
        inputLocation = findViewById(R.id.inputLocation);
        cityList = findViewById(R.id.cityList);
        searchResultCity = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this,R.layout.city_layout,searchResultCity);

        cityList.setAdapter(searchAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = inputLocation.getText().toString();
                Log.d("myLog","key: "+key);
                getCityList(key);
            }
        });

        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = searchResultCity.get(position);

                Toast.makeText(SearchActivity.this,"city: "+city.getCity()+", "+city.getCountry(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                intent.putExtra("city",city.getCity());
                startActivity(intent);

            }


        });

    }

    private void getCityList(String key) {
        searchAdapter.clear();
        String[] arrKey = key.split(" ");
        String handledKey = "";
        if(key.trim().equals("")){
            Toast.makeText(this,"Input not empty. Try again!!!",Toast.LENGTH_LONG).show();
            return;
        }else if(arrKey.length ==1){
            handledKey=key;
        }else if(arrKey.length > 1){
            handledKey = handledKey.concat(arrKey[0]);
            for (int i=1 ; i< arrKey.length; i++){
                handledKey = handledKey.concat("%20");
                handledKey = handledKey.concat(arrKey[i]);
            }
        }        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        String url = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=iWk88SAOPAo4Iz3IwgDIjttJXwGntpPR&q="+handledKey+"";
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
                                searchAdapter.add(city);
                            }
                            if(searchAdapter.isEmpty()){
                                Toast.makeText(SearchActivity.this,"Data is empty. Try again!!!",Toast.LENGTH_LONG).show();
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

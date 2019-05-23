package apt.tutorial.weatherforecastapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    ImageView imgBack;
    ListView lv;
    Daily10Adapter daily10Adapter;
    ArrayList<Daily10> daily10ArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        anhXa();
        Intent intent=getIntent();
        String city=intent.getStringExtra("city");
        Log.d("city",""+city);
        getTenDays(city);
        imgBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void anhXa(){
        imgBack=(ImageView)findViewById(R.id.image_back);
        lv=(ListView)findViewById(R.id.listview10days);
        daily10ArrayList=new ArrayList<Daily10>();
        daily10Adapter=new Daily10Adapter(Main2Activity.this,daily10ArrayList);
        lv.setAdapter(daily10Adapter);


    }
    private void getTenDays(String city){

       RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city="+city+"&key=d629fbdd82ca403899f2eb5fe13ce92f&days=10";

        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Main2Activity.this, "Getting data...", Toast.LENGTH_LONG).show();
                        Log.d("myLog","getting data"+response);

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArrayDaily=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArrayDaily.length();i++){
                                JSONObject jsonObjectOneday=jsonArrayDaily.getJSONObject(i);

                                // lấy ngày tháng
                                Long ngay=jsonObjectOneday.getLong("ts");
                                Date date=new Date(ngay*1000L);
                                Calendar c = Calendar.getInstance();
                                c.setTime(date);
                                c.add(Calendar.DATE, 1);
                                date = c.getTime();
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE yyyy-MM-dd");
                                String day= simpleDateFormat.format(date);


                                // lấy nhiệt độ max min trong ngày
                               double min_temp=jsonObjectOneday.getDouble("min_temp");
                                double max_temp=jsonObjectOneday.getDouble("max_temp");
                                int minTemp=(int)Math.round(min_temp);
                                int maxTemp=(int)Math.round(max_temp);
                                String temp=maxTemp+" °C/"+minTemp+" °C";

                                // lấy icon
                                JSONObject jsonWeather=jsonObjectOneday.getJSONObject("weather");
                                String icon=jsonWeather.getString("icon");

                                // lấy mô tả
                                String description=jsonWeather.getString("description");// mô tả chung
                                String pop=jsonObjectOneday.getString("pop"); //khả năng mưa
                                String precip=jsonObjectOneday.getString("precip"); //lượng mưa
                                String clouds=jsonObjectOneday.getString("clouds"); //độ che phủ mây
                                double uv=jsonObjectOneday.getDouble("uv"); //chỉ số uv
                                String uv2= String.format("%.2f", uv);

                                String des= description+"\n"+"Khả năng mưa: "+pop+" %, độ che phủ mây: "+clouds+" %"+"\n"+"Chỉ số UV: "+uv2;

                                daily10ArrayList.add(new Daily10(day,temp,icon,des));
                                Log.d("dailytendays",""+daily10ArrayList.get(i).toString());

                            }
                            daily10Adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this, "Getting data error...", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
}

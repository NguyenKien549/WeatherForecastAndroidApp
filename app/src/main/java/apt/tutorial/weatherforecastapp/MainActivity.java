package apt.tutorial.weatherforecastapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    LineChart mpLineChart;

    TextView currentTemp;
    TextView maxTemp;
    TextView minTemp;
    ImageView currentStateIcon;
    TextView currentDate;
    TextView currentCity;
    TextView currentState;
    TextView tempInfor;
    TextView humidInfor;
    TextView UVInfor;
    TextView visibleInfor;
    TextView dew_pointInfor;
    TextView pressureInfor;

    ArrayList<OneHourInfo> listModel = null;

    RequestQueue requestQueue;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //map component in main.xml
        anhXa();

        boolean internetAvailable = checkNetwork();
        if(internetAvailable){
            //get data from api
            getCurrentData("Hanoi");

            //get data 24h from api
            get24hoursData("Hanoi");

            //custom action bar
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);

            mpLineChart=(LineChart)findViewById(R.id.line_chart);
            LineDataSet lineDataSet1=new LineDataSet(dataValues1(),"");
            LineDataSet lineDataSet2=new LineDataSet(dataValues2(),"");
            ArrayList<ILineDataSet> dataSets=new ArrayList<>();
            dataSets.add(lineDataSet1);
            dataSets.add(lineDataSet2);
//        mpLineChart.setBackgroundColor(Color.BLUE);
            mpLineChart.setDrawGridBackground(false);
            mpLineChart.setDrawBorders(false);
            mpLineChart.getAxisLeft().setDrawGridLines(false);
            mpLineChart.getXAxis().setDrawGridLines(false);

            mpLineChart.getAxisRight().setDrawGridLines(false);
            mpLineChart.getXAxis().setDrawGridLines(false);
            mpLineChart.getXAxis().setEnabled(false);
            mpLineChart.getAxisRight().setEnabled(false);
            mpLineChart.getAxisLeft().setEnabled(false);

            mpLineChart.getAxisLeft().setDrawAxisLine(false);
            mpLineChart.getAxisRight().setDrawAxisLine(false);

            mpLineChart.getLegend().setEnabled(false);
            Description des=new Description();
            des.setText("");
            mpLineChart.setDescription(des);

            LineData data=new LineData(dataSets);
            data.setValueFormatter(new MyValueFormat());
            mpLineChart.setData(data);
            mpLineChart.invalidate();
        }
}

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
        else {
            Toast.makeText(this, "Sorry, the Internet is not available",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler24h);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        HourlyAdapter hourlyAdapter = new HourlyAdapter(listModel,MainActivity.this);
        recyclerView.setAdapter(hourlyAdapter);
}

    private void get24hoursData(String city) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/forecast/hourly?q=" +city+
                "&units=metric&appid=a294d4f6615e3794f086c469c0258c7b";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog","get data done 24h");
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i<24; i++){
                                JSONObject inforOneHour = jsonArray.getJSONObject(i);
                                JSONObject main = inforOneHour.getJSONObject("main");
                                double temp = main.getDouble("temp");
                                int tempInt = (int) Math.round(temp);

                                JSONArray weather = inforOneHour.getJSONArray("weather");
                                JSONObject infoWeather = weather.getJSONObject(0);
                                String icon = infoWeather.getString("icon");

                                String date = inforOneHour.getString("dt");
                                Date time = new Date(Long.valueOf(date) * 1000L);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                String hour = simpleDateFormat.format(time);

                                OneHourInfo oneHourInfo = new OneHourInfo(tempInt,icon,hour);

                                listModel.add(oneHourInfo);
                            }

                            //khoi tao recycle view
                            initRecycleView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myLog","get data fail 24h");
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void getCurrentData(String city){
        Log.d("myLog","getting data");

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=a294d4f6615e3794f086c469c0258c7b";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog","getting data: "+response);
                        try {
                            JSONObject jsonObject =new JSONObject(response);

                            String day = jsonObject.getString("dt");
                            Long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
                            String Date = simpleDateFormat.format(date);
                            currentDate.setText(Date);

                            String name = jsonObject.getString("name");
                            currentCity.setText(name);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String status = jsonObjectWeather.getString("main");
                            currentState.setText(status);

                            String icon = jsonObjectWeather.getString("icon");
                            Log.d("myLog", "onResponse: "+icon);
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/w/"
                                    +icon+".png").into(currentStateIcon);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            int temp = jsonObjectMain.getInt("temp");
                            int pressure = jsonObjectMain.getInt("pressure");
                            int temp_min = jsonObjectMain.getInt("temp_min");
                            int temp_max = jsonObjectMain.getInt("temp_max");
                            int humidity = jsonObjectMain.getInt("humidity");
                            int visibility = jsonObject.getInt("visibility");

                            currentTemp.setText(temp+"°C");
                            minTemp.setText(temp_min+"°");
                            maxTemp.setText(temp_max+"°");

                            tempInfor.setText(temp+"°C");
                            pressureInfor.setText(pressure+" mb");
                            humidInfor.setText(humidity+"%");
                            visibleInfor.setText((visibility/1000)+" km");

                            JSONObject jsonObjectCoord = jsonObject.getJSONObject("coord");
                            String lat = jsonObjectCoord.getString("lat");
                            String lon = jsonObjectCoord.getString("lon");
                            setUVInfor(Double.valueOf(lat),Double.valueOf(lon));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Getting data error...", Toast.LENGTH_LONG).show();
                        Log.d("myLog","getting data error");
                    }
                });
        requestQueue.add(stringRequest);

    }

    private void setUVInfor(double lat, double lon){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/uvi?lat="
                +lat+"&lon="+lon+"&appid=a294d4f6615e3794f086c469c0258c7b&cnt=1";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog","uvInfor "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            double uv = jsonObject.getDouble("value");
                            UVInfor.setText(uv+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("myLog","uvInfor fail");
                        }
                });
        requestQueue.add(stringRequest);
    }

    private void anhXa(){
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        minTemp  = (TextView) findViewById(R.id.minTemp);
        currentStateIcon  = (ImageView) findViewById(R.id.currentStateIcon);
        currentDate  = (TextView) findViewById(R.id.currentDate);
        currentCity  = (TextView) findViewById(R.id.currentCity);
        currentState  = (TextView) findViewById(R.id.currentState);
        tempInfor  = (TextView) findViewById(R.id.tempInfor);
        humidInfor  = (TextView) findViewById(R.id.humidInfor);
        UVInfor  = (TextView) findViewById(R.id.UVInfor);
        visibleInfor  = (TextView) findViewById(R.id.visibleInfor);
        dew_pointInfor  = (TextView) findViewById(R.id.dew_pointInfor);
        pressureInfor  = (TextView) findViewById(R.id.pressureInfor);

        listModel = new ArrayList<>();

    }

    private ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();
        dataVals.add(new Entry(0,20));
        dataVals.add(new Entry(1,23));
        dataVals.add(new Entry(2,22));
        dataVals.add(new Entry(3,25));
        dataVals.add(new Entry(4,29));

        return dataVals;
    }

    private ArrayList<Entry> dataValues2(){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();
        dataVals.add(new Entry(0,30));
        dataVals.add(new Entry(1,33));
        dataVals.add(new Entry(2,32));
        dataVals.add(new Entry(3,35));
        dataVals.add(new Entry(4,39));


        return dataVals;
    }

    private  class MyValueFormat implements IValueFormatter{
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value+" °C";
        }
    }

}

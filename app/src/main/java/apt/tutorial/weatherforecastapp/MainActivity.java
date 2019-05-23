package apt.tutorial.weatherforecastapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    LineChart mpLineChart;

    ArrayList<Daily> dailyArrayList=null;
    String locationKey=null;

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
    TextView aqi;
    TextView shortphase;
    TextView longphase;
    ProgressBar determinateBar;
    ImageButton list10days;
    TextView cityBar;
    ImageButton search;
    View view;
    LinearLayout bgApp;

    ArrayList<OneHourInfo> listModel = null;

    RequestQueue requestQueue;
    RecyclerView recyclerView;


    private LocationManager locationManager;
    private LocationListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //acu key1:iWk88SAOPAo4Iz3IwgDIjttJXwGntpPR tai1
        //acu key2:kGTDuDorZs1s2xFRoeQZbwX1DcHMfrDn kiên
        //acu key3:jvPR2DbpPfR3aM3gpxiGK2aBFGB9P84x tai2

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

         view=getSupportActionBar().getCustomView();
         Log.d("custom",""+(view!=null));


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //map component in main.xml
        anhXa();


        boolean internetAvailable = checkNetwork();
        if(internetAvailable) {
            Intent intent = getIntent();
            if(intent.getStringExtra("city")!=null){
                Log.d("MyLogaa","aaaa"+intent.getStringExtra("city"));
                final String city = intent.getStringExtra("city");
                getCurrentData(city);
                getLocationKey(city);
                getIndexAir(city);

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                });
                

                //chuyển sang view list 10 days
                list10days.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("city",city);
                        startActivity(intent);
                    }
                });
            }else{
                Log.d("MyLogb","a");
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double lon = location.getLongitude();
                        Log.d("kinhdo",""+lon);
                        double lat = location.getLatitude();
                        Log.d("kinhdo",""+lat);
                        getInforFromGPS(lat,lon);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);
                    }
                };
                configure_button();

            }

        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 100000, listener);
    }

    private void getInforFromGPS(double lat, double lon) {
        Log.d("myLog", "location infor ");
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=kGTDuDorZs1s2xFRoeQZbwX1DcHMfrDn&q="+lat+"%2C"+lon+"&language=en-us&details=true&toplevel=true";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog", "location infor " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1=jsonObject.getJSONObject("AdministrativeArea");

                            final String city=jsonObject1.getString("LocalizedName");
                            Log.d("namecity",""+city);
                            getCurrentData(city);
                            getLocationKey(city);
                            getIndexAir(city);

                            search.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                                    startActivity(intent);
                                }
                            });
                            //chuyển sang view list 10 days
                            list10days.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                                    intent.putExtra("city",city);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLog", "location infor " + error);
            }
        });
        requestQueue.add(stringRequest);
    }

    public void setChart(ArrayList<Daily> dailyArrayList){
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        mpLineChart=(LineChart)findViewById(R.id.line_chart);
        LineDataSet lineDataSet1=new LineDataSet(dataValues1(dailyArrayList),"");
        lineDataSet1.setValueTextSize(12);
//        lineDataSet1.setValueTextColor(000000);
        LineDataSet lineDataSet2=new LineDataSet(dataValues2(dailyArrayList),"");
        lineDataSet2.setValueTextSize(12);
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

    private void initRecycleViewFiveDays() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler5days);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        Daily5Adapter daily5Adapter = new Daily5Adapter(dailyArrayList,MainActivity.this);
        recyclerView.setAdapter(daily5Adapter);
    }

    private void get24hoursData(String key) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/"+key+"?apikey=jvPR2DbpPfR3aM3gpxiGK2aBFGB9P84x&language=en-us&metric=true";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog","get data done 24h");
                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i<12; i++){
                                JSONObject inforOneHour = jsonArray.getJSONObject(i);
                                JSONObject main = inforOneHour.getJSONObject("Temperature");
                                double temp = main.getDouble("Value");
                                int tempInt = (int) Math.round(temp);


                                String icon = inforOneHour.getString("WeatherIcon");

                                String date = inforOneHour.getString("DateTime");
                                String hour=date.substring(11,16);


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
                        Log.d("myLog","get data fail 24h"+error);
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
                            cityBar.setText(name);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String status = jsonObjectWeather.getString("main");

                            currentState.setText(status);

                            switch (status){
                                case "Clouds": bgApp.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.rain)); break;
//                                case "Clear": bgApp.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.clear)); break;
//                                case "Rain": case"Drizzle":bgApp.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.rain)); break;
//                                case "Thunderstorm": bgApp.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.storm)); break;
//                                default:bgApp.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.mist)); break;

                            }

                            String icon = jsonObjectWeather.getString("icon");
                            Log.d("myLog", "onResponse: "+icon);
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/w/"
                                    +icon+".png").into(currentStateIcon);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");

                            //+3???
                            int temp = jsonObjectMain.getInt("temp");
//                            int min = jsonObjectMain.getInt("temp_min");
//                            int max = jsonObjectMain.getInt("temp_max");
                            int pressure = jsonObjectMain.getInt("pressure");

                            int humidity = jsonObjectMain.getInt("humidity");
                            int visibility = jsonObject.getInt("visibility");

                            currentTemp.setText(temp+"°C");
//                            minTemp.setText(min+"°");
//                            maxTemp.setText(max+"°");

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


    private void setUVInfor(double lat, double lon) {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/uvi?lat="
                + lat + "&lon=" + lon + "&appid=a294d4f6615e3794f086c469c0258c7b&cnt=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myLog", "uvInfor " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            double uv = jsonObject.getDouble("value");
                            UVInfor.setText(uv + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLog", "uvInfor fail");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void getLocationKey(String city){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=iWk88SAOPAo4Iz3IwgDIjttJXwGntpPR&q="+city+"&language=en-us&details=true";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                             JSONObject jsonObject = jsonArray.getJSONObject(0);
                             locationKey=jsonObject.getString("Key");
                            Log.d("myLog","key"+locationKey);
                             getFiveDays(locationKey);
                             get24hoursData(locationKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
    private void getFiveDays(String locationKey){

            requestQueue = Volley.newRequestQueue(MainActivity.this);
            String url = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+locationKey+"?apikey=kGTDuDorZs1s2xFRoeQZbwX1DcHMfrDn&language=vi-vn&details=true&metric=true";

            StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MainActivity.this, "Getting data...", Toast.LENGTH_LONG).show();
                            Log.d("myLog","getting data"+response);

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray jsonArrayDaily=jsonObject.getJSONArray("DailyForecasts");
                                for(int i=0;i<jsonArrayDaily.length();i++){
                                    JSONObject jsonObjectOneday=jsonArrayDaily.getJSONObject(i);

                                    // lấy ngày tháng
                                    Long ngay=jsonObjectOneday.getLong("EpochDate");
                                    Date date=new Date(ngay*1000L);
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(date);
                                    c.add(Calendar.DATE, 1);
                                    date = c.getTime();
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("E");
                                    String day= simpleDateFormat.format(date);


                                    // lấy nhiệt độ max min trong ngày
                                    JSONObject jsonObjectTemp=jsonObjectOneday.getJSONObject("Temperature");
                                    JSONObject jsonObjectMinTemp=jsonObjectTemp.getJSONObject("Minimum");
                                    double minTempa=jsonObjectMinTemp.getDouble("Value");
                                    int minTemp=(int)Math.round(minTempa);
                                    JSONObject jsonObjectMaxTemp=jsonObjectTemp.getJSONObject("Maximum");
                                    double maxTempa=jsonObjectMaxTemp.getDouble("Value");
                                    int maxTemp=(int)Math.round(maxTempa);

                                    // lấy icon và mô tả
                                    JSONObject jsonObjectDay=jsonObjectOneday.getJSONObject("Day");
                                    int icon=jsonObjectDay.getInt("Icon");
                                    Log.d("icon",""+icon);
                                    String des=jsonObjectDay.getString("PrecipitationProbability");
                                    des+=" %";

                                    Daily daily=new Daily(day,minTemp,maxTemp,icon,des);
                                    Log.d("myLog","daily "+daily.toString());

                                    dailyArrayList.add(daily);

                                }

                                int temp_min = dailyArrayList.get(0).minTemp;
                                int temp_max = dailyArrayList.get(0).maxTemp;
                                minTemp.setText(temp_min+"°");
                                 maxTemp.setText(temp_max+"°");

                                initRecycleViewFiveDays();
                                setChart(dailyArrayList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Getting data error...", Toast.LENGTH_LONG).show();
                        }
                    });
            requestQueue.add(stringRequest);
    }

    private void getIndexAir(String city){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.weatherbit.io/v2.0/current/airquality?city="+city+"&key=d629fbdd82ca403899f2eb5fe13ce92f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObjectAir = new JSONObject(response);
                            JSONArray jsonObjectIndex = jsonObjectAir.getJSONArray("data");
                           JSONObject jsonObject=jsonObjectIndex.getJSONObject(0);

                            String aqiindex1=jsonObject.getString("aqi");
                            Log.d("aqi","aqi"+aqiindex1);
                            aqi.setText(aqiindex1);
                            if(!aqiindex1.equals("-")) {
                                int aqiindex = Integer.parseInt(aqiindex1);
                                determinateBar.setProgress(aqiindex/5);
                                if (aqiindex <= 50) {
                                    shortphase.setText("Tốt");
                                    longphase.setText("Chất lượng không khí đạt tiêu chuẩn");
                                } else if (aqiindex <= 100) {
                                    shortphase.setText("Vừa phải");
                                    longphase.setText("Chất lượng không khí ở mức chấp nhận được");
                                } else if (aqiindex <= 150) {
                                    shortphase.setText("Không tốt cho người nhạy cảm");
                                    longphase.setText("Nhóm người nhạy cảm có thể chịu ảnh hưởng sức khỏe");
                                } else if (aqiindex <= 200) {
                                    shortphase.setText("Có hại cho sức khỏe");
                                    longphase.setText("Mọi người đều chịu tác động đến sức khỏe, nghiêm trọng hơn là nhóm người nhạy cảm");
                                } else if (aqiindex <= 300) {
                                    shortphase.setText("Rất có hại cho sức khỏe");
                                    longphase.setText("Cảnh báo nguy hại sức khỏe nghiêm trọng, tất cả mọi người đều chịu ảnh hưởng");
                                } else {
                                    shortphase.setText("Cực kỳ nguy hiểm");
                                    longphase.setText("Cảnh báo nguy hại nghiêm trọng đến sức khỏe tất cả mọi người");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void anhXa(){
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        minTemp  = findViewById(R.id.minTemp);
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
        aqi=(TextView)findViewById(R.id.aqi);
        shortphase=(TextView)findViewById(R.id.shortphase);
        longphase=(TextView)findViewById(R.id.longphase);
        determinateBar=(ProgressBar)findViewById(R.id.determinateBar);
        list10days=(ImageButton)findViewById(R.id.list10days);
        bgApp=findViewById(R.id.bgApp);


//        LayoutInflater inflater=getLayoutInflater();
//        View custombar=inflater.inflate(R.layout.custom_action_bar,null);
        cityBar=view.findViewById(R.id.cityBar);
        search=view.findViewById(R.id.search);

        listModel = new ArrayList<>();
        dailyArrayList=new ArrayList<>();

    }

    private ArrayList<Entry> dataValues1(ArrayList<Daily> dailyArrayList){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();
        for(int i=0;i<dailyArrayList.size();i++){
            dataVals.add(new Entry(i,dailyArrayList.get(i).getMinTemp()));
        }

        return dataVals;
    }

    private ArrayList<Entry> dataValues2(ArrayList<Daily> dailyArrayList){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();
        for(int i=0;i<dailyArrayList.size();i++){
            dataVals.add(new Entry(i,dailyArrayList.get(i).getMaxTemp()));
        }

        return dataVals;
    }

    private  class MyValueFormat implements IValueFormatter{
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int)value+" °C";
        }
    }

}

package apt.tutorial.weatherforecastapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    LineChart mpLineChart;

    TextView currentTemp;
    TextView maxTemp;
    TextView minTemp;
    ImageView iconCurrentState;
    TextView currentDate;
    TextView currentCity;
    TextView currentState;
    TextView tempInfor;
    TextView humidInfor;
    TextView UVInfor;
    TextView visibleInfor;
    TextView dew_pointInfor;
    TextView pressureInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //map component in main.xml
        anhXa();

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

        //get data from api
        getCurrentData("Hanoi");
}

    private void getCurrentData(String city){
        Log.d("myLog","getting data");
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://openweathermap.org/data/2.5/find?q="+city+"&units=metric&appid=b6907d289e10d714a6e88b30761fae22";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Getting data...", Toast.LENGTH_LONG).show();
                        Log.d("myLog","getting data"+response);
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

    private void anhXa(){
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        minTemp  = (TextView) findViewById(R.id.minTemp);
        iconCurrentState  = (ImageView) findViewById(R.id.iconCurrentState);
        currentDate  = (TextView) findViewById(R.id.currentDate);
        currentCity  = (TextView) findViewById(R.id.currentCity);
        currentState  = (TextView) findViewById(R.id.currentState);
        tempInfor  = (TextView) findViewById(R.id.tempInfor);
        humidInfor  = (TextView) findViewById(R.id.humidInfor);
        UVInfor  = (TextView) findViewById(R.id.UVInfor);
        visibleInfor  = (TextView) findViewById(R.id.visibleInfor);
        dew_pointInfor  = (TextView) findViewById(R.id.dew_pointInfor);
        pressureInfor  = (TextView) findViewById(R.id.pressureInfor);

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

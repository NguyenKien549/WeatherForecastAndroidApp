package apt.tutorial.weatherforecastapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OneHourAdapter extends ArrayAdapter {

    Activity context;
    int layoutId;
    ArrayList<OneHourInfo> listHour = null;

    public OneHourAdapter(Activity context, int layoutId, ArrayList<OneHourInfo> list) {
        super(context, layoutId, list);
        this.context=context;
        this.layoutId=layoutId;
        this.listHour=list;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        OneHourHoder oneHourHoder;
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(layoutId, null);
            oneHourHoder = new OneHourHoder();
            oneHourHoder.setHourlyTemp((TextView) convertView.findViewById(R.id.hourlyTemp));
            oneHourHoder.setStateIcon((ImageView) convertView.findViewById(R.id.stateIcon));
            oneHourHoder.setHourlyTime((TextView) convertView.findViewById(R.id.hourlyTime));

            convertView.setTag(oneHourHoder);
        }else{
            oneHourHoder = (OneHourHoder) convertView.getTag();
        }
            OneHourInfo oneHour = listHour.get(position);
            oneHourHoder.getHourlyTemp().setText(oneHour.getTemp());
            oneHourHoder.getHourlyTime().setText(oneHour.getTime());
            Picasso.with(context).load("https://openweathermap.org/img/w/"+oneHour.getStateIcon()+".png").into(oneHourHoder.getStateIcon());

        return convertView;
    }
}

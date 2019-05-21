package apt.tutorial.weatherforecastapp;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchAdapter extends ArrayAdapter<City> {
    ArrayList<City> cities;
    Context context;
    int layoutId;

    public SearchAdapter(Context context, int resource,ArrayList<City> list) {
        super(context, resource,list);
        this.context = context;
        this.cities = list;
        this.layoutId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CityHolder cityHolder;
        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
            cityHolder = new CityHolder();
            cityHolder.resultCity = convertView.findViewById(R.id.resultCity);
            cityHolder.address = convertView.findViewById(R.id.address);
            convertView.setTag(cityHolder);
        }else{
            cityHolder = (CityHolder) convertView.getTag();
        }

        City city = cities.get(position);
        cityHolder.resultCity.setText(city.getSearchResult());
        cityHolder.address.setText(city.getCity() + ", "+city.getCountry());

        return convertView;
    }

    class CityHolder{
        TextView resultCity;
        TextView address;
    }
}

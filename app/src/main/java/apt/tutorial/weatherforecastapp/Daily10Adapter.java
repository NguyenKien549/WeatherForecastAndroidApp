package apt.tutorial.weatherforecastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Daily10Adapter extends BaseAdapter {
    Context context;
    ArrayList<Daily10> arrayList;

    public Daily10Adapter(Context context, ArrayList<Daily10> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.oneday_forecast_row,null);

        Daily10 daily=arrayList.get(position);

        TextView txtDay=convertView.findViewById(R.id.firstday_info);
        TextView txtTemp=convertView.findViewById(R.id.min_maxTemp_first);
        TextView txtDes=convertView.findViewById(R.id.description_first);
        ImageView imgIcon=convertView.findViewById(R.id.first_icon);

        txtDay.setText(daily.day);
        txtTemp.setText(daily.temp);
        txtDes.setText(daily.des);
        String icon=daily.icon;
        switch (icon){
            case "t03d": case "r05d":imgIcon.setImageResource(R.drawable.t03);break;
            case "t02d":imgIcon.setImageResource(R.drawable.t02);break;
            case "t01d":imgIcon.setImageResource(R.drawable.t01); break;
            case "t04d": case "t05d":imgIcon.setImageResource(R.drawable.ic_thunder);break;
            case"d01d": case "d02d": case "d03d":imgIcon.setImageResource(R.drawable.ic_rainy_7);break;
            case "r01d": case"r02d": case "f01d": case "r04d": case "r06d": imgIcon.setImageResource(R.drawable.ic_rainy_5); break;
            case "r03d":imgIcon.setImageResource(R.drawable.ic_rainy_6);break;
            case "s01d":imgIcon.setImageResource(R.drawable.ic_snowy_2);break;
            case "s02d":imgIcon.setImageResource(R.drawable.ic_snowy_5);break;
            case "s03d":imgIcon.setImageResource(R.drawable.ic_snowy_6);break;
            case "s04d":imgIcon.setImageResource(R.drawable.ic_snowy_3);break;
            case"s05d":imgIcon.setImageResource(R.drawable.s05d);break;
            case "s06d":imgIcon.setImageResource(R.drawable.ic_rainy_6);break;
            case "a01d": case "a02d": case "a03d": case "a04d": case "a05d": case "a06d":imgIcon.setImageResource(R.drawable.a01d);break;
            case "c01d":imgIcon.setImageResource(R.drawable.ic_day);break;
            case "c02d":imgIcon.setImageResource(R.drawable.ic_cloudy_day_1);break;
            case "c03d":imgIcon.setImageResource(R.drawable.ic_cloudy_day_3);break;
            case "c04d":imgIcon.setImageResource(R.drawable.ic_cloudy);break;
            case "u00d":imgIcon.setImageResource(R.drawable.ic_rainy_7);break;

        }
        return convertView;
    }
}

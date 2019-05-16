package apt.tutorial.weatherforecastapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    ArrayList<OneHourInfo> list;
    Context context;

    public HourlyAdapter(ArrayList<OneHourInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.one_hour_temp,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.hourlyTime.setText(list.get(position).getTime());
        holder.hourlyTemp.setText(list.get(position).getTemp());
        Picasso.with(context).load("https://openweathermap.org/img/w/"+list.get(position).getStateIcon()+".png").into(holder.stateIcon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView hourlyTemp;
        ImageView stateIcon;
        TextView hourlyTime;


        public ViewHolder(View itemView) {
            super(itemView);
            hourlyTemp = (TextView) itemView.findViewById(R.id.hourlyTemp);
            stateIcon = (ImageView) itemView.findViewById(R.id.stateIcon);
            hourlyTime = (TextView) itemView.findViewById(R.id.hourlyTime);
        }
    }
}

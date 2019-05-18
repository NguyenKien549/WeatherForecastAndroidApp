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

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

        ArrayList<Daily> list;
        Context context;

public DailyAdapter(ArrayList<Daily> list, Context context) {
        this.list = list;
        this.context = context;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.one_day_temp,parent,false);
        return new ViewHolder(itemView);
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        holder.des.setText(list.get(position).getDes());
        holder.day.setText(list.get(position).getDay());
        int iconKey=list.get(position).getIcon();
        if(iconKey==1){
            holder.icon.setImageResource(R.drawable.);
        }
}

@Override
public int getItemCount() {
        return list.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{

    TextView des;
    ImageView icon;
    TextView day;


    public ViewHolder(View itemView) {
        super(itemView);
        des = (TextView) itemView.findViewById(R.id.dailyDes);
        icon = (ImageView) itemView.findViewById(R.id.dailyIcon);
        day = (TextView) itemView.findViewById(R.id.dailyDay);
    }
}
}

package apt.tutorial.weatherforecastapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Daily5Adapter extends RecyclerView.Adapter<Daily5Adapter.ViewHolder> {

        ArrayList<Daily> list;
        Context context;

public Daily5Adapter(ArrayList<Daily> list, Context context) {
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
        switch (iconKey){
           case  1: holder.icon.setImageResource(R.drawable.icon1); break;
           case 2: holder.icon.setImageResource(R.drawable.icon2); break;
            case 3: holder.icon.setImageResource(R.drawable.icon3); break;
            case 4: holder.icon.setImageResource(R.drawable.icon4); break;
            case 5: holder.icon.setImageResource(R.drawable.icon5); break;
            case 6: holder.icon.setImageResource(R.drawable.icon6); break;
            case 7: holder.icon.setImageResource(R.drawable.icon7); break;
            case 8: holder.icon.setImageResource(R.drawable.icon8); break;
            case 11: holder.icon.setImageResource(R.drawable.icon11); break;
            case 12: holder.icon.setImageResource(R.drawable.icon12); break;
            case 13: holder.icon.setImageResource(R.drawable.icon13); break;
            case 14: holder.icon.setImageResource(R.drawable.icon14); break;
            case 15: holder.icon.setImageResource(R.drawable.icon15); break;
            case 16: holder.icon.setImageResource(R.drawable.icon16); break;
            case 17: holder.icon.setImageResource(R.drawable.icon17); break;
            case 18: holder.icon.setImageResource(R.drawable.icon18); break;
            case 19: holder.icon.setImageResource(R.drawable.icon19); break;
            case 20: holder.icon.setImageResource(R.drawable.icon20); break;
            case 21: holder.icon.setImageResource(R.drawable.icon21); break;
            case 22: holder.icon.setImageResource(R.drawable.icon22); break;
            case 23: holder.icon.setImageResource(R.drawable.icon23); break;
            case 24: holder.icon.setImageResource(R.drawable.icon24); break;
            case 25: holder.icon.setImageResource(R.drawable.icon25); break;
            case 26: holder.icon.setImageResource(R.drawable.icon26); break;
            case 29: holder.icon.setImageResource(R.drawable.icon29); break;
            case 30: holder.icon.setImageResource(R.drawable.icon30); break;
            case 31: holder.icon.setImageResource(R.drawable.icon31); break;
            case 32: holder.icon.setImageResource(R.drawable.icon32); break;

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

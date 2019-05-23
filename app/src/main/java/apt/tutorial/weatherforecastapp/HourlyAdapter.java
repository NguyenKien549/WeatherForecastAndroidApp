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
        holder.hourlyTemp.setText(list.get(position).getTemp()+"°");
        String icon=list.get(position).getStateIcon();
        int iconInt=Integer.parseInt(icon);
        switch (iconInt){
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
            case 33: holder.icon.setImageResource(R.drawable.icon33); break;
            case 34: holder.icon.setImageResource(R.drawable.icon34); break;
            case 35: holder.icon.setImageResource(R.drawable.icon35); break;
            case 36: holder.icon.setImageResource(R.drawable.icon36); break;
            case 37: holder.icon.setImageResource(R.drawable.icon37); break;
            case 38: holder.icon.setImageResource(R.drawable.icon38); break;
            case 39: holder.icon.setImageResource(R.drawable.icon39); break;
            case 40: holder.icon.setImageResource(R.drawable.icon40); break;
            case 41: holder.icon.setImageResource(R.drawable.icon41); break;
            case 42: holder.icon.setImageResource(R.drawable.icon42); break;
            case 43: holder.icon.setImageResource(R.drawable.icon43); break;
            case 44: holder.icon.setImageResource(R.drawable.icon44); break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView hourlyTemp;
        ImageView icon;
        TextView hourlyTime;


        public ViewHolder(View itemView) {
            super(itemView);
            hourlyTemp = (TextView) itemView.findViewById(R.id.hourlyTemp);
            icon = (ImageView) itemView.findViewById(R.id.stateIcon);
            hourlyTime = (TextView) itemView.findViewById(R.id.hourlyTime);
        }
    }
}

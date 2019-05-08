package apt.tutorial.weatherforecastapp;

import android.widget.ImageView;
import android.widget.TextView;

public class OneHourHoder {
    TextView hourlyTemp;
    ImageView stateIcon;
    TextView hourlyTime;

    public TextView getHourlyTemp() {
        return hourlyTemp;
    }

    public void setHourlyTemp(TextView hourlyTemp) {
        this.hourlyTemp = hourlyTemp;
    }

    public ImageView getStateIcon() {
        return stateIcon;
    }

    public void setStateIcon(ImageView stateIcon) {
        this.stateIcon = stateIcon;
    }

    public TextView getHourlyTime() {
        return hourlyTime;
    }

    public void setHourlyTime(TextView hourlyTime) {
        this.hourlyTime = hourlyTime;
    }
}

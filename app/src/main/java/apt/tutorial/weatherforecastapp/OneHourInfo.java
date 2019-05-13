package apt.tutorial.weatherforecastapp;

public class OneHourInfo {
    private int temp;
    private String stateIcon;
    private String time;

    public OneHourInfo(int temp, String stateIcon, String time) {
        this.temp = temp;
        this.stateIcon = stateIcon;
        this.time = time;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getStateIcon() {
        return stateIcon;
    }

    public void setStateIcon(String stateIcon) {
        this.stateIcon = stateIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package apt.tutorial.weatherforecastapp;

public class Daily {
    String day=null;
    int minTemp=0;
    int maxTemp=0;
    int icon=0;
    String des=null;

    //Hàm khởi tạo đối tượng
    public Daily(String day, int minTemp, int maxTemp, int icon, String des) {
        this.day = day;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.icon = icon;
        this.des = des;
    }

    //Các hàm getter và setter
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

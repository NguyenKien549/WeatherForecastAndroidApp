package apt.tutorial.weatherforecastapp;

public class Daily10 {
    String day=null;
    String temp=null;
    String icon=null;

    @Override
    public String toString() {
        return "Daily10{" +
                "day='" + day + '\'' +
                ", temp='" + temp + '\'' +
                ", icon='" + icon + '\'' +
                ", des='" + des + '\'' +
                '}';
    }

    String des=null;

    public Daily10(String day, String temp, String icon, String des) {
        this.day = day;
        this.temp = temp;
        this.icon = icon;
        this.des = des;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

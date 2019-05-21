package apt.tutorial.weatherforecastapp;

public class City {
    private String key;
    private String searchResult;
    private String country;
    private String city;

    public City(String key, String searchResult, String country, String city) {
        this.key = key;
        this.searchResult = searchResult;
        this.country = country;
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

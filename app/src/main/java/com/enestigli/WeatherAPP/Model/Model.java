package com.enestigli.WeatherAPP.Model;

public class Model {

    private String Temperature;
    private String Humadity;
    private String Feels_like;
    private String Min_temp;
    private String Max_temp;
    private String Pressure;
    private String Description;//it shows how the weather is like broken clouds
    private String Main;//it shows how the weather is like Clouds
    private String wind_speed;
    private String visibility;
    private String city;
    private String id;
    private String Country;

    private String icon;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getHumadity() {
        return Humadity;
    }

    public void setHumadity(String humadity) {
        Humadity = humadity;
    }

    public String getFeels_like() {
        return Feels_like;
    }

    public void setFeels_like(String feels_like) {
        Feels_like = feels_like;
    }

    public String getMin_temp() {
        return Min_temp;
    }

    public void setMin_temp(String min_temp) {
        Min_temp = min_temp;
    }

    public String getMax_temp() {
        return Max_temp;
    }

    public void setMax_temp(String max_temp) {
        Max_temp = max_temp;
    }

    public String getPressure() {
        return Pressure;
    }

    public void setPressure(String pressure) {
        Pressure = pressure;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMain() {
        return Main;
    }

    public void setMain(String main) {
        Main = main;
    }
}

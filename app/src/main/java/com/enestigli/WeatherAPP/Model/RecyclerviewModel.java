package com.enestigli.WeatherAPP.Model;

public class RecyclerviewModel {

    private String Temperature;
    private String Humadity;
    private String Feels_like;
    private String Pressure;
    private String Description;//it shows how the weather is like broken clouds
    private String wind_speed;
    private int id;
    private Long date;
    private String TimeZone;


    public RecyclerviewModel(String temperature, String humadity, String feels_like, String pressure, String description, String wind_speed, int id , Long date, String TimeZone) {
        Temperature = temperature;
        Humadity = humadity;
        Feels_like = feels_like;
        Pressure = pressure;
        Description = description;
        this.wind_speed = wind_speed;
        this.id = id;
        this.date = date;
        this.TimeZone = TimeZone;
    }



    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
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

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

package com.yujongu.weatherapp;

public class Firstpage_Data {

    private String imageview_weather;
    private String textview_city;
    private double textview_temperature;
    private String textview_country;
    private String textview_main;
    private int textview_humidity;
    private double textview_windspeed;
    private double textview_max;
    private double textview_min;

    public double getMax() {
        return textview_max;
    }

    public void setMax(double textview_max) {
        this.textview_max = textview_max;
    }

    public double getMin() {
        return textview_min;
    }

    public void setMin(double textview_min) {
        this.textview_min = textview_min;
    }

    public int getHumidity() {
        return textview_humidity;
    }

    public void setHumidity(int textview_humidity) {
        this.textview_humidity = textview_humidity;
    }

    public double getWindspeed() {
        return textview_windspeed;
    }

    public void setWindspeed(double textview_windspeed) {
        this.textview_windspeed = textview_windspeed;
    }

    public String getMain() {
        return textview_main;
    }

    public void setMain(String textview_main) {
        this.textview_main = textview_main;
    }

    public String getCountry() {
        return textview_country;
    }

    public void setCountry(String country) {
        this.textview_country = country;
    }


    public String getImageview_weather() {
        return imageview_weather;
    }

    public void setImageview_weather(String imageview_weather) {
        this.imageview_weather = imageview_weather;
    }

    public double getTemperature() {
        return textview_temperature;
    }

    public void setTemperature(double textview_temperature) {
        this.textview_temperature = textview_temperature;
    }

    public String getCity() {
        return textview_city;
    }

    public void setCity(String textview_city) {
        this.textview_city = textview_city;
    }

    public Firstpage_Data(String weatherIcon, String textview_city, double textview_temperature, String country, String textview_main,
                          int textview_humidity, double textview_windspeed, double textview_max, double textview_min){
        this.imageview_weather = weatherIcon;
        this.textview_city = textview_city;
        this.textview_temperature = textview_temperature;
        this.textview_country = country;
        this.textview_main = textview_main;
        this.textview_humidity = textview_humidity;
        this.textview_windspeed = textview_windspeed;
        this.textview_max = textview_max;
        this.textview_min = textview_min;
    }


}

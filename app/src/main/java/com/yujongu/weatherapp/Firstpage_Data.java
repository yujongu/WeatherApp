package com.yujongu.weatherapp;

public class Firstpage_Data {

    private String imageview_weather;
    private String textview_city;
    private double textview_temperatureC;
    private double textview_temperatureF;
    private String textview_country;
    private String textview_main;
    private int textview_humidity;
    private double textview_windspeed;
    private double textview_max;
    private double textview_min;
    private String inputCityName;
    private double latitude;
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getInputCityName() {
        return inputCityName;
    }

    public void setInputCityName(String inputCityName) {
        this.inputCityName = inputCityName;
    }

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

    public double getTemperatureC() {
        return textview_temperatureC;
    }

    public void setTemperatureC(double textview_temperatureC) {
        this.textview_temperatureC = textview_temperatureC;
    }

    public double getTextview_temperatureF() {
        return textview_temperatureF;
    }

    public void setTextview_temperatureF(double textview_temperatureF) {
        this.textview_temperatureF = textview_temperatureF;
    }

    public String getCity() {
        return textview_city;
    }

    public void setCity(String textview_city) {
        this.textview_city = textview_city;
    }

    public Firstpage_Data(String weatherIcon, String textview_city, double textview_temperatureC, double textview_temperatureF, String country, String textview_main,
                          int textview_humidity, double textview_windspeed, double textview_max, double textview_min, double latitude, double longitude){
        this.imageview_weather = weatherIcon;
        this.textview_city = textview_city;
        this.textview_temperatureC = textview_temperatureC;
        this.textview_temperatureF = textview_temperatureF;
        this.textview_country = country;
        this.textview_main = textview_main;
        this.textview_humidity = textview_humidity;
        this.textview_windspeed = textview_windspeed;
        this.textview_max = textview_max;
        this.textview_min = textview_min;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}

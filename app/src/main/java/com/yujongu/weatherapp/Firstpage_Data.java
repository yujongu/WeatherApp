package com.yujongu.weatherapp;

public class Firstpage_Data {

    private String textview_city;
    private double textview_temperature;

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

    public Firstpage_Data(String textview_city, double textview_temperature){
        this.textview_city = textview_city;
        this.textview_temperature = textview_temperature;
    }


}

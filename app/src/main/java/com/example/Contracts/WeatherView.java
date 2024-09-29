package com.example.Contracts;

public interface WeatherView {
    void showWeather(String temperature);

    void showError(String message);
}

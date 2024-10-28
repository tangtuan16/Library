package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.WeatherContract;
import com.example.Models.Weather;
import com.example.Models.WeatherModel;

public class WeatherPresenter implements WeatherContract.Presenter {

    private WeatherContract.View view;
    private WeatherContract.Model model;

    public WeatherPresenter(WeatherContract.View view, Context context) {
        this.view = view;
        this.model = new WeatherModel(context);
    }

    @Override
    public void loadWeather(String apiKey, String location) {
        model.getWeather(apiKey, location, new WeatherContract.Model.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                view.showWeather(weather);
            }

            @Override
            public void onFailure(Throwable t) {
                view.showError("Lỗi khi tải dữ liệu thời tiết !");
            }
        });
    }
}

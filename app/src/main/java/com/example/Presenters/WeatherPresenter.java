package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.WeatherService;
import com.example.Contracts.WeatherView;
import com.example.Models.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherPresenter {
    private WeatherView view;
    private WeatherService service;

    public WeatherPresenter(Context context, WeatherView view) {
        this.view = view;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherService.class);
    }

    public void getWeather(String city, String apiKey) {
        Call<WeatherResponse> call = service.getCurrentWeather(apiKey, city);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    view.showWeather(weather.getCurrent().getTempC() + "°C, " + weather.getCurrent().getCondition().getText());
                } else {
                    view.showError("Failed to load 1");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                view.showError("Failed to load 2");
            }
        });
    }
}

package com.example.Models;

import android.content.Context;
import android.util.Log;

import com.example.Contracts.WeatherApiService;
import com.example.Contracts.WeatherContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherModel implements WeatherContract.Model {

    private WeatherApiService apiService;

    public WeatherModel(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(WeatherApiService.class);
    }

    @Override
    public void getWeather(String apiKey, String location, final WeatherContract.Model.WeatherCallback callback) {
        List<String> desiredHours = Arrays.asList("06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        Call<Weather> call = apiService.getForecast(apiKey, location, 1);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (!response.isSuccessful()) {
                    callback.onFailure(new Throwable("Phản hồi không thành công!"));
                    return;
                }

                Weather weather = response.body();
                if (weather == null) {
                    callback.onFailure(new Throwable("Không có dữ liệu thời tiết!"));
                    return;
                }

                List<Weather.Forecast.Forecastday> forecastDays = weather.getForecast().getForecastday();
                if (forecastDays.isEmpty()) {
                    callback.onFailure(new Throwable("Không có dữ liệu dự báo thời tiết!"));
                    return;
                }

                List<Weather.Forecast.Forecastday.Hour> allHours = forecastDays.get(0).getHour();
                List<Weather.Forecast.Forecastday.Hour> filteredHours = new ArrayList<>();

                for (Weather.Forecast.Forecastday.Hour hour : allHours) {
                    String hourTime = hour.getTime().split(" ")[1].split(":")[0];
                    if (desiredHours.contains(hourTime)) {
                        filteredHours.add(hour);
                    }
                }

                forecastDays.get(0).setHour(filteredHours);
                callback.onSuccess(weather);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}

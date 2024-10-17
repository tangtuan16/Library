package com.example.Models;

import android.content.Context;

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
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    if (weather != null) {
                        List<Weather.Forecast.Forecastday.Hour> filteredHours = new ArrayList<>();
                        List<Weather.Forecast.Forecastday.Hour> allHours = weather.getForecast().getForecastday().get(0).getHour();

                        for (Weather.Forecast.Forecastday.Hour hour : allHours) {
                            String hourTime = hour.getTime().split(" ")[1].split(":")[0];
                            if (desiredHours.contains(hourTime)) {
                                filteredHours.add(hour);
                            }
                        }
                        Weather.Forecast.Forecastday forecastday = weather.getForecast().getForecastday().get(0);
                        forecastday.setHour(filteredHours);
                        callback.onSuccess(weather);
                    } else {
                        callback.onFailure(new Throwable("Không có dữ liệu thời tiết  !"));
                    }
                } else {
                    callback.onFailure(new Throwable("Phản hồi không thành công !"));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}

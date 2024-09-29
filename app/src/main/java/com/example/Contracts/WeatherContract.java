package com.example.Contracts;

import com.example.Models.Weather;

public interface WeatherContract {
    interface Presenter {
        void loadWeather(String apiKey, String location);
    }

    interface Model {
        void getWeather(String apiKey, String location, WeatherCallback callback);

        interface WeatherCallback {
            void onSuccess(Weather weather);

            void onFailure(Throwable t);
        }
    }

    interface View {
        void showWeather(Weather weather);

        void showError(String message);
    }

}

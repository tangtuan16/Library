package com.example.Contracts;

import com.example.Models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("key") String apiKey, @Query("q") String location);

    ;
}

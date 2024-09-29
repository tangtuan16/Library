package com.example.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Contracts.WeatherView;
import com.example.Presenters.WeatherPresenter;
import com.example.btl_libary.R;

public class HomeFragment extends Fragment implements WeatherView {
    private TextView weatherTextView;
    private WeatherPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        weatherTextView = view.findViewById(R.id.weatherTextView);
        presenter = new WeatherPresenter(getContext(), this);
        presenter.getWeather("HaNoi", getString(R.string.weather_api_key));
        return view;
    }

    @Override
    public void showWeather(String temperature) {
        weatherTextView.setText(temperature);
    }

    @Override
    public void showError(String message) {
        weatherTextView.setText(message);
    }
}

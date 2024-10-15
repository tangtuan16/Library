package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.AuthorData;
import com.example.Models.BookModel;
import com.example.Models.GenreData;

import java.util.List;

public class ChartPresenter {
    private BookContract.View.HomeView homeView;
    private Context context;
    private BookModel bookModel;

    public ChartPresenter(Context context, BookContract.View.HomeView homeView) {
        this.context = context;
        this.homeView = homeView;
        bookModel = new BookModel(context);
    }

    public void loadBarChart() {
        List<AuthorData> authorData = bookModel.getBarChartData();
        homeView.showBarChart(authorData);
    }

    public void loadPieChart() {
        List<GenreData> genreData = bookModel.getPieChartData();
        homeView.showPieChart(genreData);
    }
}

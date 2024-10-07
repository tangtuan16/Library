package com.example.Views.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Contracts.UserContract;
import com.example.Contracts.WeatherContract;
import com.example.Models.Book;
import com.example.Models.User;
import com.example.Models.Weather;
import com.example.Presenters.BookPresenter;
import com.example.Presenters.UserPresenter;
import com.example.Presenters.WeatherPresenter;
import com.example.Views.Activitys.MainActivity;
import com.example.Views.Activitys.SearchActivity;
import com.example.Views.Adapters.HourlyAdapter;
import com.example.Views.Adapters.PopularBookAdapter;
import com.example.btl_libary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragment extends Fragment implements WeatherContract.View, BookContract.View.HomeView, UserContract.View {
    private ImageView weatherIcon, userAvatar;
    private TextView rainfall, userName;
    private RecyclerView hourlyTemperature, rcvHightLight;
    private WeatherPresenter weatherPresenter;
    private BookPresenter bookPresenter;
    private UserPresenter userPresenter;
    private ScrollView scrollView;
    private ListView lvAuthor;
    private List<String> authors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        rainfall = view.findViewById(R.id.rainfall);
        rcvHightLight = view.findViewById(R.id.rcvHightLight);
        hourlyTemperature = view.findViewById(R.id.hourlyTemperature);
        lvAuthor = view.findViewById(R.id.lvAuthorPopular);
        userAvatar = view.findViewById(R.id.imgUser);
        userName = view.findViewById(R.id.txtUser);

        hourlyTemperature.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        weatherPresenter = new WeatherPresenter(this, getContext());
        bookPresenter = new BookPresenter(getContext(), this);
        userPresenter = new UserPresenter(getContext(), this);
        userPresenter.loadUsers();
        bookPresenter.loadPopularBooks();
        bookPresenter.loadAuthorBooks();
        weatherPresenter.loadWeather(getString(R.string.weather_api_key), getString(R.string.locatiton));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.findViewById(R.id.scrollHome);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (scrollY > oldScrollY) {
                    mainActivity.hideBottomNavigationView();
                } else {
                    mainActivity.showBottomNavigationView();
                }
            }
        });

        lvAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListAuthor", authors.toString().trim());
                String selectedAuthor = authors.get(position);
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("selectedAuthor", selectedAuthor);
                intent.putExtra("showSearchBar", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showWeather(Weather weather) {
        String iconUrl = "https:" + weather.getCurrent().getCondition().getIcon();
        Picasso.get().load(iconUrl).into(weatherIcon);

        double rainfallAmount = weather.getForecast().getForecastday().get(0).getDay().getTotalprecip_mm();
        rainfall.setText("Lượng mưa: " + rainfallAmount + " mm");

        List<Weather.Forecast.Forecastday.Hour> hourlyData = weather.getForecast().getForecastday().get(0).getHour();
        if (hourlyData != null && !hourlyData.isEmpty()) {
            HourlyAdapter adapter = new HourlyAdapter(hourlyData);
            hourlyTemperature.setAdapter(adapter);
        } else {
            showError("Không có dữ liệu thời tiết !");
        }
    }

    @Override
    public void displayBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvHightLight.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvHightLight.addItemDecoration(itemDecoration);
        rcvHightLight.setAdapter(new PopularBookAdapter(list));
    }

    @Override
    public void displayAuthor(List<String> authorBooks) {
        Log.d("ListAuthor", authorBooks.toString().trim());
        Log.d("ListAuthor", "Size of authorBooks: " + authorBooks.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, authorBooks);
        lvAuthor.setAdapter(adapter);
        authors = authorBooks;
    }

    @Override
    public void displayUser(List<User> userList) {
        if (userList != null && !userList.isEmpty()) {
            User user = userList.get(0);
            if (user.getAvatar() != null) {
                Bitmap avatarBitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
                Log.d("CheckInfor", "avatarBitmap:  " + avatarBitmap + "    userName    " + user.getFullName());
                userAvatar.setImageBitmap(avatarBitmap);
            }
            userName.setText(user.getFullName());
        }
    }

    @Override
    public void showSuccess(String mess) {
        Toast.makeText(getContext(), mess, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showBookDetailsPopup(View anchorView, Book book) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_book_details, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        TextView bookTitle = popupView.findViewById(R.id.bookTitle);
        TextView bookAuthor = popupView.findViewById(R.id.bookAuthor);
        TextView bookDescription = popupView.findViewById(R.id.bookDescription);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookDescription.setText(book.getContent());
        popupWindow.showAsDropDown(anchorView, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}



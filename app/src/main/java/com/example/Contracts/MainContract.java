package com.example.Contracts;

import androidx.fragment.app.Fragment;

import java.util.Map;

public interface MainContract {
    interface View {
        void hideBottomNavigationView();

        void showBottomNavigationView();
    }

    interface Presenter {
        Map<Integer, Class<? extends Fragment>> loadMap();
    }

    interface Model {

    }
}

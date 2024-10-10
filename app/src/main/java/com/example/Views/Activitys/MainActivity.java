package com.example.Views.Activitys;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.Contracts.MainContract;
import com.example.Presenters.MainPresenter;
import com.example.btl_libary.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private Map<Integer, Class<? extends Fragment>> fragmentMap = new HashMap<>();
    private BottomNavigationView navigationView;
    private MainPresenter mainPresenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fragmentMap = mainPresenter.loadMap();
        navigationView = findViewById(R.id.navigation_bottom_view);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Class<? extends Fragment> fragmentClass = fragmentMap.get(item.getItemId());
                if (fragmentClass != null) {
                    try {
                        Fragment fragment = fragmentClass.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    return true;
                }
                return false;
            }
        });
        navigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void hideBottomNavigationView() {
        if (navigationView.isShown()) {
            navigationView.animate()
                    .alpha(0.0f)
                    .setDuration(10)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            navigationView.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void showBottomNavigationView() {
        if (!navigationView.isShown()) {
            navigationView.setAlpha(0.0f);
            navigationView.setVisibility(View.VISIBLE);
            navigationView.animate()
                    .alpha(1.0f)
                    .setDuration(10);
        }
    }


}
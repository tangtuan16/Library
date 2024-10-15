package com.example.Views.Activitys;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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
        createNotificationChannel();
        checkNotificationPermission();



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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Library Notifications";
            String description = "Channel for Library notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    100);
        } else {
            return;
        }
    }



}
package com.example.Views.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_libary.R;

public class LibraryInforActivity extends AppCompatActivity {
    private TextView Address, Directions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_infor);
        Address = findViewById(R.id.Address);
        Directions = findViewById(R.id.Directions);
        Address.setText("Address: 54 phố Triều Khúc, quận Thanh Xuân, thành phố Hà Nội");
        Directions.setText("Chỉ đường");
        Directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryInforActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}

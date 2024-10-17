package com.example.Views.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Presenters.UserPresenter;
import com.example.Models.User;
import com.example.Untils.SharedPreferencesUtil;
import com.example.btl_libary.R;

public class AccountActivity extends AppCompatActivity implements UserPresenter.UserInfoCallback {
    private ImageView imageViewAvatar;
    private TextView textFullName, textEmail, textPhone;
    private Button buttonEditInfo, buttonChangePassword, buttonLogout;
    private UserPresenter userPresenter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
        initPresenter();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        imageViewAvatar = findViewById(R.id.profileImage);
        textFullName = findViewById(R.id.profileName);
        textEmail = findViewById(R.id.profileEmail);
        textPhone = findViewById(R.id.profilePhone);
        buttonEditInfo = findViewById(R.id.btnchangeProfile);
        buttonChangePassword = findViewById(R.id.btnchangePassword);
        buttonLogout = findViewById(R.id.btndangXuat);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(this);
        userId = SharedPreferencesUtil.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserData() {
        userPresenter.getUserInfo(userId, this);
    }

    private void setupListeners() {
        buttonEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, EditInfoActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        buttonChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferencesUtil.clearUserLoginState(AccountActivity.this);
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUserInfoLoaded(@NonNull User user) {
        textFullName.setText(user.getFullName());
        textEmail.setText(user.getEmail());
        textPhone.setText(user.getPhone());

        if (user.getAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            imageViewAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onUserInfoError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}

//hehehehe
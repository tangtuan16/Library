package com.example.Views.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Presenters.UserPresenter;
import com.example.Models.User;
import com.example.Untils.SharedPreferencesUtil;
import com.example.btl_libary.R;

public class LoginActivity extends AppCompatActivity implements UserPresenter.LoginCallback {
    private EditText editUsername, editPassword;
    private Button buttonLogin, buttonRegister;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Check if user is already logged in
        if (SharedPreferencesUtil.isLoggedIn(this)) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        initPresenter();
        setupListeners();
    }

    private void initViews() {
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(this, this);
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Vui lòng điền đầy đủ thông tin");
                return;
            }

            userPresenter.loginUser(username, password);
        });

        buttonRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginSuccess(User user) {
        SharedPreferencesUtil.saveUserLoginState(this, user.getId());
        showToast("Đăng nhập thành công!");
        startMainActivity();
    }

    @Override
    public void onLoginFailure() {
        showToast("Thông tin đăng nhập không đúng!");
    }
}
package com.example.Views.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Presenters.UserPresenter;
import com.example.btl_libary.R;

public class RegisterActivity extends AppCompatActivity implements UserPresenter.RegisterCallback {
    private EditText editUsername, editPassword, editFullName, editEmail, editPhone;
    private Button buttonRegister, buttonLogin;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initPresenter();
        setupListeners();
    }

    private void initViews() {
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(this);
    }

    private void setupListeners() {
        buttonRegister.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String fullName = editFullName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();

            userPresenter.registerUser(username, password, fullName, email, phone, this);
        });

        buttonLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
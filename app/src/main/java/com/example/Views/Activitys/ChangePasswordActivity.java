package com.example.Views.Activitys;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Presenters.UserPresenter;
import com.example.btl_libary.R;

public class ChangePasswordActivity extends AppCompatActivity implements UserPresenter.ChangePasswordCallback {
    private EditText editOldPassword, editNewPassword, editConfirmPassword;
    private Button buttonSubmit;
    private UserPresenter userPresenter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();
        initPresenter();
        setupListeners();
    }

    private void initViews() {
        editOldPassword = findViewById(R.id.editOldPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonSubmit = findViewById(R.id.buttonSubmit);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        buttonSubmit.setOnClickListener(v -> {
            String oldPassword = editOldPassword.getText().toString().trim();
            String newPassword = editNewPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            userPresenter.changePassword(userId, oldPassword, newPassword, confirmPassword, this);
        });
    }

    @Override
    public void onChangePasswordSuccess() {
        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onChangePasswordFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
package com.example.Views.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.User;
import com.example.Untils.DBManager;
import com.example.btl_libary.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editOldPassword, editNewPassword, editConfirmPassword;
    private Button buttonSubmit;
    private DBManager dbManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo DBManager
        dbManager = new DBManager(this);
        dbManager.open();

        // Lấy userId từ Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Ánh xạ các view
        editOldPassword = findViewById(R.id.editOldPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editOldPassword.getText().toString().trim();
                String newPassword = editNewPassword.getText().toString().trim();
                String confirmPassword = editConfirmPassword.getText().toString().trim();

                // Kiểm tra các trường thông tin hợp lệ
                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy thông tin người dùng từ cơ sở dữ liệu
                User user = dbManager.getUserById(userId);
                if (user != null) {
                    // Kiểm tra mật khẩu cũ có chính xác không
                    if (!user.getPassword().equals(oldPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không chính xác.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra xem mật khẩu mới có giống mật khẩu cũ không
                    if (oldPassword.equals(newPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới phải khác mật khẩu cũ.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra độ dài mật khẩu mới
                    if (newPassword.length() < 6) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có khớp không
                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật mật khẩu mới vào cơ sở dữ liệu
                    int result = dbManager.updateUser(userId, newPassword, null, null, null, null);
                    if (result > 0) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}

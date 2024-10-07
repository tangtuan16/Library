package com.example.Views.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Untils.DBManager;
import com.example.Models.User;
import com.example.btl_libary.R;

public class AccountActivity extends AppCompatActivity {
    private ImageView imageViewAvatar;
    private TextView textFullName, textEmail, textPhone;
    private Button buttonEditInfo, buttonChangePassword, buttonLogout;
    private DBManager dbManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dbManager = new DBManager(this);
        dbManager.open();

        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("USER_PREF", MODE_PRIVATE).getInt("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu userId không hợp lệ
            return;
        }

        User user = dbManager.getUserById(userId);

        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textFullName = findViewById(R.id.textFullName);
        textEmail = findViewById(R.id.textEmail);
        textPhone = findViewById(R.id.textPhone);
        buttonEditInfo = findViewById(R.id.buttonEditInfo);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonLogout = findViewById(R.id.buttonLogout);

        if (user != null) {
            textFullName.setText(user.getFullName());
            textEmail.setText(user.getEmail());
            textPhone.setText(user.getPhone());

            // Hiển thị avatar nếu có
            if (user.getAvatar() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
                imageViewAvatar.setImageBitmap(bitmap);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, EditInfoActivity.class);
                intent.putExtra("USER_ID", userId); // Truyền userId đến EditInfoActivity
                startActivity(intent);
                finish();
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm logic để đổi mật khẩu nếu cần
                Toast.makeText(AccountActivity.this, "Chức năng đổi mật khẩu chưa được triển khai.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện đăng xuất
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}


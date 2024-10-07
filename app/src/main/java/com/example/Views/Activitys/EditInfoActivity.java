package com.example.Views.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Untils.DBManager;
import com.example.Models.User;
import com.example.btl_libary.R;

import java.io.ByteArrayOutputStream;

public class EditInfoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editFullName, editEmail, editPhone;
    private ImageView imageViewAvatar;
    private Button buttonSave, buttonChooseImage;
    private DBManager dbManager;
    private int userId;
    private Bitmap avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        dbManager = new DBManager(this);
        dbManager.open();

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu userId không hợp lệ
            return;
        }

        User user = dbManager.getUserById(userId);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);

        if (user != null) {
            editFullName.setText(user.getFullName());
            editEmail.setText(user.getEmail());
            editPhone.setText(user.getPhone());

            // Hiển thị avatar nếu có
            if (user.getAvatar() != null) {
                avatarBitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
                imageViewAvatar.setImageBitmap(avatarBitmap);
            }
        }

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                avatarBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewAvatar.setImageBitmap(avatarBitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Không thể chọn hình ảnh.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserInfo() {
        String fullName = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] avatar = null;
        if (avatarBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            avatar = stream.toByteArray();
        }

        int rowsUpdated = dbManager.updateUser(userId, null, fullName, email, phone, avatar);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại activity trước đó
        } else {
            Toast.makeText(this, "Cập nhật không thành công, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Quay lại mà không cần làm gì thêm, thông tin sẽ được tự động cập nhật trong AccountActivity
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}

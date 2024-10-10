package com.example.Views.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Presenters.UserPresenter;
import com.example.Models.User;
import com.example.btl_libary.R;

public class EditInfoActivity extends AppCompatActivity implements UserPresenter.UserInfoCallback, UserPresenter.UpdateUserCallback {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editFullName, editEmail, editPhone;
    private ImageView imageViewAvatar;
    private Button buttonSave, buttonChooseImage;
    private UserPresenter userPresenter;
    private int userId;
    private Bitmap avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initViews();
        initPresenter();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserData() {
        userPresenter.getUserInfo(userId, this);
    }

    private void setupListeners() {
        buttonChooseImage.setOnClickListener(v -> openFileChooser());

        buttonSave.setOnClickListener(v -> {
            String fullName = editFullName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            userPresenter.updateUserInfo(userId, fullName, email, phone, avatarBitmap, this);
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

    @Override
    public void onUserInfoLoaded(User user) {
        editFullName.setText(user.getFullName());
        editEmail.setText(user.getEmail());
        editPhone.setText(user.getPhone());

        if (user.getAvatar() != null) {
            avatarBitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            imageViewAvatar.setImageBitmap(avatarBitmap);
        }
    }

    @Override
    public void onUserInfoError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUpdateSuccess() {
        Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
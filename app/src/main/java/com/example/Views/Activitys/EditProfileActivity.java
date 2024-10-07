package com.example.Views.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_libary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText profileFullName, profilePhoneNum;
    private TextView profileEmailAddress;
    private ImageView profileImageView;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button btnSaveProfileInfo;
    private FirebaseUser user;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        setUpFirebase();
        loadUserData();
        setUpListeners();
    }

    private void initializeViews() {
        profileFullName = findViewById(R.id.profileFullName);
        profileEmailAddress = findViewById(R.id.profileEmailAddress);
        profilePhoneNum = findViewById(R.id.profilePhoneNum);
        profileImageView = findViewById(R.id.profileImageView);
        btnSaveProfileInfo = findViewById(R.id.btnSaveProfileInfo);

        // Change EditText to TextView for email
        profileEmailAddress.setFocusable(false);
        profileEmailAddress.setClickable(false);
    }

    private void setUpFirebase() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void loadUserData() {
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullname");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        profileFullName.setText(fullName);
        profileEmailAddress.setText(email);
        profilePhoneNum.setText(phone);

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView));
    }

    private void setUpListeners() {
        profileImageView.setOnClickListener(view -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, PICK_IMAGE_REQUEST);
        });

        btnSaveProfileInfo.setOnClickListener(view -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        if (!validateInputs()) {
            return;
        }

        final String fullName = profileFullName.getText().toString().trim();
        final String phone = profilePhoneNum.getText().toString().trim();

        updateUserInfo(fullName, phone);
    }

    private boolean validateInputs() {
        if (profileFullName.getText().toString().trim().isEmpty() ||
                profilePhoneNum.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUserInfo(String fullName, String phone) {
        DocumentReference docRef = fStore.collection("users").document(user.getUid());
        Map<String, Object> edited = new HashMap<>();
        edited.put("fName", fullName);
        edited.put("phone", phone);
        docRef.update(edited)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    if (imageUri != null) {
                        uploadImage();
                    } else {
                        navigateToAccountActivity();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi cập nhật thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadImage() {
        StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(profileImageView);
                        Toast.makeText(this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                        navigateToAccountActivity();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateToAccountActivity() {
        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
}
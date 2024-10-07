package com.example.Views.Activitys;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_libary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST = 1;

    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button btnchangePassword, btnchangeProfile;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initializeViews();
        setUpFirebase();
        loadUserInfo();
        setUpListeners();
    }

    private void initializeViews() {
        phone = findViewById(R.id.profilePhone);
        email = findViewById(R.id.profileEmail);
        fullName = findViewById(R.id.profileName);
        btnchangePassword = findViewById(R.id.btnchangePassword);
        profileImage = findViewById(R.id.profileImage);
        btnchangeProfile = findViewById(R.id.btnchangeProfile);
    }

    private void setUpFirebase() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        user = fAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        } else {
            Toast.makeText(this, "Không thể xác thực người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    private void loadUserInfo() {
        if (userID == null) return;

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Lỗi khi tải thông tin người dùng", error);
                    Toast.makeText(AccountActivity.this, "Lỗi khi tải thông tin: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                } else {
                    Log.d(TAG, "Không tìm thấy dữ liệu người dùng");
                    Toast.makeText(AccountActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadProfileImage();
    }

    private void loadProfileImage() {
        StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Lỗi khi tải ảnh đại diện: " + e.getMessage());
            }
        });
    }

    private void setUpListeners() {
        btnchangeProfile.setOnClickListener((v) -> {
            Intent i = new Intent(v.getContext(), EditProfileActivity.class);
            i.putExtra("fullname", fullName.getText().toString());
            i.putExtra("email", email.getText().toString());
            i.putExtra("phone", phone.getText().toString());
            startActivityForResult(i, EDIT_PROFILE_REQUEST);
        });

        btnchangePassword.setOnClickListener((v) -> {
            showChangePasswordDialog();
        });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(this);
        passwordResetDialog.setTitle("Đặt lại mật khẩu");
        passwordResetDialog.setMessage("Nhập mật khẩu mới (ít nhất 6 ký tự)");

        final EditText resetPassword = new EditText(this);
        passwordResetDialog.setView(resetPassword);

        passwordResetDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPassword = resetPassword.getText().toString().trim();
                if (newPassword.isEmpty() || newPassword.length() < 6) {
                    Toast.makeText(AccountActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AccountActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("Hủy", null);
        passwordResetDialog.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            loadUserInfo();
            Toast.makeText(this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
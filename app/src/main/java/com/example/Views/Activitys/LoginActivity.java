package com.example.Views.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_libary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail, editPasword;
    Button btnDangNhap;
    TextView txtChuaDK, txtquenMK;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.editEmail);
        editPasword = findViewById(R.id.editPasword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtChuaDK = findViewById(R.id.txtChuaDK);
        fAuth = FirebaseAuth.getInstance();
        txtquenMK = findViewById(R.id.txtquenMK);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPasword.getText().toString().trim();

                if (email.isEmpty()) {
                    editEmail.setError("Không được để trống email");
                    editEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    editPasword.setError("Không được để trống mật khẩu");
                    editPasword.requestFocus();
                    return;
                }
                if (password.length()<6){
                    editPasword.setError("Mật khẩu phải có ít nhất 6 kí tự");
                    editPasword.requestFocus();
                    return;
                }

                //authen

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Đã đăng nhập", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Lỗi đăng nhập" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        txtChuaDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        txtquenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Đặt lại mật khẩu");
                passwordResetDialog.setMessage("Nhập email của bạn để đặt lại mật khẩu");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Đã gửi email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Lỗi, chưa gửi email đặt lại mật khẩu" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                passwordResetDialog.create().show();


            }
        });

    }
}
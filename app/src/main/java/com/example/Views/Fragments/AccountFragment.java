package com.example.Views.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Presenters.UserPresenter;
import com.example.Models.User;
import com.example.Untils.SharedPreferencesUtil;
import com.example.Views.Activitys.EditInfoActivity;
import com.example.Views.Activitys.ChangePasswordActivity;
import com.example.Views.Activitys.LoginActivity;
import com.example.btl_libary.R;

public class AccountFragment extends Fragment implements UserPresenter.UserInfoCallback {
    private ImageView imageViewAvatar;
    private TextView textFullName, textEmail, textPhone;
    private Button buttonEditInfo, buttonChangePassword, buttonLogout;
    private UserPresenter userPresenter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        initViews(view);
        initPresenter();
        setupListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserData();
    }

    private void initViews(@NonNull View view) {
        imageViewAvatar = view.findViewById(R.id.profileImage);
        textFullName = view.findViewById(R.id.profileName);
        textEmail = view.findViewById(R.id.profileEmail);
        textPhone = view.findViewById(R.id.profilePhone);
        buttonEditInfo = view.findViewById(R.id.btnchangeProfile);
        buttonChangePassword = view.findViewById(R.id.btnchangePassword);
        buttonLogout = view.findViewById(R.id.btndangXuat);
    }

    private void initPresenter() {
        userPresenter = new UserPresenter(requireContext());
        userId = SharedPreferencesUtil.getUserId(requireContext());

        if (userId == -1) {
            Toast.makeText(requireContext(), "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
    }

    private void loadUserData() {
        userPresenter.getUserInfo(userId, this);
    }

    private void setupListeners() {
        buttonEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditInfoActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        buttonChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferencesUtil.clearUserLoginState(requireContext());
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onUserInfoLoaded(User user) {
        textFullName.setText(user.getFullName());
        textEmail.setText(user.getEmail());
        textPhone.setText(user.getPhone());

        if (user.getAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            imageViewAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onUserInfoError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}

///
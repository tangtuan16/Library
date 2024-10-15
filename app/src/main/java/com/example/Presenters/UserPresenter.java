package com.example.Presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.Contracts.UserContract;
import com.example.Models.User;
import com.example.Models.UserModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserPresenter {
    private Context context;
    private UserModel userModel;
    private UserContract.View userView;

    public UserPresenter(Context context, UserContract.View userView) {
        this.userModel = new UserModel(context);
        this.userView = userView;
    }

    public void loadUsers() {
        List<User> userList = userModel.getAllUsers();
        userView.displayUser(userList);
    }

    //CODE SON
    // Callbacks for different operations
    public interface RegisterCallback {
        void onRegisterSuccess();
        void onRegisterFailure(String message);
    }

    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailure();
    }

    public interface UserInfoCallback {
        void onUserInfoLoaded(User user);
        void onUserInfoError(String message);
    }

    public interface UpdateUserCallback {
        void onUpdateSuccess();
        void onUpdateFailure(String message);
    }

    public interface ChangePasswordCallback {
        void onChangePasswordSuccess();
        void onChangePasswordFailure(String message);
    }

    private LoginCallback loginCallback;

    public UserPresenter(Context context) {
        this.context = context;
        this.userModel = new UserModel(context);
    }

    public UserPresenter(Context context, LoginCallback loginCallback) {
        this.context = context;
        this.userModel = new UserModel(context);
        this.loginCallback = loginCallback;
    }

    // Handle user registration
    public void registerUser(String username, String password, String fullName,
                             String email, String phone, RegisterCallback callback) {
        // Validate input
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()
                || email.isEmpty() || phone.isEmpty()) {
            callback.onRegisterFailure("Vui lòng điền đầy đủ thông tin");
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onRegisterFailure("Email không hợp lệ");
            return;
        }

        // Validate phone number format
        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            callback.onRegisterFailure("Số điện thoại không hợp lệ");
            return;
        }

        // Check if username already exists
        if (userModel.getUserByUsername(username) != null) {
            callback.onRegisterFailure("Tên đăng nhập đã tồn tại");
            return;
        }

        // Attempt to register user
        long result = userModel.registerUser(username, password, fullName, email, phone);
        if (result != -1) {
            callback.onRegisterSuccess();
        } else {
            callback.onRegisterFailure("Đăng ký thất bại");
        }
    }

    // Handle user login
    public void loginUser(String username, String password) {
        if (userModel.loginUser(username, password)) {
            User user = userModel.getUserByUsername(username);
            loginCallback.onLoginSuccess(user);
        } else {
            loginCallback.onLoginFailure();
        }
    }

    // Get user information
    public void getUserInfo(int userId, UserInfoCallback callback) {
        User user = userModel.getUserById(userId);
        if (user != null) {
            callback.onUserInfoLoaded(user);
        } else {
            callback.onUserInfoError("Không thể tải thông tin người dùng");
        }
    }

    // Update user information
    public void updateUserInfo(int userId, String fullName, String email, String phone,
                               Bitmap avatar, UpdateUserCallback callback) {
        // Validate input
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            callback.onUpdateFailure("Vui lòng điền đầy đủ thông tin");
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onUpdateFailure("Email không hợp lệ");
            return;
        }

        // Validate phone number format
        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            callback.onUpdateFailure("Số điện thoại không hợp lệ");
            return;
        }

        // Convert bitmap to byte array if avatar is provided
        byte[] avatarBytes = null;
        if (avatar != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.PNG, 100, stream);
            avatarBytes = stream.toByteArray();
        }

        // Attempt to update user information
        int result = userModel.updateUser(userId, null, fullName, email, phone, avatarBytes);
        if (result > 0) {
            callback.onUpdateSuccess();
        } else {
            callback.onUpdateFailure("Cập nhật thông tin thất bại");
        }
    }

    // Change user password
    public void changePassword(int userId, String oldPassword, String newPassword,
                               String confirmPassword, ChangePasswordCallback callback) {
        // Validate input
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            callback.onChangePasswordFailure("Vui lòng điền đầy đủ thông tin");
            return;
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            callback.onChangePasswordFailure("Mật khẩu mới không khớp");
            return;
        }

        // Get user and verify old password
        User user = userModel.getUserById(userId);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            callback.onChangePasswordFailure("Mật khẩu cũ không đúng");
            return;
        }

        // Attempt to update password
        int result = userModel.updateUser(userId, newPassword, null, null, null, null);
        if (result > 0) {
            callback.onChangePasswordSuccess();
        } else {
            callback.onChangePasswordFailure("Đổi mật khẩu thất bại");
        }
    }
}
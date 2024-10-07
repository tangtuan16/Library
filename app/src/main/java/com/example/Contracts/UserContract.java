package com.example.Contracts;

public class UserContract {
    public interface UserView {
        void showLoginSuccess();

        void showLoginError();

    }

    public interface UserPresenter {
        void loginUser(String username, String password);

        void registerUser(String username, String password, String fullName, String email, String phone);

        void updateUser(int userId, String password, String fullName, String email, String phone, byte[] avatar);

        void getUserById(int userId);

        void getUserByUsername(String username);



    }
}

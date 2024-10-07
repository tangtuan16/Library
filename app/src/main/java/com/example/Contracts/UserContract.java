package com.example.Contracts;

import com.example.Models.User;

import java.util.List;

public interface UserContract {
    interface View {
        void displayUser(List<User> userList);
    }

    interface Presenter {
        void loadUsers();
    }

    interface Model {
        List<User> getAllUsers();
    }
}

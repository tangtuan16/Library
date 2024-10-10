package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.UserContract;
import com.example.Models.User;
import com.example.Models.UserModel;

import java.util.List;

public class UserPresenter implements UserContract.Presenter {
    private UserModel userModel;
    private final UserContract.View userView;

    public UserPresenter(Context context, UserContract.View userView) {
        this.userModel = new UserModel(context);
        this.userView = userView;
    }

    @Override
    public void loadUsers() {
        List<User> userList = userModel.getAllUsers();
        userView.displayUser(userList);
    }
}


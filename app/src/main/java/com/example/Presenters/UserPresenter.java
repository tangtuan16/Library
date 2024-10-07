package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.UserContract;
import com.example.Models.UserModel;

public class UserPresenter {
    private UserModel userModel;
    private UserContract.UserView userView;

    public UserPresenter(Context context, UserContract.UserView userView) {
        this.userView = userView;
        this.userModel = new UserModel(context);
    }

}

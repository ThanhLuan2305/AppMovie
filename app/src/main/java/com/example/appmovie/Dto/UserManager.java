package com.example.appmovie.Dto;

import com.example.appmovie.Model.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
        // Private constructor để ngăn việc khởi tạo đối tượng từ bên ngoài
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
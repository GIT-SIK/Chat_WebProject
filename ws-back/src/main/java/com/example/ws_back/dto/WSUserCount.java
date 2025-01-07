package com.example.ws_back.dto;

public class WSUserCount {
    
    private int userCount;

    // 생성자
    public WSUserCount(int userCount) {
        this.userCount = userCount;
    }

    // getter
    public int getUserCount() {
        return userCount;
    }

    // setter
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}

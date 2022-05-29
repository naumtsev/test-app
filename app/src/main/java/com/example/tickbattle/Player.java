package com.example.tickbattle;

public class Player {
    int color;
    private String username;
    Player(String username, int color) {
        this.color = color;
        this.username = username;
    }

    public int getColor() {
        return color;
    }

    public String getUsername() {
        return username;
    }
}

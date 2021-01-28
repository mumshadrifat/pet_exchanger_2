package com.example.nmi;

public class users {

    private String username;
    private  String userImage;


    public users() {
    }

    public users(String username, String userImage) {
        this.username = username;
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getUserImage()
    {
        return userImage;
    }

    public void setUserImage(String userImage) {

        this.userImage = userImage;
    }
}



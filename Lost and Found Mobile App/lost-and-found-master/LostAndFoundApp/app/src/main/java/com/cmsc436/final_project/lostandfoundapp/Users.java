package com.cmsc436.final_project.lostandfoundapp;


public class Users {
    private String id;
    private String email;
    private String password;
    private int rating;
    private String username;
    private String imageURL;

    int[] ratings;

    public Users(String email, int rating, String username,String imageURL, String id){
        this.email = email;
        this.rating = rating;
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
    }
    public Users()
    {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int[] getRatings() {
        return ratings;
    }

    public void setRatings(int[] ratings) {
        this.ratings = ratings;
    }
}

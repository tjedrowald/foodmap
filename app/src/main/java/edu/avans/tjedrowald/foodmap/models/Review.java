package edu.avans.tjedrowald.foodmap.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;



public class Review implements Serializable {
    @JsonGetter("text")
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    String text;

    @JsonGetter("user")
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    User user;

    @JsonGetter("rating")
    public int getRating() {
        return this.rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    int rating;

    @JsonGetter("time_created")
    public String getTimeCreated() {
        return this.timeCreated;
    }
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }
    String timeCreated;

    @JsonGetter("url")
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    String url;
}

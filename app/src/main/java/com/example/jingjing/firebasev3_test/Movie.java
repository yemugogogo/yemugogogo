package com.example.jingjing.firebasev3_test;

public class Movie {
    String city;
    String movie;
    String theater;

    public Movie(){

    }

    public Movie(String city, String movie, String theater) {
        this.city = city;
        this.movie = movie;
        this.theater = theater;
    }

    public String getCity() {
        return city;
    }

    public String getMovie() {
        return movie;
    }

    public String getTheater() {
        return theater;
    }
}

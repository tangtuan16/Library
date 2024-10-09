package com.example.Models;

public class GenreData {
    private String genre;
    private int total;

    public GenreData(String genre, int total) {
        this.genre = genre;
        this.total = total;
    }

    public String getGenre() {
        return genre;
    }

    public int getTotal() {
        return total;
    }
}

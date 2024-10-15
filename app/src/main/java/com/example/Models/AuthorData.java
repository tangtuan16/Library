package com.example.Models;

public class AuthorData {
    private String author;
    private int total;

    public AuthorData(String author, int total) {
        this.author = author;
        this.total = total;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotal() {
        return total;
    }
}

package com.example.Models;

public class Book {
    private int id, avt;
    private String title, author, desc;

    public Book(int id, int avt, String title, String author, String desc) {
        this.id = id;
        this.avt = avt;
        this.title = title;
        this.author = author;
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public int getAvt() {
        return avt;
    }

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

package com.example.Models;

public class Book {
    private int id, avt;
    private String title, author, category;
    private String content;

    public Book(int id, int avt, String title, String author, String category) {
        this.id = id;
        this.avt = avt;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public int getAvt() {
        return avt;
    }

    public String getDesc() {
        return category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

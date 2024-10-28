package com.example.Models;
public class CartItem {
    private int userId;
    private int bookId;
    private String author;
    private String title;
    private String genre;
    private int avt;
    public CartItem(int userId, int bookId, String title, String author, String genre, int avt) {
        this.userId = userId;
        this.bookId = bookId;
        this.title =title;
        this.author = author;
        this.genre = genre;
        this.avt = avt;
    }
    public int getUserId() {
        return userId;
    }
    public int getBookId() {
        return bookId;
    }
    public String getTitle() {
        return title;
    }
    public int getAvt(){
        return avt;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
}

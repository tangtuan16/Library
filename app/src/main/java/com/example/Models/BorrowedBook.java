package com.example.Models;

public class BorrowedBook {

    private int id,bookid, avt,total;
    private String title, author, category,borrowedDate,returnDate,position;

    public BorrowedBook(int id,int bookid, int avt, String title, String author, String category,int total, String borrowedDate, String returnDate, String position) {
        this.id = id;
        this.bookid=bookid;
        this.avt = avt;
        this.title = title;
        this.author = author;
        this.category = category;
        this.total=total;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getAvt() {
        return avt;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getPosition() {
        return position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvt(int avt) {
        this.avt = avt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

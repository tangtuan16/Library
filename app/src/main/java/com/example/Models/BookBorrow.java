package com.example.Models;

public class BookBorrow {
    private int id;//concac
    private int userId;
    private int bookId;
    private int bookTotal;
    private String title;
    private String borrowedDate;
    private String returnDate;

    public BookBorrow(int bookId, int userId, String title, int bookTotal, String borrowedDate, String returnDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.bookTotal = bookTotal;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
    }

    public int getBookId() {
        return bookId;
    }

    public int getBookTotal() {
        return bookTotal;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public int getId() {
        return id;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }
}

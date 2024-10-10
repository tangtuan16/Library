package com.example.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(avt);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(desc);
    }

    protected Book(Parcel in) {
        id = in.readInt();
        avt = in.readInt();
        title = in.readString();
        author = in.readString();
        desc = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}

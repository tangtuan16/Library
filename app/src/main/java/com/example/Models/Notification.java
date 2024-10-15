package com.example.Models;

public class Notification {
    private int id;
    private String title;
    private String content;
    private String time;
    private int status;

    public Notification(int id, String title, String content, String time, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;

    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

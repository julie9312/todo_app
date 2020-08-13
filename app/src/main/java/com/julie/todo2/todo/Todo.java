package com.julie.todo2.todo;

public class Todo {
    private int id;
    private String title;
    private String date;
    private String completed;

    public Todo(int id, String title, String date, String completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.completed = completed;
    }
    public Todo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}

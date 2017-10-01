package com.example.yuda.myapplication;

/**
 * Created by yuda on 01/10/2017.
 */

public class MyMovie {
    private int id;
    private String name;
    private int year;
    private String category;

    public MyMovie(int id, String name, int year, String category) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.category = category;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

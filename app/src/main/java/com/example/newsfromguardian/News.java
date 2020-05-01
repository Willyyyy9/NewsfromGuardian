package com.example.newsfromguardian;

public class News {
    private String title;
    private String sectionName;
    private String date;
    private String time;
    private String url;

    // Constructor
    public News(String title, String sectionName, String date, String time, String url) {
        this.title = title;
        this.sectionName = sectionName;
        this.date = date;
        this.time = time;
        this.url = url;
    }

    //Getters
    public String getTitle() { return title; }

    public String getSectionName() { return sectionName; }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getUrl() { return url; }
}

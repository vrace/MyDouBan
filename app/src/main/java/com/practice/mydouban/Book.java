package com.practice.mydouban;

import android.text.TextUtils;

public class Book {
    public String title;
    public String image;
    public String author;
    public String publisher;
    public String publishDate;
    public String summary;
    public double rating;

    public Book(String title, String image, String author, String publisher, String publishDate, String summary, double rating) {
        this.title = title;
        this.image = image;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.summary = summary;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getSummary() {
        return summary;
    }

    public double getRating() {
        return rating;
    }

    public String getInformation() {
        return TextUtils.join("/", new String[]{
                getAuthor(), getPublisher(), getPublishDate()
        });
    }
}

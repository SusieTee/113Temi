package com.example.BookLibraryTemi.model;
import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("publication_year")
    private String publicationYear;
    @SerializedName("shelf_location")
    private String shelfLocation;
    private String language;
    private int id;

    public Book(String title, String author, String publicationYear, String shelfLocation, String language, int id) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.shelfLocation = shelfLocation;
        this.language = language;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getPublicationYear() {

        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getShelfLocation() {

        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {

        this.shelfLocation = shelfLocation;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return title;
    }
}



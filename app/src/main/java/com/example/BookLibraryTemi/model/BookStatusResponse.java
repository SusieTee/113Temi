package com.example.BookLibraryTemi.model;
import com.google.gson.annotations.SerializedName;

public class BookStatusResponse {
    @SerializedName("is_borrowed")
    private boolean isBorrowed;

    public BookStatusResponse() {
        this.isBorrowed = false;
    }

    public BookStatusResponse(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
}



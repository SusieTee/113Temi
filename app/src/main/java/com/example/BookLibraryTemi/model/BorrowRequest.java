package com.example.BookLibraryTemi.model;
import com.google.gson.annotations.SerializedName;

public class BorrowRequest {
    @SerializedName("student_id")
    private String studentId;

    @SerializedName("book_id")
    private String bookId;

    public BorrowRequest(String studentId, String bookId) {
        this.studentId = studentId;
        this.bookId = bookId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}






package com.example.BookLibraryTemi.model;
import com.google.gson.annotations.SerializedName;

public class BorrowResponse {
    @SerializedName("book_id")
    private String bookId;

    @SerializedName("student_id")
    private String studentId;

    @SerializedName("message")
    private String message;

    public BorrowResponse() {
    }

    public BorrowResponse(String bookId, String studentId, String message) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.message = message;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BorrowResponse{" +
                "bookId='" + bookId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}



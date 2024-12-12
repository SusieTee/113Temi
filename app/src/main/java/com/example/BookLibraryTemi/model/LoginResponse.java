package com.example.BookLibraryTemi.model;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private boolean success;
    private String message;

    @SerializedName("student_id")
    private String studentId;

    @SerializedName("student_number")
    private String studentNumber;

    public String getStudentId() {
        return studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



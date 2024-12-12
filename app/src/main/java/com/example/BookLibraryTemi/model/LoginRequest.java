package com.example.BookLibraryTemi.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("student_number")
    private String studentNumber;
    @SerializedName("password")
    private String password;

    public LoginRequest(String studentNumber, String password) {
        this.studentNumber = studentNumber;
        this.password = password;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


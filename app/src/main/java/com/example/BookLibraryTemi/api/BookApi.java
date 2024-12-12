package com.example.BookLibraryTemi.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import java.util.List;
import com.example.BookLibraryTemi.model.Book;
import com.example.BookLibraryTemi.model.LoginRequest;
import com.example.BookLibraryTemi.model.LoginResponse;
import com.example.BookLibraryTemi.model.ReturnRequest;
import com.example.BookLibraryTemi.model.ReturnResponse;
import com.example.BookLibraryTemi.model.BorrowRequest;
import com.example.BookLibraryTemi.model.BorrowResponse;
import com.example.BookLibraryTemi.model.BookStatusResponse;

public interface BookApi {
    @GET("books")
    Call<List<Book>> getBooks();

    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("title") String title);
    @POST("students/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("books/status/{bookId}")
    Call<BookStatusResponse> getBookStatus(@Path("bookId") String bookId);

    @POST("books/borrow")
    Call<BorrowResponse> borrowBook(@Body BorrowRequest borrowRequest);

    @POST("books/return")
    Call<ReturnResponse> returnBook(@Body ReturnRequest returnRequest);

}
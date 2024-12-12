package com.example.BookLibraryTemi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BookLibraryTemi.api.BookApi;
import com.example.BookLibraryTemi.model.BorrowRequest;
import com.example.BookLibraryTemi.model.BorrowResponse;
import com.example.BookLibraryTemi.model.ReturnRequest;
import com.example.BookLibraryTemi.model.ReturnResponse;
import com.example.BookLibraryTemi.model.BookStatusResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class borrow extends AppCompatActivity {

    private BookApi bookApi;
    private String studentId;
    private EditText bookIdInput;
    private Button borrowButton, returnButton, logoutButton;
    private Retrofit retrofit;

    private TextView resultTextView;
    private String BASE_URL = "http://125.229.145.125:4000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowbooks);

        studentId = getIntent().getStringExtra("student_id");

        bookIdInput = findViewById(R.id.bookIdInput);
        borrowButton = findViewById(R.id.borrowButton);
        returnButton = findViewById(R.id.returnButton);
        logoutButton = findViewById(R.id.logoutButton);
        resultTextView = findViewById(R.id.resultTextView);

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bookApi = ApiClient.getClient().create(BookApi.class);


        borrowButton.setOnClickListener(v -> {
            String bookId = bookIdInput.getText().toString();
            if (bookId.isEmpty()) {
                Toast.makeText(borrow.this, "Enter Book ID", Toast.LENGTH_SHORT).show();
            } else {
                checkBookStatus(bookId, true);
            }
        });

        returnButton.setOnClickListener(v -> {
            String bookId = bookIdInput.getText().toString();
            if (bookId.isEmpty()) {
                Toast.makeText(borrow.this, "Enter Book ID", Toast.LENGTH_SHORT).show();
            } else {
                checkBookStatus(bookId, false);
            }
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(borrow.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }


    private void checkBookStatus(String bookId, boolean isBorrowed) {
        Call<BookStatusResponse> call = bookApi.getBookStatus(bookId);
        call.enqueue(new Callback<BookStatusResponse>() {
            @Override
            public void onResponse(Call<BookStatusResponse> call, Response<BookStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookStatusResponse statusResponse = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    String studentId = sharedPreferences.getString("student_id", null);
                    if (isBorrowed) {
                        BorrowRequest borrowRequest = new BorrowRequest(studentId, bookId);
                        borrowBook(borrowRequest);
                    } else {
                        ReturnRequest returnRequest = new ReturnRequest(studentId, bookId);
                        returnBook(returnRequest);
                    }
                } else {
                    Toast.makeText(borrow.this, "Failed to get book status", Toast.LENGTH_SHORT).show();
                }
            }
        @Override
        public void onFailure(Call<BookStatusResponse> call, Throwable t) {
            Toast.makeText(borrow.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}


    private void borrowBook(BorrowRequest borrowRequest) {
        Call<BorrowResponse> call = bookApi.borrowBook(borrowRequest);
        call.enqueue(new Callback<BorrowResponse>() {
            @Override
            public void onResponse(Call<BorrowResponse> call, Response<BorrowResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BorrowResponse borrowResponse = response.body();
                    String result = "Book ID: " + borrowResponse.getBookId() +
                            " , Student ID: " + borrowResponse.getStudentId() + "\n"+
                            "Message: " + borrowResponse.getMessage();
                    resultTextView.setText(result);
                    Toast.makeText(borrow.this, "Book borrowed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(borrow.this, "This book is already borrowed", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(borrow.this, "Borrowing failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BorrowResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void returnBook(ReturnRequest returnRequest) {
        Call<ReturnResponse> call = bookApi.returnBook(returnRequest);
        call.enqueue(new Callback<ReturnResponse>() {
            @Override
            public void onResponse(Call<ReturnResponse> call, Response<ReturnResponse> response) {
                if (response.isSuccessful()) {
                    ReturnResponse returnResponse = response.body();
                    String result = "Book ID: " + returnResponse.getBookId() +
                            " , Student ID: " + returnResponse.getStudentId() + "\n"+
                            "Message: " + returnResponse.getMessage();
                    resultTextView.setText(result);
                    Toast.makeText(borrow.this, "Book returned successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(borrow.this, "This book is not currently borrowed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(borrow.this, "Failed to return the book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReturnResponse> call, Throwable t) {
                Toast.makeText(borrow.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = event.getRawX() + v.getLeft() - location[0];
                float y = event.getRawY() + v.getTop() - location[1];

                if (x < 0 || x > v.getWidth() || y < 0 || y > v.getHeight()) {
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}







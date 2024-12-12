package com.example.BookLibraryTemi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.BookLibraryTemi.api.BookApi;
import com.example.BookLibraryTemi.model.Book;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;


public class OneSearchActivity extends AppCompatActivity{

    private EditText searchBar;

    private TextView bookDetails;
    private ListView searchResult;
    private Button searchButton, startGuideButton;
    private Retrofit retrofit;
    private BookApi bookApi;
    private Robot robot;

    private ArrayAdapter<Book> booksAdapter;
    private List<Book> books = new ArrayList<>();
    private String BASE_URL = "http://125.229.145.125:4000/";

    private Book selectedBook = null;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onesearch);

        robot = Robot.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        startGuideButton = findViewById(R.id.btn_start_guide);
        searchButton = findViewById(R.id.btn_search_button);
        searchResult = findViewById(R.id.searchResult);
        searchBar = findViewById(R.id.search_bar);
        bookDetails = findViewById(R.id.bookDetails);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApi = retrofit.create(BookApi.class);
        startGuideButton.setEnabled(false);

        booksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, books);
        searchResult.setAdapter(booksAdapter);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_search) {
                return true;
            } else if (itemId == R.id.bottom_book) {
                startActivity(new Intent(getApplicationContext(), login.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_map) {
                startActivity(new Intent(getApplicationContext(), LibraryTourActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });


        searchButton.setOnClickListener(v -> {
            String title = searchBar.getText().toString();
            if (!title.isEmpty()) {
                searchBooks(title);
            } else {
                Toast.makeText(this, "Please enter the book title", Toast.LENGTH_SHORT).show();
            }
        });

        searchResult.setOnItemClickListener((parent, view, position, id) -> {
            //Book
            selectedBook = books.get(position);
            String details = "Title: " + selectedBook.getTitle() + "\n" +
                    "Author: " + selectedBook.getAuthor() + "\n" +
                    "shelfLocation: " + selectedBook.getShelfLocation();
            bookDetails.setText(details);
            startGuideButton.setEnabled(true);
        });

        startGuideButton.setOnClickListener(v -> {
            if (selectedBook != null) {
                String location = selectedBook.getShelfLocation();
                if (location != null && !location.isEmpty()) {
                    robot.goTo(location);
                    Toast.makeText(this, "Navigating to " + location, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid shelf location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select a book first", Toast.LENGTH_SHORT).show();
            }
        });

        robot.addOnGoToLocationStatusChangedListener(new OnGoToLocationStatusChangedListener() {
            @Override
            public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description) {
                handleNavigationStatus(location, status);
            }
        });
    }

    private void searchBooks(String title) {
        Call<List<Book>> call = bookApi.searchBooks(title);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> resultBooks = response.body();
                    if (resultBooks != null && !resultBooks.isEmpty()) {
                        books.clear();
                        books.addAll(resultBooks);
                        booksAdapter.notifyDataSetChanged();
                        bookDetails.setText("");
                    } else {
                        Toast.makeText(OneSearchActivity.this, "No related books found", Toast.LENGTH_SHORT).show();
                    }
                    } else {
                    Toast.makeText(OneSearchActivity.this, "Request failed, please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(OneSearchActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleNavigationStatus(String location, String status) {
        switch (status) {
            case "start":
                Toast.makeText(this, "Starting navigation to: " + location, Toast.LENGTH_SHORT).show();
                break;

            case "complete":
                Toast.makeText(this, "Arrived at: " + location, Toast.LENGTH_SHORT).show();
                robot.speak(TtsRequest.create("Arrived at shelf " + location + ". Please take your book.", false));
                selectedBook = null;
                handler.postDelayed(this::checkForNewTaskOrDock, 100000);
                break;

            case "abort":
                Toast.makeText(this, "Navigation aborted, please try again", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "Navigation status: " + status, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkForNewTaskOrDock() {
        if (selectedBook == null) {
            robot.goTo("home base");
            robot.speak(TtsRequest.create("No new tasks detected. Returning to charging station.", false));
        } else {
            Toast.makeText(this, "Ready for the next query.", Toast.LENGTH_SHORT).show();
        }
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


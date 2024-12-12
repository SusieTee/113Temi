package com.example.BookLibraryTemi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.BookLibraryTemi.api.BookApi;
import com.example.BookLibraryTemi.model.LoginRequest;
import com.example.BookLibraryTemi.model.LoginResponse;

public class login extends AppCompatActivity {

    private Button btnSignIn;

    private String studentId;
    private EditText etUsername, etPassword;
    private BookApi bookApi;
    private Retrofit retrofit;
    private String BASE_URL = "http://125.229.145.125:4000/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinapp);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        btnSignIn = findViewById(R.id.btn_signin);
        etUsername = findViewById(R.id.et_signin_username);
        etPassword = findViewById(R.id.et_signin_password);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApi = retrofit.create(BookApi.class);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), OneSearchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_book) {
                return true;
            } else if (itemId == R.id.bottom_map) {
                startActivity(new Intent(getApplicationContext(), LibraryTourActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });


        btnSignIn.setOnClickListener(view -> {
            String studentNumber = etUsername.getText().toString();
            String password = etPassword.getText().toString();


            if (studentNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Please enter student number and password", Toast.LENGTH_SHORT).show();
            } else {
                login(new LoginRequest(studentNumber, password));
            }
        });
    }

    private void login(LoginRequest loginRequest) {
        Call<LoginResponse> call = bookApi.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String studentId = response.body().getStudentId();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("student_id", studentId);
                    editor.apply();

                    Intent intent = new Intent(login.this, borrow.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(login.this, "Login Failure: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(login.this, "Login Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
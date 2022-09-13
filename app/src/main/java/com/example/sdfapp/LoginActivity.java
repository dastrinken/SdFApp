package com.example.sdfapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdfapp.model.ConnectionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText user;
    private EditText pw;
    private TextView response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        user = findViewById(R.id.username);
        pw = findViewById(R.id.password);

        response = findViewById(R.id.loginResponseText);
    }

    public void userLogin(View view) {
        final String username = user.getText().toString();
        final String password = pw.getText().toString();

        ConnectionManager.getInstance().login(username, password);
    }
}

package com.example.sdfapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdfapp.model.ConnectionInterface;
import com.example.sdfapp.model.ConnectionManager;

import java.sql.Connection;

public class LoginActivity extends AppCompatActivity implements ConnectionInterface {
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

        ConnectionManager.getInstance().login(username, password, this);
    }

    @Override
    public void onLoginProcessed(boolean success) {
        if(success) {
            Log.w("RESPONSE ON SUCCESS: ", String.valueOf(success));

            String auth_token = ConnectionManager.getInstance().getAuthToken();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("auth_token",auth_token);
            editor.apply();
        } else {
            Log.w("RESPONSE ON FAILURE: ", String.valueOf(success));
        }
    }
}

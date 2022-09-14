package com.example.sdfapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class RaidActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_raid);
        ImageView titleBar = findViewById(R.id.titleBarRaid);
        titleBar.setImageResource(R.drawable.logo2021);
    }
}

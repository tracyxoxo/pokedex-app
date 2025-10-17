package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnPokedex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnPokedex = findViewById(R.id.btnPokedex);

        String username = getIntent().getStringExtra("username");
        tvWelcome.setText("Hi, " + username + "!");

        btnPokedex.setOnClickListener(v -> {
            startActivity(new Intent(this, PokedexActivity.class));
        });
    }
}

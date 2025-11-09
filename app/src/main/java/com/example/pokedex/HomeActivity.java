package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnPokedex;                 // keep as Button if your XML uses <Button>
    MaterialButton btnAddPokemon;      // XML uses <MaterialButton> for this one
    String username;                   // <-- make it a field so listeners can use it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // views
        tvWelcome     = findViewById(R.id.tvWelcome);
        btnPokedex    = findViewById(R.id.btnPokedex);
        btnAddPokemon = findViewById(R.id.btnAddPokemon);

        // get username ONCE, before listeners
        username = getIntent().getStringExtra("username");
        tvWelcome.setText("Hi, " + username + "!");

        // open pokedex
        btnPokedex.setOnClickListener(v -> {
            Intent i = new Intent(this, PokedexActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        });

        // open add-pokemon screen, pass username
        btnAddPokemon.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPokemonActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}

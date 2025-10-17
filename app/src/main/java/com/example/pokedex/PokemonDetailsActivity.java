package com.example.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

public class PokemonDetailsActivity extends AppCompatActivity {

    private ImageView imgPokemon;
    private TextView tvName, tvNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        imgPokemon = findViewById(R.id.imgPokemon);
        tvName = findViewById(R.id.tvName);
        tvNumber = findViewById(R.id.tvNumber);

        // Get Pokémon index passed from MainActivity
        int index = getIntent().getIntExtra("pokemon_index", 1);

        // Show the Pokémon number
        tvNumber.setText(String.format("#%03d", index));

        // (Optional) Set a fake name — later you could use an API or local list
        tvName.setText(getPokemonName(index));

        // Load Pokémon image in background thread
        new Thread(() -> {
            try {
                String formatted = String.format("%03d", index);
                String url = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + formatted + ".png";
                InputStream is = new URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                imgPokemon.post(() -> imgPokemon.setImageBitmap(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Temporary method to simulate Pokémon names
    private String getPokemonName(int index) {
        switch (index) {
            case 1: return "Bulbasaur";
            case 2: return "Ivysaur";
            case 3: return "Venusaur";
            case 4: return "Charmander";
            case 5: return "Charmeleon";
            case 6: return "Charizard";
            default: return "Pokémon #" + index;
        }
    }
}

package com.example.pokedex;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PokedexActivity extends AppCompatActivity {

    LinearLayout lnlPokedex;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        lnlPokedex = findViewById(R.id.lnlPokedex);
        username = getIntent().getStringExtra("username");

        loadUserPokemon();
    }

    private void loadUserPokemon() {
        List<PokemonEntity> list = AppDatabase
                .getInstance(this)
                .pokemonDao()
                .getPokemonsByUser(username);

        lnlPokedex.removeAllViews();

        if (list == null || list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Seu Pokédex está vazio!\nAdicione um Pokémon.");
            tv.setTextSize(18);
            tv.setPadding(40, 60, 40, 60);
            lnlPokedex.addView(tv);
            return;
        }

        for (PokemonEntity p : list) {
            TextView item = new TextView(this);
            item.setText("#" + p.number + " - " + p.name + " (" + p.type + ")");
            item.setTextSize(18);
            item.setPadding(30, 30, 30, 30);
            lnlPokedex.addView(item);
        }
    }
}

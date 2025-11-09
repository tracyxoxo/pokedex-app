package com.example.pokedex;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PokemonDetailsActivity extends AppCompatActivity {

    private ImageView imgPokemon;
    private TextView tvName, tvNumber;
    private ImageButton btnBack;
    private ChipGroup chipTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        imgPokemon = findViewById(R.id.imgPokemon);
        tvName = findViewById(R.id.tvName);
        tvNumber = findViewById(R.id.tvNumber);
        btnBack = findViewById(R.id.btnBack);
        chipTypes = findViewById(R.id.chipTypes);

        int index = getIntent().getIntExtra("pokemon_index", 1);
        tvNumber.setText(String.format("#%03d", index));

        btnBack.setOnClickListener(v -> finish());

        loadPokemonDetails(index);
    }

    private void loadPokemonDetails(int id) {
        new Thread(() -> {
            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + id);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line; while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject root = new JSONObject(sb.toString());

                String name = capitalize(root.getString("name"));

                // types
                JSONArray types = root.getJSONArray("types");
                String[] typeNames = new String[types.length()];
                for (int i = 0; i < types.length(); i++) {
                    typeNames[i] = capitalize(types.getJSONObject(i).getJSONObject("type").getString("name"));
                }

                String artUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                                + id + ".png";

                runOnUiThread(() -> {
                    tvName.setText(name);
                    Glide.with(this).load(artUrl).into(imgPokemon);

                    chipTypes.removeAllViews();
                    for (String type : typeNames) {
                        Chip chip = new Chip(this);
                        chip.setText(type);
                        chip.setTextColor(0xFFFFFFFF);
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getTypeColor(type)));
                        chipTypes.addView(chip);
                    }

                    // theme color based on first type
                    int primaryColor = getTypeColor(typeNames[0]);
                    findViewById(R.id.headerBar)
                            .setBackgroundTintList(ColorStateList.valueOf(primaryColor));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    private int getTypeColor(String type) {
        switch (type.toLowerCase()) {
            case "grass": return 0xFF78C850;
            case "fire": return 0xFFF08030;
            case "water": return 0xFF6890F0;
            case "electric": return 0xFFF8D030;
            case "psychic": return 0xFFF85888;
            case "ice": return 0xFF98D8D8;
            case "dragon": return 0xFF7038F8;
            case "dark": return 0xFF705848;
            case "fairy": return 0xFFEE99AC;
            case "fighting": return 0xFFC03028;
            case "rock": return 0xFFB8A038;
            case "ground": return 0xFFE0C068;
            case "bug": return 0xFFA8B820;
            case "ghost": return 0xFF705898;
            case "poison": return 0xFFA040A0;
            case "steel": return 0xFFB8B8D0;
            case "flying": return 0xFFA890F0;
            case "normal":
            default: return 0xFFA8A878;
        }
    }
}

package com.example.pokedex;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddPokemonActivity extends AppCompatActivity {

    private RecyclerView rv;
    private String username;
    private final ArrayList<PokemonListItem> items = new ArrayList<>();
    private PokemonGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Escolha um Pokémon");
        }

        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Usuário não recebido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rv = findViewById(R.id.rvPokemonGrid);
        rv.setLayoutManager(new GridLayoutManager(this, 3));


        adapter = new PokemonGridAdapter(items,
                // onImageClick → open details
                (id, name) -> {
                    // Open details page you already have
                    startActivity(
                            new android.content.Intent(this, PokemonDetailsActivity.class)
                                    .putExtra("pokemon_index", id)
                    );
                },
                // onAddClick → save to Room
                (id, name) -> {
                    try {
                        PokemonEntity p = new PokemonEntity();
                        p.username = username;
                        p.number = id;
                        p.name = capitalize(name);
                        p.type = ""; // (optional) fetch types later
                        AppDatabase.getInstance(this).pokemonDao().insertPokemon(p);
                        Toast.makeText(this, p.name + " adicionado!", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(this, "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        rv.setAdapter(adapter);

        // Load first 151 from PokéAPI
        loadPokemonList(151);
    }

    private void loadPokemonList(int limit) {
        new Thread(() -> {
            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon?limit=" + limit + "&offset=0");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray results = root.getJSONArray("results");
                ArrayList<PokemonListItem> temp = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String name = obj.getString("name"); // e.g., "bulbasaur"
                    // Derive id from list index (offset 0 => id = i+1)
                    int id = i + 1;

                    String padded = String.format("%03d", id);
                    String imgUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + padded + ".png";

                    temp.add(new PokemonListItem(id, name, imgUrl));
                }

                runOnUiThread(() -> {
                    items.clear();
                    items.addAll(temp);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                Log.e("AddPokemonActivity", "loadPokemonList: ", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro ao carregar lista: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

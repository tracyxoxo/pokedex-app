package com.example.pokedex;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PokedexActivity extends AppCompatActivity {

    RecyclerView rv;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        rv = findViewById(R.id.rvUserPokedex);
        rv.setLayoutManager(new LinearLayoutManager(this));

        username = getIntent().getStringExtra("username");
        loadUserPokemon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserPokemon(); // refresh when returning from Add
    }

    private void loadUserPokemon() {
        List<PokemonEntity> list = AppDatabase
                .getInstance(this)
                .pokemonDao()
                .getPokemonsByUser(username);

        if (list == null || list.isEmpty()) {
            Toast.makeText(this, "Seu Pokédex está vazio! Adicione um Pokémon.", Toast.LENGTH_SHORT).show();
            rv.setAdapter(new UserPokemonAdapter(this, java.util.Collections.emptyList()));
            return;
        }

        rv.setAdapter(new UserPokemonAdapter(this, list));
    }
}

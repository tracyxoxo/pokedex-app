package com.example.pokedex;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PokemonDao {

    @Insert
    void insertPokemon(PokemonEntity pokemon);

    @Query("SELECT * FROM pokemons WHERE username = :username")
    List<PokemonEntity> getPokemonsByUser(String username);
}

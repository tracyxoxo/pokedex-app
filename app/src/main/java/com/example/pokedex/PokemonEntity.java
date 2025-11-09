package com.example.pokedex;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pokemons")
public class PokemonEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username; // owner of this pokemon
    public String name;
    public int number;
    public String type;
}
package com.example.pokedex;

public class PokemonListItem {
    public final int id;
    public final String name;
    public final String imageUrl;

    public PokemonListItem(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}

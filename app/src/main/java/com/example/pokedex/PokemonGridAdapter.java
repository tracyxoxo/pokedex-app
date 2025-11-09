package com.example.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PokemonGridAdapter extends RecyclerView.Adapter<PokemonGridAdapter.Holder> {

    public interface OnImageClick {
        void onClick(int id, String name);
    }
    public interface OnAddClick {
        void onClick(int id, String name);
    }

    private final List<PokemonListItem> data;
    private final OnImageClick imageClick;
    private final OnAddClick addClick;

    public PokemonGridAdapter(List<PokemonListItem> data, OnImageClick imageClick, OnAddClick addClick) {
        this.data = data;
        this.imageClick = imageClick;
        this.addClick = addClick;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon_grid, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        PokemonListItem item = data.get(position);

        Glide.with(h.img.getContext())
                .load(item.imageUrl)
                .into(h.img);

        h.tvName.setText(capitalize(item.name));

        // Click image → details
        h.img.setOnClickListener(v -> imageClick.onClick(item.id, item.name));

        // Add button → save to DB
        h.btnAdd.setOnClickListener(v -> addClick.onClick(item.id, item.name));
    }

    @Override public int getItemCount() { return data.size(); }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) return "";
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        MaterialButton btnAdd;
        TextView tvName;
        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgPokemon);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvName = itemView.findViewById(R.id.tvPokemonName);
        }
    }
}

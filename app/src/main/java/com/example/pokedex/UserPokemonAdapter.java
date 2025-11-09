package com.example.pokedex;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserPokemonAdapter extends RecyclerView.Adapter<UserPokemonAdapter.Holder> {

    private final Context ctx;
    private final List<PokemonEntity> data;

    public UserPokemonAdapter(Context ctx, List<PokemonEntity> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_user_pokemon, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        PokemonEntity p = data.get(position);

        // Title (#NNN Name)
        h.tvTitle.setText(String.format("#%03d %s", p.number, capitalize(p.name)));

        // Image (official artwork)
        String art = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + p.number + ".png";
        Glide.with(h.img.getContext()).load(art).into(h.img);

        // Types chips: if DB has none, fetch and update
        h.chips.removeAllViews();
        if (p.type != null && !p.type.trim().isEmpty()) {
            for (String t : p.type.split(",")) {
                h.chips.addView(makeChip(t.trim(), h.chips));
            }
        } else {
            // fetch types and save in DB for next time
            fetchTypesAndUpdate(p, h);
        }

        // Open details on click
        h.itemView.setOnClickListener(v -> {
            ctx.startActivity(new Intent(ctx, PokemonDetailsActivity.class)
                    .putExtra("pokemon_index", p.number));
        });
    }

    @Override public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTitle;
        ChipGroup chips;
        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            chips = itemView.findViewById(R.id.chips);
        }
    }

    // ---- helpers ----
    private void fetchTypesAndUpdate(PokemonEntity p, Holder h) {
        new Thread(() -> {
            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + p.number);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder(); String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray types = root.getJSONArray("types");
                StringBuilder joined = new StringBuilder();
                for (int i = 0; i < types.length(); i++) {
                    String t = types.getJSONObject(i).getJSONObject("type").getString("name");
                    if (i > 0) joined.append(",");
                    joined.append(capitalize(t));
                }
                String typesStr = joined.toString();

                // Update DB field (persist)
                p.type = typesStr;
                AppDatabase.getInstance(ctx).pokemonDao().insertPokemon(p); // simple upsert alternative; ideally add @Update

                // Update UI
                ((AppCompatActivity) ctx).runOnUiThread(() -> {
                    h.chips.removeAllViews();
                    for (String t : typesStr.split(",")) {
                        h.chips.addView(makeChip(t.trim(), h.chips));
                    }
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private Chip makeChip(String typeName, ChipGroup parent) {
        Chip chip = new Chip(parent.getContext(), null, com.google.android.material.R.style.Widget_Material3_Chip_Assist_Elevated);
        chip.setText(typeName);
        chip.setTextColor(0xFFFFFFFF);
        int color = getTypeColor(typeName);
        chip.setChipBackgroundColor(ColorStateList.valueOf(color));
        chip.setCheckable(false);
        return chip;
    }

    private static String capitalize(String s) {
        return (s == null || s.isEmpty()) ? s : s.substring(0,1).toUpperCase() + s.substring(1);
    }

    private int getTypeColor(String t) {
        switch (t.toLowerCase()) {
            case "grass": return 0xFF78C850;
            case "fire": return 0xFFF08030;
            case "water": return 0xFF6890F0;
            case "electric": return 0xFFF8D030;
            case "ice": return 0xFF98D8D8;
            case "fighting": return 0xFFC03028;
            case "poison": return 0xFFA040A0;
            case "ground": return 0xFFE0C068;
            case "flying": return 0xFFA890F0;
            case "psychic": return 0xFFF85888;
            case "bug": return 0xFFA8B820;
            case "rock": return 0xFFB8A038;
            case "ghost": return 0xFF705898;
            case "dragon": return 0xFF7038F8;
            case "dark": return 0xFF705848;
            case "steel": return 0xFFB8B8D0;
            case "fairy": return 0xFFEE99AC;
            case "normal":
            default: return 0xFFA8A878;
        }
    }
}

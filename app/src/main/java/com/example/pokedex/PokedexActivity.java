package com.example.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.net.URL;
import android.content.Intent;

public class PokedexActivity extends AppCompatActivity {

    int posicao = 1;
    int qtde = 10;
    LinearLayout lnlPokedex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pokedex);

        lnlPokedex = findViewById(R.id.lnlPokedex);

        carregar();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void carregar(){
        lnlPokedex.removeAllViews();

        for (int i = posicao; i < posicao + qtde; i++) {
            carregarImagem(i);
        }
    }

    private void carregarImagem(int indice){
        ImageView img = new ImageView(getBaseContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // Example: generate correct URL for each Pokémon
                    String formattedIndex = String.format("%03d", indice);
                    InputStream is = new URL("https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + formattedIndex + ".png").openStream();
                    Bitmap b = BitmapFactory.decodeStream(is);

                    img.post(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(b);

                            // 👇 Add the click listener here
                            img.setOnClickListener(v -> {
                                Intent intent = new Intent(PokedexActivity.this, PokemonDetailsActivity.class);
                                intent.putExtra("pokemon_index", indice); // pass the Pokémon number
                                startActivity(intent);
                            });
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        lnlPokedex.addView(img);
    }

}
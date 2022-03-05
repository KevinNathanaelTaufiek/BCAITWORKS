package com.kevinnt.bcaitworks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.kevinnt.bcaitworks.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String ANIME_DETAIL = "anime_detail";
    private ActivityDetailBinding binding;
    private AnimeMovie animeMovie;
    private int favourited = -1, index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        animeMovie = getIntent().getParcelableExtra(ANIME_DETAIL);

        Picasso.with(this)
                .load(animeMovie.getImage())
                .into(binding.ivPosterDetail);

        binding.tvTitleDetail.setText(animeMovie.getTitle());
        binding.tvRatingDetail.setText(animeMovie.getAverageRating().toString());
        binding.tvEpsDetail.setText(binding.tvEpsDetail.getText().toString() + animeMovie.getEps());
        binding.tvDescDetail.setText(binding.tvDescDetail.getText().toString() + animeMovie.getDesc());

        Gson gson = new Gson();

        SharedPreferences sharedPref = getSharedPreferences("ITWORKSANIMELIST", Context.MODE_PRIVATE);
        String jsonList = sharedPref.getString("favourite_animes", "[]");
        AnimeMovie[] animes = gson.fromJson(jsonList, AnimeMovie[].class);
        List<AnimeMovie> animeMovieArrayList = new ArrayList<>(Arrays.asList(animes));

        for (AnimeMovie a : animeMovieArrayList) {
            if(animeMovie.getTitle().equals(a.getTitle())){
                favourited = index;
                binding.btnFavourite.setBackgroundColor(Color.parseColor("#E91E63"));
                binding.btnFavourite.setText("Remove from Favourite");
                break;
            }
            index++;
        }



        binding.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("fav", jsonList);
//                for (AnimeMovie a : animeMovieArrayList){
//                    Log.d("saved_favourite", a.toString());
//                }

                if(favourited!=-1){
                    animeMovieArrayList.remove(index);
                }
                else{
                    animeMovieArrayList.add(animeMovie);
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("favourite_animes", gson.toJson(animeMovieArrayList));
                editor.apply();

                Intent intent = new Intent(DetailActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
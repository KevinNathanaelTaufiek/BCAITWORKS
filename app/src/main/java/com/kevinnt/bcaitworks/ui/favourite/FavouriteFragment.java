package com.kevinnt.bcaitworks.ui.favourite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.kevinnt.bcaitworks.AnimeListAdapter;
import com.kevinnt.bcaitworks.AnimeMovie;
import com.kevinnt.bcaitworks.DetailActivity;
import com.kevinnt.bcaitworks.R;
import com.kevinnt.bcaitworks.databinding.FragmentFavouriteBinding;
import com.kevinnt.bcaitworks.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteFragment extends Fragment implements AnimeListAdapter.ListItemClickCallback{

//    private FavouriteViewModel favouriteViewModel;

    private FragmentFavouriteBinding binding;
    private ArrayList<AnimeMovie> animeList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        favouriteViewModel =
//                new ViewModelProvider(this).get(FavouriteViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
//        final TextView textView = root.findViewById(R.id.text_favourite);
//        favouriteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Gson gson = new Gson();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("ITWORKSANIMELIST", Context.MODE_PRIVATE);
        String jsonList = sharedPref.getString("favourite_animes", "[]");
        AnimeMovie[] animes = gson.fromJson(jsonList, AnimeMovie[].class);
        animeList = new ArrayList<>(Arrays.asList(animes));

        if (jsonList.equals("[]")){
            binding.tvNofav.setVisibility(View.VISIBLE);
        }
        else{
            binding.tvNofav.setVisibility(View.GONE);
        }

//        Log.d("saved_favourite", jsonList);
//        for (AnimeMovie a : animeList){
//            Log.d("saved_favourite", a.toString());
//        }

        AnimeListAdapter adapter = new AnimeListAdapter(animeList, getContext(), this);
        binding.rvFavourite.setAdapter(adapter);
        binding.rvFavourite.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClick(AnimeMovie animeMovie, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.ANIME_DETAIL, animeMovie);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
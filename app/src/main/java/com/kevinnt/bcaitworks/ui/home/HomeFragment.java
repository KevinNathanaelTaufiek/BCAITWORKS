package com.kevinnt.bcaitworks.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinnt.bcaitworks.AnimeListAdapter;
import com.kevinnt.bcaitworks.AnimeMovie;
import com.kevinnt.bcaitworks.DetailActivity;
import com.kevinnt.bcaitworks.databinding.FragmentHomeBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements AnimeListAdapter.ListItemClickCallback {

//    private HomeViewModel homeViewModel;

    private FragmentHomeBinding binding;
    private ArrayList<AnimeMovie> animeList = new ArrayList<>();
    private boolean loading = false;
    private AnimeListAdapter adapter;
    private int offsetCount=20;
    private AsyncHttpClient client = new AsyncHttpClient();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        client.get("https://kitsu.io/api/edge/anime?page[limit]=20&sort=popularityRank", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject response = new JSONObject(new String(responseBody));

                    JSONArray data = response.getJSONArray("data");

                    for (int i=0; i < data.length(); i++) {
                        JSONObject anime = data.getJSONObject(i).getJSONObject("attributes");

                        AnimeMovie animeMovieItem = new AnimeMovie();

                        animeMovieItem.setTitle(anime.getString("canonicalTitle"));
                        animeMovieItem.setAverageRating(anime.getDouble("averageRating"));
                        animeMovieItem.setImage(anime.getJSONObject("posterImage").getString("medium"));
                        animeMovieItem.setEps(anime.getInt("episodeLength"));
                        animeMovieItem.setDesc(anime.getString("description"));

                        animeList.add(animeMovieItem);
                    }
                    Log.d("apiresponse", "success");
                    showDataAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("apiresponse", "fail");
                error.printStackTrace();
            }
        });

        return view;
    }

    private void showDataAdapter() {
        adapter = new AnimeListAdapter(animeList, getContext(), this);
        binding.rvAnimeList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvAnimeList.setLayoutManager(layoutManager);


        binding.progressBar.setVisibility(View.GONE);


        binding.rvAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalPosition = adapter.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
//                Log.d("test_scroll", "Last Position : "+ lastPosition);
//                Log.d("test_scroll", "Total Position : "+ totalPosition);
                if(totalPosition - 1 == lastPosition && !loading){
//                    Log.d("test_scroll", "Last Position : "+ lastPosition);
                    loading = true;
                    loadNextPagination(offsetCount);
                }

            }
        });
    }

    private void loadNextPagination(int pageOffset) {
        client.get("https://kitsu.io/api/edge/anime?page[limit]=20&page[offset]="+pageOffset+"&sort=popularityRank", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ArrayList<AnimeMovie> loadedAnime = new ArrayList<>();
                try {
                    JSONObject response = new JSONObject(new String(responseBody));

                    JSONArray data = response.getJSONArray("data");

                    for (int i=0; i < data.length(); i++) {
                        JSONObject anime = data.getJSONObject(i).getJSONObject("attributes");

                        AnimeMovie animeMovieItem = new AnimeMovie();

                        animeMovieItem.setTitle(anime.getString("canonicalTitle"));
                        animeMovieItem.setAverageRating(anime.getDouble("averageRating"));
                        animeMovieItem.setImage(anime.getJSONObject("posterImage").getString("medium"));
                        animeMovieItem.setEps(anime.getInt("episodeLength"));
                        animeMovieItem.setDesc(anime.getString("description"));

                        loadedAnime.add(animeMovieItem);
                    }
                    offsetCount+=20;

                    Log.d("apiresponse", "success");
                    adapter.addToAnimeList(loadedAnime);
                    loading = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("apiresponse", "fail");
                error.printStackTrace();
            }
        });
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
package com.kevinnt.bcaitworks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinnt.bcaitworks.databinding.AnimeListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AnimeListAdapter extends RecyclerView.Adapter<AnimeListAdapter.AnimeViewHolder> {

    private List<AnimeMovie> animeList;
    private Context context;
    private ListItemClickCallback listItemClickCallback;

    public AnimeListAdapter(List<AnimeMovie> animeList, Context context, ListItemClickCallback listItemClickCallback) {
        this.animeList = animeList;
        this.context = context;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(AnimeListItemBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        AnimeMovie anime = animeList.get(position);
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public interface ListItemClickCallback{
        void onItemClick(AnimeMovie animeMovie, Context context);
    }

    public void addToAnimeList(ArrayList<AnimeMovie> loadedAnime){
        animeList.addAll(loadedAnime);
        notifyDataSetChanged();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder {
        private AnimeListItemBinding binding;

        public AnimeViewHolder(@NonNull AnimeListItemBinding bind) {
            super(bind.getRoot());
            binding = bind;

        }

        public void bind(AnimeMovie anime){

            binding.tvTitle.setText(anime.getTitle());
            binding.tvRating.setText(anime.getAverageRating().toString());
            Picasso.with(context)
                    .load(anime.getImage())
                    .into(binding.ivImage);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemClickCallback.onItemClick(anime, context);
                }
            });
        }

    }
}

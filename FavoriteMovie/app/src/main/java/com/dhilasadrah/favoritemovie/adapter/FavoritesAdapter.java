package com.dhilasadrah.favoritemovie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhilasadrah.favoritemovie.MovieDetails;
import com.dhilasadrah.favoritemovie.R;
import com.dhilasadrah.favoritemovie.model.FavoriteItem;

import java.util.ArrayList;

import static com.dhilasadrah.favoritemovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;

public class FavoritesAdapter  extends RecyclerView.Adapter<FavoritesAdapter.MovieViewHolder> {

    private final Activity activity;
    private ArrayList<FavoriteItem> movieList = new ArrayList<>();

    public FavoritesAdapter(Activity activity) {
        this.activity = activity;
    }

    private ArrayList<FavoriteItem> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<FavoriteItem> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritesAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movies, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.MovieViewHolder movieViewHolder, int i) {
        FavoriteItem movies = getMovieList().get(i);
        movieViewHolder.tv_title.setText(movies.getTitle());
        movieViewHolder.tv_overview.setText(movies.getOverview());
        movieViewHolder.tv_releaseDate.setText(movies.getReleaseDate());
        movieViewHolder.rating.setRating((float) (movies.getVoteAverage() / 2));

        Glide.with(activity)
                .load("https://image.tmdb.org/t/p/w185/"+movieList.get(i).getPoster())
                .into(movieViewHolder.img_poster);
    }

    @Override
    public int getItemCount() {
        return getMovieList().size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView tv_title, tv_releaseDate, tv_overview;
        RatingBar rating;

        MovieViewHolder(View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.poster);
            tv_title = itemView.findViewById(R.id.title);
            tv_releaseDate = itemView.findViewById(R.id.releaseDate);
            tv_overview = itemView.findViewById(R.id.overview);
            rating = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(activity, MovieDetails.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + getMovieList().get(position).getMovieId());
                    intent.putExtra(MovieDetails.EXTRA_MOVIES, movieList.get(position));
                    intent.setData(uri);
                    activity.startActivity(intent);
                }
            });
        }
    }
}

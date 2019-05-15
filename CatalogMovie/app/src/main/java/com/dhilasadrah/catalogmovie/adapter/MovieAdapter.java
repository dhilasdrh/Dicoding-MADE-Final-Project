package com.dhilasadrah.catalogmovie.adapter;

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
import com.dhilasadrah.catalogmovie.activity.MovieDetailsActivity;
import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.model.Movies;

import java.util.ArrayList;

import static com.dhilasadrah.catalogmovie.activity.MovieDetailsActivity.EXTRA_MOVIES;
import static com.dhilasadrah.catalogmovie.activity.MovieDetailsActivity.EXTRA_POSITION;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Activity activity;
    private ArrayList<Movies> movieList = new ArrayList<>();

    public MovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Movies> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<Movies> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movies, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int i) {
        Movies movies = getMovieList().get(i);
        movieViewHolder.tv_title.setText(movies.getTitle());
        movieViewHolder.tv_overview.setText(movies.getOverview());
        movieViewHolder.tv_releaseDate.setText(movies.getReleaseDate());
        movieViewHolder.rating.setRating((float) (movies.getRating() / 2));

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
                    Intent intent = new Intent(activity, MovieDetailsActivity.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + getMovieList().get(position).getMovieId());
                    intent.setData(uri);
                    intent.putExtra(EXTRA_POSITION, position);
                    intent.putExtra(EXTRA_MOVIES, movieList.get(position));
                    activity.startActivityForResult(intent, MovieDetailsActivity.REQUEST_UPDATE);
                }
            });
        }
    }

}

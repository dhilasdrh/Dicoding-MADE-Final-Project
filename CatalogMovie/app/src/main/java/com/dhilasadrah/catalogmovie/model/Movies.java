package com.dhilasadrah.catalogmovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dhilasadrah.catalogmovie.database.DatabaseContract;

import org.json.JSONObject;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.getColumnDouble;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.getColumnInt;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.getColumnString;

public class Movies implements Parcelable {
    private int movieId;
    private String title, releaseDate, overview, poster, backdrop;
    private double rating;

    public Movies() {
    }

    public Movies(JSONObject object) {
        try {
            Integer movieId = object.getInt("id");
            String title = object.getString("title");
            String releaseDate = object.getString("release_date");
            String overview = object.getString("overview");
            String poster = object.getString("poster_path");
            String backdrop = object.getString("backdrop_path");
            double rating = object.getDouble("vote_average");

            this.movieId = movieId;
            this.poster = poster;
            this.backdrop = backdrop;
            this.title = title;
            this.releaseDate = releaseDate;
            this.overview = overview;
            this.rating = rating;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movies(Cursor cursor) {
        this.movieId = getColumnInt(cursor, DatabaseContract.MoviesColumns.MOVIE_ID);
        this.title = getColumnString(cursor, DatabaseContract.MoviesColumns.TITLE);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MoviesColumns.RELEASE_DATE);
        this.overview = getColumnString(cursor, DatabaseContract.MoviesColumns.OVERVIEW);
        this.poster = getColumnString(cursor, DatabaseContract.MoviesColumns.POSTER);
        this.backdrop = getColumnString(cursor, DatabaseContract.MoviesColumns.BACKDROP);
        this.rating = getColumnDouble(cursor, DatabaseContract.MoviesColumns.RATING);
    }

    public Movies(int movieId, String title, String releaseDate, String overview, String poster, String backdrop, double rating) {
        this.movieId = movieId;
        this.poster = poster;
        this.backdrop = backdrop;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeDouble(this.rating);
    }

    protected Movies(Parcel in) {
        this.movieId = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.rating = in.readDouble();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

}
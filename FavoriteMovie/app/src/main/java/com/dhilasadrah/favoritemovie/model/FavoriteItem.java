package com.dhilasadrah.favoritemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dhilasadrah.favoritemovie.database.DatabaseContract;

import static com.dhilasadrah.favoritemovie.database.DatabaseContract.getColumnDouble;
import static com.dhilasadrah.favoritemovie.database.DatabaseContract.getColumnInt;
import static com.dhilasadrah.favoritemovie.database.DatabaseContract.getColumnString;

public class FavoriteItem implements Parcelable {
    private int movieId, runtime;
    private String title, releaseDate, overview, poster, backdrop, genres;
    private double rating;

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public FavoriteItem(Cursor cursor){
        this.movieId = getColumnInt(cursor, DatabaseContract.MoviesColumns.MOVIE_ID);
        this.title = getColumnString(cursor, DatabaseContract.MoviesColumns.TITLE);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MoviesColumns.RELEASE_DATE);
        this.overview = getColumnString(cursor, DatabaseContract.MoviesColumns.OVERVIEW);
        this.poster = getColumnString(cursor, DatabaseContract.MoviesColumns.POSTER);
        this.backdrop = getColumnString(cursor, DatabaseContract.MoviesColumns.BACKDROP);
        this.rating = getColumnDouble(cursor, DatabaseContract.MoviesColumns.RATING);
    }

    public FavoriteItem(int movieId, String title, String releaseDate, String overview, String poster, String backdrop, double rating){
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

    public double getVoteAverage() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeInt(this.runtime);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeString(this.genres);
        dest.writeDouble(this.rating);
    }

    private FavoriteItem(Parcel in) {
        this.movieId = in.readInt();
        this.runtime = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.genres = in.readString();
        this.rating = in.readDouble();
    }

    public static final Creator<FavoriteItem> CREATOR = new Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel source) {
            return new FavoriteItem(source);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}

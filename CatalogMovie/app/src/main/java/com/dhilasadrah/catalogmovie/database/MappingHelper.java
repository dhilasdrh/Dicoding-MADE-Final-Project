package com.dhilasadrah.catalogmovie.database;

import android.database.Cursor;

import com.dhilasadrah.catalogmovie.model.Movies;

import java.util.ArrayList;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.BACKDROP;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.MOVIE_ID;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.OVERVIEW;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.POSTER;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.RATING;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.RELEASE_DATE;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Movies> mapCursorToArrayList(Cursor notesCursor) {

        ArrayList<Movies> movieList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int movieId = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(MOVIE_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POSTER));
            String backdrop = notesCursor.getString(notesCursor.getColumnIndexOrThrow(BACKDROP));
            double rating = notesCursor.getDouble(notesCursor.getColumnIndexOrThrow(RATING));
            movieList.add(new Movies(movieId, title, releaseDate, overview, poster, backdrop, rating));
        }
        return movieList;
    }
}

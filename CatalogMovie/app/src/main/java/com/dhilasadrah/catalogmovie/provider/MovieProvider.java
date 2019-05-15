package com.dhilasadrah.catalogmovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.dhilasadrah.catalogmovie.database.MovieHelper;
import com.dhilasadrah.catalogmovie.fragment.Favorite;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.AUTHORITY;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.TABLE_NAME;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);
        uriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                MOVIE_ID);
    }

    private MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        movieHelper.open();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        movieHelper.open();
        long added;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (getContext() != null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new Favorite.DataObserver(new Handler(), getContext()));
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        movieHelper.open();
        int updated;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (getContext() != null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new Favorite.DataObserver(new Handler(), getContext()));
        }

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        movieHelper.open();
        int deleted;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new Favorite.DataObserver(new Handler(), getContext()));

        }

        return deleted;
    }
}

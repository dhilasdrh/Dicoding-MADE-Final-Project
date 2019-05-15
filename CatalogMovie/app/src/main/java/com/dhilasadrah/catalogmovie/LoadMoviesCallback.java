package com.dhilasadrah.catalogmovie;

import android.database.Cursor;

public interface LoadMoviesCallback {
    void preExecute();

    void postExecute(Cursor movies);
}

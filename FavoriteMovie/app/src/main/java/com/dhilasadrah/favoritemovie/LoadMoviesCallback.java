package com.dhilasadrah.favoritemovie;

import android.database.Cursor;

public interface LoadMoviesCallback {

    void postExecute(Cursor notes);

}

package com.dhilasadrah.catalogmovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbmoviecatalog";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s REAL NOT NULL)",
            TABLE_NAME,
            MoviesColumns.MOVIE_ID,
            MoviesColumns.TITLE,
            MoviesColumns.RELEASE_DATE,
            MoviesColumns.OVERVIEW,
            MoviesColumns.POSTER,
            MoviesColumns.BACKDROP,
            MoviesColumns.RATING
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

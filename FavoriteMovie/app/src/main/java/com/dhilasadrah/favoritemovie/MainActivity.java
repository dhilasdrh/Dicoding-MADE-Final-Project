package com.dhilasadrah.favoritemovie;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.dhilasadrah.favoritemovie.adapter.FavoritesAdapter;
import com.dhilasadrah.favoritemovie.model.FavoriteItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.dhilasadrah.favoritemovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;
import static com.dhilasadrah.favoritemovie.helper.MappingHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements LoadMoviesCallback{

    private ProgressBar progressBar;
    private FavoritesAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);
        RecyclerView rvFavorites = findViewById(R.id.recyclerView);

        movieAdapter = new FavoritesAdapter(this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        rvFavorites.setHasFixedSize(true);
        rvFavorites.setAdapter(movieAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver observer = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, observer);
        new getData(this, this).execute();
    }

    @Override
    public void postExecute(Cursor notes) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<FavoriteItem> listNotes = mapCursorToArrayList(notes);
        if (listNotes.size() > 0) {
            movieAdapter.setMovieList(listNotes);
        } else {
            movieAdapter.setMovieList(new ArrayList<FavoriteItem>());
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private getData(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

       DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getData(getApplicationContext(), this).execute();
    }

}

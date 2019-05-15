package com.dhilasadrah.catalogmovie.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dhilasadrah.catalogmovie.LoadMoviesCallback;
import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.adapter.MovieAdapter;
import com.dhilasadrah.catalogmovie.model.Movies;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;
import static com.dhilasadrah.catalogmovie.database.MappingHelper.mapCursorToArrayList;

public class Favorite extends Fragment implements LoadMoviesCallback {

    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public Favorite() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        progressBar = view.findViewById(R.id.pbFavorites);
        RecyclerView rvFavorites = view.findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavorites.setHasFixedSize(true);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());
        if (getActivity() != null){
            getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        }

        movieAdapter = new MovieAdapter(getActivity());
        rvFavorites.setAdapter(movieAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(getContext(), this).execute();
        } else {
            ArrayList<Movies> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                movieAdapter.setMovieList(list);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, movieAdapter.getMovieList());
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(Cursor movies) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<Movies> listNotes = mapCursorToArrayList(movies);
        if (listNotes.size() > 0) {
            movieAdapter.setMovieList(listNotes);
        } else {
            movieAdapter.setMovieList(new ArrayList<Movies>());
        }
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMoviesAsync(getContext(), this).execute();
    }
}

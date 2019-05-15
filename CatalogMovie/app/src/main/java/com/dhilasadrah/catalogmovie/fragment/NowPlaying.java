package com.dhilasadrah.catalogmovie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dhilasadrah.catalogmovie.BuildConfig;
import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.adapter.MovieAdapter;
import com.dhilasadrah.catalogmovie.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NowPlaying extends Fragment {

    private RecyclerView rvNowPlaying;
    private ProgressBar progressBar;
    private Button btnRetry;
    private MovieAdapter movieAdapter;
    private ArrayList<Movies> movieList;

    public NowPlaying() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        btnRetry = view.findViewById(R.id.retryNowPlaying);
        progressBar = view.findViewById(R.id.pbNowPlaying);
        rvNowPlaying = view.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();

        return view;
    }

    public void loadData() {
        String url = BuildConfig.BASE_URL + "movie/now_playing?api_key=" + BuildConfig.API_KEY + "&language=en-US";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray jsonArray = responseObject.getJSONArray("results");
                    movieList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Movies movies = new Movies(object);
                        movieList.add(movies);
                    }
                    movieAdapter = new MovieAdapter(getActivity());
                    movieAdapter.setMovieList(movieList);
                    rvNowPlaying.setAdapter(movieAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getFragmentManager() != null) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(NowPlaying.this).attach(NowPlaying.this).commit();
                        }
                    }
                });
            }
        });
    }
}
package com.dhilasadrah.catalogmovie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button button;

    private MovieAdapter movieAdapter;
    private ArrayList<Movies> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.pbSearch);
        button = findViewById(R.id.retrySearch);
        searchView = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rvSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                searchMovie(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void searchMovie(final String query) {
        String url = BuildConfig.BASE_URL + "search/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US&query=" + query;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
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
                    movieAdapter = new MovieAdapter(SearchActivity.this);
                    if (movieList.size() == 0) {
                        Toast.makeText(SearchActivity.this, R.string.no_result, Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        movieAdapter.setMovieList(movieList);
                        recyclerView.setAdapter(movieAdapter);
                        searchView.onActionViewCollapsed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
        });
    }
}
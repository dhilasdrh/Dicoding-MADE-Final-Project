package com.dhilasadrah.catalogmovie.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dhilasadrah.catalogmovie.BuildConfig;
import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.BACKDROP;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.CONTENT_URI;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.MOVIE_ID;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.OVERVIEW;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.POSTER;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.RATING;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.RELEASE_DATE;
import static com.dhilasadrah.catalogmovie.database.DatabaseContract.MoviesColumns.TITLE;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView detailBackdrop, detailPoster;
    TextView detailTitle, detailReleaseDate, detailRuntime, detailGenres, detailOverview;
    RatingBar detailRating;
    ProgressBar progressBar;
    Button btnRetry;
    ScrollView scrollView;

    private Movies movies;

    public static final String EXTRA_MOVIES = "extra_movies";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_UPDATE = 200;

    int position;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRetry = findViewById(R.id.retry);
        progressBar = findViewById(R.id.pbDetails);
        scrollView = findViewById(R.id.scrollView);
        detailBackdrop = findViewById(R.id.detailBackdrop);
        detailPoster = findViewById(R.id.detailPoster);
        detailTitle = findViewById(R.id.detailTitle);
        detailReleaseDate = findViewById(R.id.detailReleaseDate);
        detailRuntime = findViewById(R.id.detailRuntime);
        detailGenres = findViewById(R.id.detailGenres);
        detailOverview = findViewById(R.id.detailOverview);
        detailRating = findViewById(R.id.detailRating);

        movies = getIntent().getParcelableExtra(EXTRA_MOVIES);
        int movieId = movies.getMovieId();

        getMovieDetails(movieId);

        if (movies != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isFavorite = false;
        }else{
            movies = new Movies();
        }

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movies = new Movies(cursor);
                cursor.close();
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(movies.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getMovieDetails(int movieId) {
        String url = BuildConfig.BASE_URL+"movie/"+movieId+"?api_key="+BuildConfig.API_KEY+"&language=en-US";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                try {
                    String response = new String(responseBody);
                    JSONObject object = new JSONObject(response);
                    Movies movies = new Movies(object);

                    JSONArray jsonArray = object.getJSONArray("genres");
                    List<String> genreList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String genreName = jsonObject.getString("name");
                        genreList.add(genreName);
                    }
                    String genres = TextUtils.join(", ", genreList);
                    String overview = object.getString("overview");
                    String runtime = String.valueOf(object.getInt("runtime"));
                    String duration = String.format(getString(R.string.minutes), runtime);

                    detailTitle.setText(movies.getTitle());
                    detailReleaseDate.setText(movies.getReleaseDate());
                    detailRuntime.setText(duration);
                    detailGenres.setText(genres);
                    detailOverview.setText(overview);
                    detailRating.setRating((float) (movies.getRating()/2));

                    Glide.with(getApplicationContext())
                            .load(BuildConfig.BASE_IMG_URL + "w185/" + movies.getPoster())
                            .into(detailPoster);

                    Glide.with(getApplicationContext())
                            .load(BuildConfig.BASE_IMG_URL + "w500/" + movies.getBackdrop())
                            .into(detailBackdrop);

                } catch (JSONException e) {
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
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        invalidateOptionsMenu();

        if (item.getItemId() == R.id.favorite){
            if (!isFavorite) {
                isFavorite = true;
                item.setIcon(R.drawable.fav_icon);
                Toast.makeText(MovieDetailsActivity.this, R.string.add_favorite, Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = false;
                item.setIcon(R.drawable.unfav_icon);
                Toast.makeText(MovieDetailsActivity.this, R.string.delete_favorite, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIES, movies);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();

            values.put(MOVIE_ID, movies.getMovieId());
            values.put(TITLE, movies.getTitle());
            values.put(RELEASE_DATE, movies.getReleaseDate());
            values.put(OVERVIEW, movies.getOverview());
            values.put(POSTER, movies.getPoster());
            values.put(BACKDROP, movies.getBackdrop());
            values.put(RATING, movies.getRating());

            if (isFavorite){
                getContentResolver().insert(CONTENT_URI, values);
            } else{
                intent.putExtra(EXTRA_POSITION, position);
                if (getIntent().getData() != null){
                    getContentResolver().delete(getIntent().getData(), null, null);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       int movieId = movies.getMovieId();

        if (isFavorite(movieId)){
            isFavorite = true;
            menu.getItem(0).setIcon(R.drawable.fav_icon);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean isFavorite(int id){
        Uri uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        @SuppressLint("Recycle")
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);

        assert cursor != null;
        return cursor.moveToFirst();
    }

}


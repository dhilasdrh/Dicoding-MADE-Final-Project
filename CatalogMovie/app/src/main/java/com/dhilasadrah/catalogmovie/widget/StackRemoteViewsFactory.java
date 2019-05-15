package com.dhilasadrah.catalogmovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhilasadrah.catalogmovie.BuildConfig;
import com.dhilasadrah.catalogmovie.R;

import com.dhilasadrah.catalogmovie.database.DatabaseHelper;
import com.dhilasadrah.catalogmovie.model.Movies;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.dhilasadrah.catalogmovie.database.MovieHelper.DATABASE_TABLE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory  {

    private ArrayList<Movies> movieList = new ArrayList<>();
    private Context context;
    private int widgetId;

    StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
       movieList.clear();
       getFavoriteMovies(context);
    }

    private void getFavoriteMovies(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                Movies movies = new Movies(cursor);
                movieList.add(movies);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Movies movies = movieList.get(position);
        String posterUrl = BuildConfig.BASE_IMG_URL + "w185/" + movies.getPoster();
        try {
            Bitmap preview = Glide.with(context)
                    .asBitmap()
                    .load(posterUrl)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.imageView, preview);
            remoteViews.setTextViewText(R.id.txt_title, movies.getTitle());
            remoteViews.setTextViewText(R.id.txt_overview, movies.getOverview());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Bundle extras= new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.layout_widget, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
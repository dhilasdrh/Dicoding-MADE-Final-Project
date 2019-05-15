package com.dhilasadrah.catalogmovie.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.dhilasadrah.catalogmovie.R;

public class MovieWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "com.dhilasadrah.catalogmovie.EXTRA_ITEM";
    public static final String UPDATE_ACTION = "com.dhilasadrah.catalogmovie.UPDATE_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent updateIntent = new Intent(context, MovieWidget.class);
        updateIntent.setAction(MovieWidget.UPDATE_ACTION);
        updateIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context, 400, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, updatePendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(UPDATE_ACTION)){
                ComponentName thisWidget = new ComponentName(context, MovieWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

                /*
                  int widgetIDs[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MovieWidget.class));
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetIDs, R.id.stack_view);
                 */
            }
        }
        super.onReceive(context, intent);
    }
}


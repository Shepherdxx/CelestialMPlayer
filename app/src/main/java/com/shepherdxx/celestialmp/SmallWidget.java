package com.shepherdxx.celestialmp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.shepherdxx.celestialmp.extras.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class SmallWidget extends AppWidgetProvider {

    private static boolean sEnabled;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

//        Song song = null;
        String songName= context.getResources().getString(R.string.appwidget_text);
        int state = Constants.MP_EMPTY;

        if (MP_BG_Service.hasInstance()) {
            MP_BG_Service service = MP_BG_Service.get(context);
            state = service.MPState;
            songName = service.SongTitle;
            Log.i("SmallWidget", songName + " " + state);
        }

        sEnabled = true;
//        updateWidget(context, manager, song, state);
        updateAppWidget(context, appWidgetManager,songName, state);
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager,songName, state, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        sEnabled = true;
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        sEnabled = false;
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,String songName,int state
//            ,int appWidgetId
    ) {

        if (!sEnabled)
            return;


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.small_widget);
        views.setTextViewText(R.id.sw_text, songName);

        views.setImageViewResource(R.id.sw_pausePlay,state== Constants.MP_PLAY?
                    R.drawable.ic_pause_circle_outline_black_48dp :
                    R.drawable.ic_play_circle_outline_black_48dp);


        Intent intent;
        PendingIntent pendingIntent;

        intent = PreService.getIntent(context, MP_BG_Service.ACTION_TOGGLE_PLAYBACK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.sw_pausePlay, pendingIntent);

        intent = PreService.getIntent(context, MP_BG_Service.ACTION_NEXT_SONG);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.sw_next_song, pendingIntent);

        intent = PreService.getIntent(context, MP_BG_Service.ACTION_PREV_SONG);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.sw_back_song, pendingIntent);

        intent = PreService.getIntent(context, MP_BG_Service.ACTION_PLAY);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.sw_text, pendingIntent);


        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(new ComponentName(context, SmallWidget.class), views);
    }

    public static void checkEnabled(Context context,
                                    AppWidgetManager manager) {
        sEnabled = manager.getAppWidgetIds(new ComponentName(context, SmallWidget.class)).length != 0;
    }


}


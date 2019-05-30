package com.example.lab4;

import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SuperWidgetConfigureActivity SuperWidgetConfigureActivity}
 */
public class SuperWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = SuperWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.super_widget);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate;
        try
        {
            strDate = sdf.parse(widgetText.toString());
            if (System.currentTimeMillis() > strDate.getTime())
            {
                widgetText = "Событие уже наступило!";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("Уведомление!")
                        .setContentText("Событие настало!")
                        .setTicker("Внимание!")
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1234, builder.build());

            }
            else
                {
                // +
                long difference = strDate.getTime() - System.currentTimeMillis();
                long differenceDays = ( difference / (1000 * 60 * 60 * 24) ) + 1;
                widgetText = "Количество дней до события: " + differenceDays;
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent configIntent = new Intent(context, SuperWidgetConfigureActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId,
                configIntent, 0);
        views.setOnClickPendingIntent(R.id.mainLayer, pIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SuperWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


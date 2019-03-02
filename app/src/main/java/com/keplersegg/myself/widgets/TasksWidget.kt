package com.keplersegg.myself.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.keplersegg.myself.R

import com.keplersegg.myself.activities.MainActivity


class TasksWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val views = RemoteViews(context.packageName, R.layout.tasks_widget)
            views.setTextViewText(R.id.lblLabel, "Coffee")
            views.setOnClickPendingIntent(R.id.imgMinus,
                    getPendingIntent(context, -1))
            views.setOnClickPendingIntent(R.id.imgPlus,
                    getPendingIntent(context, 1))
/*
            val intent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
            intent.putExtra("taskId", 123)
            intent.putExtra("value", -1)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.imgPlus, pendingIntent)
*/
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        internal fun getPendingIntent(context: Context, value: Int): PendingIntent {

            val intent = Intent(context, MainActivity::class.java)

            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra("value", value)

            return PendingIntent.getActivity(context, value, intent, 0)
        }
    }
}


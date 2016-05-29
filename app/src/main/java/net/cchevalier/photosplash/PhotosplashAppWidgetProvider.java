package net.cchevalier.photosplash;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.data.PhotosplashContract;

import java.util.Random;

/**
 * PhotosplashAppWidgetProvider
 *
 * Created by cch on 28/05/2016.
 */
public class PhotosplashAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "Photosplash-Widget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int nWidgets = appWidgetIds.length;
        Log.d(TAG, "onUpdate: Starting for " + nWidgets + " widgets.");

        // Define the columns to retrieve
        String[] projectionFields = new String[] {
                PhotosplashContract.Favorites._ID,
                PhotosplashContract.Favorites.COLUMN_PHOTO_ID,
                PhotosplashContract.Favorites.COLUMN_HEIGHT,
                PhotosplashContract.Favorites.COLUMN_WIDTH,
                PhotosplashContract.Favorites.COLUMN_URLS_SMALL
        };

        Cursor cursor = context.getContentResolver().query(
                PhotosplashContract.Favorites.CONTENT_URI, // URI
                projectionFields, // projection fields
                null, // the selection criteria
                null, // the selection args
                null // the sort order
        );

        if (cursor == null ) {
            Log.d(TAG, "onUpdate: cursor null");
            return;
        }

        if (cursor.getCount() == 0) {
            Log.d(TAG, "onUpdate: cursor empty");
            cursor.close();
            return;
        }

        int nFavorites = cursor.getCount();

        for (int i=0; i < nWidgets; i++) {

            int appWidgetId = appWidgetIds[i];

            int random = new Random().nextInt(nFavorites);
            cursor.moveToPosition(random);
            Log.d(TAG, "onUpdate: " + i + " " + cursor.getString(1));
            String urlSmall = cursor.getString(4);

            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.photosplash_appwidget);

            Picasso.with(context)
                    .load(urlSmall)
                    .into(remoteView, R.id.iv_widget_photo, new int[]{appWidgetId}  ) ;
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }

        cursor.close();

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d(TAG, "onAppWidgetOptionsChanged: ");
        int[] appWidgetIds = {appWidgetId};
        onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted: ");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled: ");
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled: ");
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        Log.d(TAG, "onRestored: ");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}

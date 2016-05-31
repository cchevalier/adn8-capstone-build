package net.cchevalier.photosplash.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.activities.view.ViewActivity;
import net.cchevalier.photosplash.data.PhotosplashContract;
import net.cchevalier.photosplash.models.Photo;

import java.util.Random;

/**
 * FavoritesWidgetProvider
 *
 * Created by cch on 28/05/2016.
 */
public class FavoritesWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "Photosplash-Widget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int nWidgets = appWidgetIds.length;
        Log.d(TAG, "onUpdate: Starting for " + nWidgets + " widgets.");

        // Define the columns to retrieve
        String[] projectionFields = new String[] {
                PhotosplashContract.Favorites._ID, // 0
                PhotosplashContract.Favorites.COLUMN_PHOTO_ID, // 1
                PhotosplashContract.Favorites.COLUMN_WIDTH,
                PhotosplashContract.Favorites.COLUMN_HEIGHT,
                PhotosplashContract.Favorites.COLUMN_COLOR,
                PhotosplashContract.Favorites.COLUMN_USER_ID,
                PhotosplashContract.Favorites.COLUMN_USER_NAME,
                PhotosplashContract.Favorites.COLUMN_URLS_FULL,
                PhotosplashContract.Favorites.COLUMN_URLS_REGULAR,
                PhotosplashContract.Favorites.COLUMN_URLS_SMALL //9
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

            // cursor to photo
            String photo_id = cursor.getString(1);
            int width = cursor.getInt(2);
            int height = cursor.getInt(3);
            String color = cursor.getString(4);
            String user_id = cursor.getString(5);
            String user_name = cursor.getString(6);
            String url_full = cursor.getString(7);
            String url_regular = cursor.getString(8);
            String url_small = cursor.getString(9);
            Photo photo = new Photo(photo_id, width, height, color,
                    user_id, user_name,
                    url_full, url_regular, url_small);

            Log.d(TAG, "onUpdate: " + i + " with " + photo.id);

            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.favorites_widget);

            Picasso.with(context)
                    .load(photo.urls.small)
                    .into(remoteView, R.id.iv_widget_photo, new int[]{appWidgetId});


            //
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("currentPhoto", photo);

            // see http://stackoverflow.com/questions/3730258/mulitple-instances-of-pending-intent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.iv_widget_photo , pendingIntent);

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

package net.cchevalier.photosplash;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.data.PhotosplashContract;
import net.cchevalier.photosplash.models.Photo;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewActivity extends AppCompatActivity {

    private final String TAG = "PhotoSplash";

    private Tracker mTracker;

    private Photo currentPhoto;
    private boolean isFavorite;

    private FloatingActionButton fabFavorite;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);

        // Obtain the shared Tracker instance.
        PhotosplashApplication application = (PhotosplashApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Incoming intent
        currentPhoto = getIntent().getParcelableExtra("currentPhoto");
        isFavorite = isFavorite(currentPhoto);
        Log.d(TAG, "onCreate: " + currentPhoto.toString());

        // PhotoView handling
        PhotoView photoView  = (PhotoView) findViewById(R.id.iv_photo);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this)
                .load(currentPhoto.urls.regular)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }

                    @Override
                    public void onError() {
                    }
                });


        // Favorite FAB
        fabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);
        updateFavoriteFAB(isFavorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateFavoriteStatus task = new UpdateFavoriteStatus();
                task.execute();
            }
        });

        // Info
        //
        // Sample Info text
        TextView photoInfo = (TextView) findViewById(R.id.tv_photo_info);
        photoInfo.setText(currentPhoto.toString());
        //
        // Bottom Sheet
        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //
        // Info FAB
        FloatingActionButton fabInfo = (FloatingActionButton) findViewById(R.id.fab_info);
        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Photo")
                .setAction("View")
                .build());
    }


    private class UpdateFavoriteStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean currentStatus = isFavorite;
            Boolean newStatus = currentStatus;

            if (currentStatus) {

                getContentResolver().delete(
                        PhotosplashContract.Favorites.CONTENT_URI,
                        PhotosplashContract.Favorites.COLUMN_PHOTO_ID + " = '" + currentPhoto.id + "'",
                        null
                );

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Favorite")
                        .setAction("Remove")
                        .build());

                newStatus = false;
            } else {
                ContentValues values = new ContentValues();
                values.put(PhotosplashContract.Favorites.COLUMN_PHOTO_ID, currentPhoto.id);
                values.put(PhotosplashContract.Favorites.COLUMN_WIDTH, currentPhoto.width);
                values.put(PhotosplashContract.Favorites.COLUMN_HEIGHT, currentPhoto.height);
                values.put(PhotosplashContract.Favorites.COLUMN_COLOR, currentPhoto.color);
                values.put(PhotosplashContract.Favorites.COLUMN_USER_ID, currentPhoto.user.id);
                values.put(PhotosplashContract.Favorites.COLUMN_USER_NAME, currentPhoto.user.name);
                values.put(PhotosplashContract.Favorites.COLUMN_URLS_FULL, currentPhoto.urls.full);
                values.put(PhotosplashContract.Favorites.COLUMN_URLS_REGULAR, currentPhoto.urls.regular);
                values.put(PhotosplashContract.Favorites.COLUMN_URLS_SMALL, currentPhoto.urls.small);

                Uri uri = getContentResolver().insert(
                        PhotosplashContract.Favorites.CONTENT_URI,
                        values
                );

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Favorite")
                        .setAction("Add")
                        .build());

                newStatus = true;
            }
            return newStatus;
        }

        @Override
        protected void onPostExecute(Boolean newStatus) {
            //super.onPostExecute(newStatus);
            isFavorite = newStatus;
            updateFavoriteFAB(newStatus);
        }
    }


    private void updateFavoriteFAB(Boolean favoriteStatus) {
        if (favoriteStatus) {
            fabFavorite.setImageResource(R.drawable.heart_full);
        } else {
            fabFavorite.setImageResource(R.drawable.heart);
        }
    }


    private boolean isFavorite(Photo photo) {
        Cursor cursor = getContentResolver().query(
                PhotosplashContract.Favorites.CONTENT_URI,
                null,
                PhotosplashContract.Favorites.COLUMN_PHOTO_ID + " = '" + photo.id + "'",
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

}

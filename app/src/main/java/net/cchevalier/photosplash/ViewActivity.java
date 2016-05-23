package net.cchevalier.photosplash;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.data.PhotosplashContract;
import net.cchevalier.photosplash.models.Photo;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewActivity extends AppCompatActivity {

    private final String TAG = "PhotoSplash";

    private Photo currentPhoto;
    private boolean isFavorite;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // PhotoView handling
        PhotoView photoView  = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

//        String url = getIntent().getStringExtra("url.full");
        currentPhoto = getIntent().getParcelableExtra("currentPhoto");
        Log.d(TAG, "onCreate: " + currentPhoto.toString());

        isFavorite = isFavorite(currentPhoto);

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

        // Bottom Sheet
        TextView photoInfo = (TextView) findViewById(R.id.tv_photo_info);
        photoInfo.setText(currentPhoto.toString());

        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior.setPeekHeight(100);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        // Favorite FAB
        FloatingActionButton fabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to your favorites", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();

                if (!isFavorite) {
                    addToFavorites();

                    Snackbar.make(view, "Added to your favorites", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();

                    isFavorite = !isFavorite;

                } else {

                    Snackbar.make(view, "Already in your favorites", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null)
                            .show();

                }

            }
        });

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


    private void addToFavorites() {

        new AsyncTask<Void, Void, Void>() {

            /**
             * Override this method to perform a computation on a background thread.
             */
            @Override
            protected Void doInBackground(Void... params) {

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

                Uri uri = getContentResolver().insert(PhotosplashContract.Favorites.CONTENT_URI, values);
                Log.d(TAG, "doInBackground: " + uri.toString());
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

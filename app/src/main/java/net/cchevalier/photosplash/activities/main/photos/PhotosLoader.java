package net.cchevalier.photosplash.activities.main.photos;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.cchevalier.photosplash.models.Photo;
import net.cchevalier.photosplash.network.UnsplashService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * PhotosLoader:
 *
 * Created by cch on 21/05/2016.
 */
public class PhotosLoader extends AsyncTaskLoader<List<Photo>> {

    private final String TAG = "PhotoSplash-ATL";

    List<Photo> mPhotos;

    private int mCategoryId;
    private String mCategoryTitle;

    public PhotosLoader(Context context) {
        super(context);
        Log.d(TAG, "PhotosLoader: ");
    }

    public PhotosLoader(Context context, int categoryId, String categoryTitle) {
        super(context);
        Log.d(TAG, "PhotosLoader with: " + categoryId + " " + categoryTitle);
        mCategoryId = categoryId;
        mCategoryTitle = categoryTitle;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     */
    @Override
    public List<Photo> loadInBackground() {

        Log.d(TAG, "loadInBackground starts for category: " + mCategoryId + " " + mCategoryTitle);

        List<Photo> photos = new ArrayList<>();

        int page = 1;
        int perPage = 20;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UnsplashService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UnsplashService unsplashService = retrofit.create(UnsplashService.class);

        Call<List<Photo>> call;

        switch (mCategoryId) {
            case 0: // "New"
                call = unsplashService.getNewPhotos(page, perPage);
                break;
            case 1:
                call = unsplashService.getFeaturedPhotos(page, perPage);
                break;
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
                call = unsplashService.getPhotosFromCategory(mCategoryId, page, perPage);
                break;
            default:
                call = unsplashService.getSearchPhotos("yellow", 1, 18);
                break;
        }

        try {
            Response<List<Photo>> response = call.execute();
            Log.d(TAG, "loadInBackground: Response is " + response.code() + " " + response.message());
            if (response.body().size() > 0) {
                int count = response.body().size();
                for (int i = 0; i < count; i++) {
                    Photo current = response.body().get(i);
                    photos.add(i, current);
                }
            }
        } catch (IOException e ){
            // handle error
            Log.d(TAG, "loadInBackground: Error" + e.getMessage());
        }

        return photos;
    }


    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p/>
     * Must be called from the process's main_menu thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(List<Photo> data) {
        Log.d(TAG, "deliverResult: ");

        if (isReset()) {
            if (data != null) {
                releaseResources(data);
                return;
            }
        }

        List<Photo> oldPhotos = mPhotos;
        mPhotos = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldPhotos != null && oldPhotos != data) {
            releaseResources(oldPhotos);
        }
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");

//        super.onStartLoading();
        if (mPhotos != null) {
            deliverResult(mPhotos);
        }

        // Skip observer

        if (takeContentChanged() || mPhotos == null) {
            forceLoad();
        }
    }

    /**
     * Subclasses must implement this to take care of stopping their loader,
     * as per {@link #stopLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #stopLoading()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading: ");

//        super.onStopLoading();
        cancelLoad();
    }

    /**
     * Subclasses must implement this to take care of resetting their loader,
     * as per {@link #reset()}.  This is not called by clients directly,
     * but as a result of a call to {@link #reset()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: ");

//        super.onReset();
        onStopLoading();

        if (mPhotos != null) {
            releaseResources(mPhotos);
            mPhotos = null;
        }

        // Skip observer
    }

    /**
     * Called if the task was canceled before it was completed.  Gives the class a chance
     * to clean up post-cancellation and to properly dispose of the result.
     *
     * @param data The value that was returned by {@link #loadInBackground}, or null
     *             if the task threw {@link OperationCanceledException}.
     */
    @Override
    public void onCanceled(List<Photo> data) {
        Log.d(TAG, "onCanceled: ");

        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(List<Photo> data) {
        // Nothing to do for List
    }
}

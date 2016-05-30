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
 * Created by cch on 21/05/2016.
 */
public class PhotosLoader extends AsyncTaskLoader<List<Photo>> {

    private final String TAG = "PhotoSplash-ATL";

    private int mCategoryId;
    private String mCategoryTitle;

    List<Photo> mPhotos;

    public PhotosLoader(Context context, int categoryId, String categoryTitle) {
        super(context);
        Log.d(TAG, "PhotosLoader: " + categoryId + " " + categoryTitle);
        mCategoryId = categoryId;
        mCategoryTitle = categoryTitle;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     */
    @Override
    public List<Photo> loadInBackground() {

        Log.d(TAG, "loadInBackground for category: " + mCategoryId + " " + mCategoryTitle);

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
        mPhotos = data;
        super.deliverResult(data);
    }
}

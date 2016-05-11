package net.cchevalier.photosplash.network;

import net.cchevalier.photosplash.BuildConfig;
import net.cchevalier.photosplash.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by cch on 11/05/2016.
 */
public interface UnsplashService {

    String ENDPOINT = "https://api.unsplash.com/";

    String authorizationHeader = "Authorization: Client-ID " + BuildConfig.UNSPLASH_API_KEY;
    String versionHeader = "Accept-Version: v1";


    @Headers({authorizationHeader, versionHeader})
    @GET("photos/")
    Call<List<Photo>> getNewPhotos(@Query("page") int page,
                                   @Query("per_page") int perPage);


}

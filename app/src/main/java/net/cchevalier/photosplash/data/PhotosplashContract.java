package net.cchevalier.photosplash.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cch on 16/05/2016.
 */
public class PhotosplashContract {

    //
    public static final String CONTENT_AUTHORITY = "net.cchevalier.photosplash";

    //
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible Paths
    public static final String PATH_FAVORITES = "favorites";

    // Favorites:
    // inner class that defines the table contents of the favorites table
    public static final class Favorites implements BaseColumns {

        // Content URI represents the base location for the table: favorites
        //      "content://net.cchevalier.photosplash/favorites"
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        //
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        //
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        // Table Schema
        //
        // Table Name
        public static final String TABLE_NAME = "favorites";
        //
        // Table Columns
        //
        // from Photo
        public static final String COLUMN_PHOTO_ID = "photo_id";
        public static final String COLUMN_WIDTH = "width";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_COLOR = "color";
        //
        // from User
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NAME = "user_name";
        //
        // from Urls
        public static final String COLUMN_URLS_FULL = "url_full";
        public static final String COLUMN_URLS_REGULAR = "url_regular";
        public static final String COLUMN_URLS_SMALL = "url_small";


        // Define a function to build a URI to find a specific favorite by it's identifier
        public static Uri buildFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}

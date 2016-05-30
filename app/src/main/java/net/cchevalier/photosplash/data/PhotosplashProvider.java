package net.cchevalier.photosplash.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cch on 18/05/2016.
 */
public class PhotosplashProvider extends ContentProvider {

    private static final String TAG = "PhotoSplash-CP";

    private PhotosplashDbHelper mOpenHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 110;

    //
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    // UriMatcher builder
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PhotosplashContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PhotosplashContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(authority, PhotosplashContract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
        return matcher;
    }


    /**
     * onCreate():
     *
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main_menu thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new PhotosplashDbHelper(getContext());
        return true;
    }


    /**
     * getType():
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                // Multiple records / items
                return PhotosplashContract.Favorites.CONTENT_TYPE;
            case FAVORITE_WITH_ID:
                // Single record / item
                return PhotosplashContract.Favorites.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    /**
     * query(...):
     *
     * Implement this to handle query requests from clients.
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case FAVORITES:
                retCursor = db.query(
                        PhotosplashContract.Favorites.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVORITE_WITH_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        PhotosplashContract.Favorites.TABLE_NAME,
                        projection,
                        PhotosplashContract.Favorites._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }



    /**
     * insert(...):
     *
     * Implement this to handle requests to insert a new row.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after inserting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri retUri;

        switch (sUriMatcher.match(uri)) {

            case FAVORITES:
                long _id = db.insert(PhotosplashContract.Favorites.TABLE_NAME, null, values);
                if (_id > 0) {
                    retUri = PhotosplashContract.Favorites.buildFavoriteUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Log.d(TAG, "insert: " + retUri.toString());
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }


    /**
     * delete(...):
     *
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after deleting.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        if ( null == selection ) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case FAVORITES:
                rowsDeleted = db.delete(PhotosplashContract.Favorites.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Log.d(TAG, "delete: " + rowsDeleted);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


    /**
     * update(...):
     *
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after updating.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

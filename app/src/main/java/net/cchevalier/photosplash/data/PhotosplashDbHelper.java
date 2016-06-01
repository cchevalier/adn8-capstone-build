package net.cchevalier.photosplash.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import net.cchevalier.photosplash.data.PhotosplashContract.Favorites;

/**
 * Created by cch on 16/05/2016.
 */
public class PhotosplashDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Photosplash.db";
    public static final int DATABASE_VERSION = 1;


    /**
     * Create a helper object to create, open, and/or manage a database.
     * @param context to use to open or create the database
     */
    public PhotosplashDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + PhotosplashContract.Favorites.TABLE_NAME +
                        " (" +
                        PhotosplashContract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PhotosplashContract.Favorites.COLUMN_PHOTO_ID + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_WIDTH + " INTEGER NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_HEIGHT + " INTEGER NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_COLOR + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_USER_ID + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_URLS_FULL + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_URLS_REGULAR + " TEXT NOT NULL, " +
                        PhotosplashContract.Favorites.COLUMN_URLS_SMALL + " TEXT NOT NULL " +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PhotosplashContract.Favorites.TABLE_NAME);
        onCreate(db);
    }
}

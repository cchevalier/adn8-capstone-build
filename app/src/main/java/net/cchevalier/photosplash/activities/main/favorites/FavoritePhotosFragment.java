package net.cchevalier.photosplash.activities.main.favorites;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.data.PhotosplashContract;

/**
 * FavoritePhotosFragment
 */
public class FavoritePhotosFragment extends Fragment {

    public final String TAG = "PhotoSplash-FFrag";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FavoriteAdapter mFavoriteAdapter;

    // From docs: A unique identifier for this loader. Can be whatever you want.
    public static final int FAVORITES_LOADER_ID = 44;

    // Fragment initialization parameters
    private static final String CATEGORY_TITLE = "Category_Title";
    private String mCategoryTitle;


    public FavoritePhotosFragment() {
    }

    public static FavoritePhotosFragment newInstance(String categoryTitle) {
        FavoritePhotosFragment fragment = new FavoritePhotosFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_TITLE, categoryTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(CATEGORY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_favorite_photos_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_favorites);

        int nCols = getResources().getInteger(R.integer.photos_columns);
        mLayoutManager = new StaggeredGridLayoutManager(nCols, GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFavoriteAdapter = new FavoriteAdapter();
        mRecyclerView.setAdapter(mFavoriteAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");

        // Initialize the loader with a special ID and the defined callbacks from above
        getActivity().getSupportLoaderManager().restartLoader(
                FAVORITES_LOADER_ID,
                new Bundle(),
                favoritesLoader);
        super.onActivityCreated(savedInstanceState);
    }


    // Defines the asynchronous callback for the contacts data loader
    private LoaderManager.LoaderCallbacks<Cursor> favoritesLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {

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

                    // Construct the loader
                    CursorLoader cursorLoader = new CursorLoader(
                            getActivity(),
                            PhotosplashContract.Favorites.CONTENT_URI, // URI
                            projectionFields, // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            null // the sort order
                    );

                    // Return the loader for use
                    return cursorLoader;
                }

                // When the system finishes retrieving the Cursor through the CursorLoader,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    mFavoriteAdapter.swapCursor(cursor);
                }

                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    mFavoriteAdapter.swapCursor(null);
                }
            };
}

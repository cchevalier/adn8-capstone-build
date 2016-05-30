package net.cchevalier.photosplash.activities.main.photos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.models.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * PhotosByCategoryFragment
 */
public class PhotosByCategoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Photo>> {

    public static final String TAG = "PhotoSplash-PFrag";

    private static final int PHOTO_LOADER_ID = 33;

    // Fragment initialization parameters
    private static final String CATEGORY_TITLE = "Category_Title";
    private static final String CATEGORY_ID = "Category_Id";

    private String mCategoryTitle;
    private int mCategoryId;

    private static final String PHOTOS_LOADED = "Photos_Loaded";
    private ArrayList<Photo> mPhotos;

//    private static final String NEW_LOAD_REQUIRED = "New_Load_Required";
//    private boolean newLoadRequired;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PhotoAdapter mPhotoAdapter;

    public PhotosByCategoryFragment() {
        // Required empty public constructor
    }

    // Factory method provided parameters.
    public static PhotosByCategoryFragment newInstance(String categoryTitle, int categoryId) {
        Log.d(TAG, "newInstance: ");

        PhotosByCategoryFragment fragment = new PhotosByCategoryFragment();

        Bundle args = new Bundle();
        args.putString(CATEGORY_TITLE, categoryTitle);
        args.putInt(CATEGORY_ID, categoryId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(CATEGORY_TITLE);
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        }

        if (savedInstanceState != null) {
            String savedCategoryTitle = savedInstanceState.getString(CATEGORY_TITLE, "Fav");
            int savedCategoryId = savedInstanceState.getInt(CATEGORY_ID, -1);
            Log.d(TAG, "onCreate: saved: " + savedCategoryId + " " + savedCategoryTitle);
//            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS_LOADED);
        }

        mPhotos = new ArrayList<>();
//        newLoadRequired = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_photos_by_category_fragment, container, false);

        if (savedInstanceState != null) {
//            newLoadRequired = savedInstanceState.getBoolean(NEW_LOAD_REQUIRED, true);
            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS_LOADED);
        }

        // RecyclerView stuff
//        mPhotos = new ArrayList<>();
        mPhotoAdapter = new PhotoAdapter(mPhotos);

        // Adaptive Grid based on screen width
        // see https://www.udacity.com/course/viewer#!/c-ud862-nd/l-4964230471/m-4904228664
        int nCols = getResources().getInteger(R.integer.photos_columns);
        mLayoutManager = new StaggeredGridLayoutManager(nCols, GridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photos_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPhotoAdapter);

        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(CATEGORY_TITLE);
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        } else {
            mCategoryTitle = "Undefined";
            mCategoryId = 99;
        }


        return rootView;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");

        super.onActivityCreated(savedInstanceState);

        // initialize a Loader within the activity's onCreate() method, or within the fragment's onActivityCreated() method.
        getLoaderManager().initLoader(PHOTO_LOADER_ID, null, this).forceLoad();

        if (savedInstanceState != null) {
//            newLoadRequired = savedInstanceState.getBoolean(NEW_LOAD_REQUIRED, true);
            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS_LOADED);
        }

//        if (newLoadRequired) {
//            Log.d(TAG, "onActivityCreated: restartLoader...");
//            getLoaderManager().restartLoader(PHOTO_LOADER_ID, null, this);
//        }


    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState: " + newLoadRequired + " " + mPhotos.size());
//        outState.putBoolean(NEW_LOAD_REQUIRED, newLoadRequired);
        outState.putParcelableArrayList(PHOTOS_LOADED, mPhotos);
        super.onSaveInstanceState(outState);
    }

    //
    //
    // LoaderCallbacks
    //
    //
    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        PhotosLoader photosLoader = new PhotosLoader(getActivity(), mCategoryId, mCategoryTitle);
        return photosLoader;
    }

    /**
     * Called when a previously created loader has finished its load.
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> data) {
        Log.d(TAG, "onLoadFinished: " + data.size());

        mPhotos.clear();
        mPhotoAdapter.notifyDataSetChanged();

        int count = data.size();
        for (int i = 0; i < count; i++) {
            Photo current = data.get(i);
            mPhotos.add(i, current);
            mPhotoAdapter.notifyItemChanged(i);
        }
        mPhotoAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);

//        newLoadRequired = false;
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mPhotos.clear();
        mPhotoAdapter.notifyDataSetChanged();

//        newLoadRequired = true;
    }

}

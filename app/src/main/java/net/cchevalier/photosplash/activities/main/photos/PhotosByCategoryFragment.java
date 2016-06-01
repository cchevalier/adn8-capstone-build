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
import android.widget.TextView;

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

    // Fragment initialization parameters
    private String mCategoryTitle;
    private static final String CATEGORY_TITLE = "Category_Title";
    private int mCategoryId;
    private static final String CATEGORY_ID = "Category_Id";

    private ArrayList<Photo> mPhotos;
    private static final String PHOTOS = "Photos";

    private static final int PHOTOS_LOADER_ID = 33;
    private LoaderManager.LoaderCallbacks<List<Photo>> mCallbacks;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PhotoAdapter mPhotoAdapter;

    private TextView mStatusInfo;

    // Required empty public constructor
    public PhotosByCategoryFragment() {
        Log.d(TAG, "PhotosByCategoryFragment: ");
    }

    // PhotosByCategoryFragment: Factory method with parameters
    public static PhotosByCategoryFragment newInstance(String categoryTitle, int categoryId) {
        Log.d(TAG, "PhotosByCategoryFragment - newInstance: ");

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

        mPhotos = new ArrayList<>();
        mCallbacks = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View rootView = inflater.inflate(R.layout.main_photos_by_category_fragment, container, false);

        // RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photos_recycler_view);

        // LayoutManager: adaptive Grid based on screen width
        // see https://www.udacity.com/course/viewer#!/c-ud862-nd/l-4964230471/m-4904228664
        int nCols = getResources().getInteger(R.integer.photos_columns);
        mLayoutManager = new StaggeredGridLayoutManager(nCols, GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Adapter
        mPhotoAdapter = new PhotoAdapter(mPhotos);
        mRecyclerView.setAdapter(mPhotoAdapter);

        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(CATEGORY_TITLE);
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        } else {
            mCategoryTitle = "Undefined";
            mCategoryId = 99;
        }

        mStatusInfo = (TextView) rootView.findViewById(R.id.tv_status);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");

        super.onActivityCreated(savedInstanceState);

        // TODO: Serious Bug
        // see https://code.google.com/p/android/issues/detail?id=183783
        //
        // initialize a Loader within the activity's onCreate() method, or within the fragment's onActivityCreated() method.
        getLoaderManager().initLoader(PHOTOS_LOADER_ID, null, mCallbacks);

//        if (savedInstanceState != null) {
//            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS);
//        } else {
//            getLoaderManager().initLoader(PHOTOS_LOADER_ID, null, mCallbacks);
//        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState: ");
//
//        outState.putParcelableArrayList(PHOTOS, mPhotos);
//        super.onSaveInstanceState(outState);
//    }

    //
    // LoaderCallbacks
    //
    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");

        PhotosLoader photosLoader = new PhotosLoader(getActivity(), mCategoryId, mCategoryTitle);
        return photosLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> data) {

        if (data != null && data.size() != 0) {
            Log.d(TAG, "onLoadFinished: " + data.size());

            mStatusInfo.setVisibility(View.GONE);

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
        } else {
            mStatusInfo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {
        Log.d(TAG, "onLoaderReset: ");

        mPhotos.clear();
        mPhotoAdapter.notifyDataSetChanged();
    }

}

package net.cchevalier.photosplash.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.data.PhotosplashContract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesPhotosFragment extends Fragment {

    public final String TAG = "PhotoSplash";

    private SimpleCursorAdapter adapter;

    // From docs: A unique identifier for this loader. Can be whatever you want.
    public static final int FAVORITES_LOADER_ID = 1664;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public FavoritesPhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FavoritesPhotosFragment newInstance(String param1, String param2) {
        FavoritesPhotosFragment fragment = new FavoritesPhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setupCursorAdapter();

        // Initialize the loader with a special ID and the defined callbacks from above
        getActivity().getSupportLoaderManager().initLoader(
                FAVORITES_LOADER_ID,
                new Bundle(),
                contactsLoader);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =
         inflater.inflate(R.layout.fragment_favorites_photos, container, false);

        ListView lvFavorites = (ListView) rootView.findViewById(R.id.lv_favorites);
        lvFavorites.setAdapter(adapter);

        return rootView;
    }


    // Create simple cursor adapter to connect the cursor dataset we load with a ListView
    private void setupCursorAdapter() {

        // Column data from cursor to bind views from
        String[] uiBindFrom = {PhotosplashContract.Favorites.COLUMN_PHOTO_ID };

        // View IDs which will have the respective column data inserted
        int[] uiBindTo = { R.id.tv_fav_photo_id };

        // Create the simple cursor adapter to use for our list
        // specifying the template to inflate (item_contact),
        adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.item_favorites,
                null,
                uiBindFrom,
                uiBindTo,
                0);
    }


    // Defines the asynchronous callback for the contacts data loader
    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                // Create and return the actual cursor loader for the contacts data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                    // Define the columns to retrieve
                    String[] projectionFields = new String[] { PhotosplashContract.Favorites._ID,
                            PhotosplashContract.Favorites.COLUMN_PHOTO_ID };

                    // Construct the loader
                    CursorLoader cursorLoader = new CursorLoader(
                            getContext(),
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
                    // The swapCursor() method assigns the new Cursor to the adapter
                    adapter.swapCursor(cursor);
                }

                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    adapter.swapCursor(null);
                }
            };





/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/

/*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

/*
    */
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/
}

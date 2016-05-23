package net.cchevalier.photosplash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.adapters.PhotoAdapter;
import net.cchevalier.photosplash.models.Photo;
import net.cchevalier.photosplash.network.UnsplashService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosByCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosByCategoryFragment extends Fragment {

    public final String TAG = "PhotoSplash";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Category_Title";
    private static final String ARG_PARAM2 = "Category_Id";

    private String mCategoryTitle;
    private int mCategoryId;

    private ArrayList<Photo> mPhotos;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PhotoAdapter mPhotoAdapter;

    public PhotosByCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static PhotosByCategoryFragment newInstance(String param1, int param2) {
        PhotosByCategoryFragment fragment = new PhotosByCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(ARG_PARAM1);
            mCategoryId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photos_by_category, container, false);

        // RecyclerView stuff
        mPhotos = new ArrayList<>();
        mPhotoAdapter = new PhotoAdapter(mPhotos);

        // Adaptive Grid based on screen width
        // see https://www.udacity.com/course/viewer#!/c-ud862-nd/l-4964230471/m-4904228664
        int nCols = getResources().getInteger(R.integer.photos_columns);
        mLayoutManager = new StaggeredGridLayoutManager(nCols, GridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photos_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPhotoAdapter);

        if (getArguments() != null) {
            mCategoryTitle = getArguments().getString(ARG_PARAM1);
            mCategoryId = getArguments().getInt(ARG_PARAM2);
        } else {
            mCategoryId = 99;
        }
        getPhotos(mCategoryId);

        return rootView;
    }


    //
    //
    public void getPhotos(int choice) {

        int page = 1;
        int perPage = 20;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UnsplashService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UnsplashService unsplashService = retrofit.create(UnsplashService.class);

        Call<List<Photo>> call;

        switch (choice) {
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
                call = unsplashService.getPhotosFromCategory(choice, page, perPage);
                break;
            default:
                call = unsplashService.getSearchPhotos("yellow", 1, 18);
                break;
        }

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {

/*
                String msg = "Successful Request!";
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
*/

                // LOG some Response Headers
                Log.d(TAG, "onResponse: <RESPONSE HEADERS>" );
                String xPerPage = response.headers().get("X-Per-Page");
                Log.d(TAG, "onResponse: X-Per-Page: " + xPerPage);
                String xTotal = response.headers().get("X-Total");
                Log.d(TAG, "onResponse: X-Total: " + xTotal);

                // LOG Response Body
                Log.d(TAG, "onResponse: <RESPONSE BODY>");
                Log.d(TAG, "onResponse: Size = " + response.body().size());

                if (response.body().size() > 0) {

                    mPhotos.clear();
                    mPhotoAdapter.notifyDataSetChanged();

                    int count = response.body().size();
                    for (int i = 0; i < count; i++) {
                        Photo current = response.body().get(i);
                        mPhotos.add(i, current);
                        mPhotoAdapter.notifyItemChanged(i);
                    }
//                    mPhotoAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {

            }
        });
    }


}

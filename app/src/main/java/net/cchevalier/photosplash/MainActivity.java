package net.cchevalier.photosplash;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final String TAG = "PhotoSplash";

    private ArrayList<Photo> mPhotos;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PhotoAdapter mPhotoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Retrieving photos...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // RecyclerView stuff
        mPhotos = new ArrayList<>();
        mPhotoAdapter = new PhotoAdapter(mPhotos);
        mLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPhotoAdapter);

        // Track Picasso perfs
        Picasso
                .with(getBaseContext())
                .setIndicatorsEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String selected = "PhotoSplash";
        int choice;

        if (id == R.id.nav_category_new) {
            selected = "New";
            choice = 0;
        } else if (id == R.id.nav_category_featured) {
            selected = "Featured";
            choice = 1;
        } else if (id == R.id.nav_category_favorites) {
            selected = "Favorites";
            choice = -1;
        } else if (id == R.id.nav_category_buildings) {
            selected = "Buildings";
            choice = 2;
        } else if (id == R.id.nav_category_food) {
            selected = "Food";
            choice = 3;
        } else if (id == R.id.nav_category_nature) {
            selected = "Nature";
            choice = 4;
        } else if (id == R.id.nav_category_objects) {
            selected = "Objects";
            choice = 8;
        } else if (id == R.id.nav_category_people) {
            selected = "People";
            choice = 6;
        } else if (id == R.id.nav_category_technology) {
            selected = "Technology";
            choice = 7;
        } else {
            choice = 99;
        }
        getSupportActionBar().setTitle(selected);
        getPhotos(choice);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                String msg = "Successful Request!";
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

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

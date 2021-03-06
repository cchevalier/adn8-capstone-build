package net.cchevalier.photosplash.activities.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.PhotosplashApplication;
import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.activities.main.favorites.FavoritePhotosFragment;
import net.cchevalier.photosplash.activities.main.photos.PhotosByCategoryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final String TAG = "PhotoSplash-Main";

    private Tracker mTracker;

    private static final String CATEGORY_TITLE = "Category_Title";
    private static final String CATEGORY_ID = "Category_Id";

    public String mCategoryTitle;
    public int mCategoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Obtain the shared Tracker instance.
        PhotosplashApplication application = (PhotosplashApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Enable Picasso tracking perfomance
        Picasso.with(getBaseContext())
                .setIndicatorsEnabled(false);

        if (savedInstanceState != null) {
            mCategoryTitle = savedInstanceState.getString(CATEGORY_TITLE, getResources().getString(R.string.category_undefined));
            mCategoryId = savedInstanceState.getInt(CATEGORY_ID, 99);
        } else {
            mCategoryTitle = getResources().getString(R.string.category_new);
            mCategoryId = 0;
        }
        addSelectedFragment();
        sendScreenCategoryName();

        // Ad request
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString(CATEGORY_TITLE, mCategoryTitle);
        outState.putInt(CATEGORY_ID, mCategoryId);
        super.onSaveInstanceState(outState);
    }

    private void addSelectedFragment() {
        Log.d(TAG, "addSelectedFragment: " + mCategoryId + " " + mCategoryTitle);

        getSupportActionBar().setTitle(mCategoryTitle);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mCategoryId >= 0) {
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.fragment_container, PhotosByCategoryFragment.newInstance(mCategoryTitle, mCategoryId));
        } else {
            ft.replace(R.id.fragment_container, FavoritePhotosFragment.newInstance(mCategoryTitle));
        }
        // Complete the changes added above
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            String about = getResources().getString(R.string.about);
            Toast toast = Toast.makeText(this, about, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: " + item.getTitle());

        // Handle navigation view item clicks here.
        mCategoryTitle = (String) item.getTitle();
        int id = item.getItemId();

        if (id == R.id.nav_category_favorites) {
//            mCategoryTitle = "Favorites";
            mCategoryId = -1;
        } else if (id == R.id.nav_category_new) {
//            mCategoryTitle = "New";
            mCategoryId = 0;
        } else if (id == R.id.nav_category_featured) {
//            mCategoryTitle = "Featured";
            mCategoryId = 1;
        } else if (id == R.id.nav_category_buildings) {
//            mCategoryTitle = "Buildings";
            mCategoryId = 2;
        } else if (id == R.id.nav_category_food) {
//            mCategoryTitle = "Food";
            mCategoryId = 3;
        } else if (id == R.id.nav_category_nature) {
//            mCategoryTitle = "Nature";
            mCategoryId = 4;
        } else if (id == R.id.nav_category_objects) {
//            mCategoryTitle = "Objects";
            mCategoryId = 8;
        } else if (id == R.id.nav_category_people) {
//            mCategoryTitle = "People";
            mCategoryId = 6;
        } else if (id == R.id.nav_category_technology) {
//            mCategoryTitle = "Technology";
            mCategoryId = 7;
        } else {
            mCategoryTitle = getResources().getString(R.string.category_undefined);
            mCategoryId = 99;
        }

        getSupportActionBar().setTitle(mCategoryTitle);
        addSelectedFragment();
        sendScreenCategoryName();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void sendScreenCategoryName() {
        Log.d(TAG, "sendScreenCategoryName: " + mCategoryTitle);

        mTracker.setScreenName("Category " + mCategoryTitle);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


//    /**
//     * Dispatch onStart() to all fragments.  Ensure any created loaders are
//     * now started.
//     */
//    @Override
//    protected void onStart() {
//        Log.d(TAG, "onStart: ");
//        super.onStart();
//    }
//
//    /**
//     * Dispatch onResume() to fragments.  Note that for better inter-operation
//     * with older versions of the platform, at the point of this call the
//     * fragments attached to the activity are <em>not</em> resumed.  This means
//     * that in some cases the previous state may still be saved, not allowing
//     * fragment transactions that modify the state.  To correctly interact
//     * with fragments in their proper state, you should instead override
//     * {@link #onResumeFragments()}.
//     */
//    @Override
//    protected void onResume() {
//        Log.d(TAG, "onResume: ");
//        super.onResume();
//    }
//
//    /**
//     * This is the fragment-orientated version of {@link #onResume()} that you
//     * can override to perform operations in the Activity at the same point
//     * where its fragments are resumed.  Be sure to always call through to
//     * the super-class.
//     */
//    @Override
//    protected void onResumeFragments() {
//        Log.d(TAG, "onResumeFragments: ");
//        super.onResumeFragments();
//    }
//
//    /**
//     * Dispatch onPause() to fragments.
//     */
//    @Override
//    protected void onPause() {
//        Log.d(TAG, "onPause: ");
//        super.onPause();
//    }

//    @Override
//    protected void onStop() {
//        Log.d(TAG, "onStop: ");
//        super.onStop();
//    }
//
//    @Override
//    protected void onRestart() {
//        Log.d(TAG, "onRestart: ");
//        super.onRestart();
//    }
//
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: ");
//        super.onDestroy();
//    }


}

package example.com.taxicityappdriver.controller.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.controller.fragments.WaitingTripsFragment;
import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

import static example.com.taxicityappdriver.controller.WaitingTripAdapter.isBusyDriver;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static BackEnd db = BackEndFactory.getInstance();
    private WaitingTripsFragment waitingTripsFragment;
    private final String TAG = "mainActivity";

    private final int FRAGMENT_WAITING_TRIPS = 0;
    private final int FRAGMENT_HISTORY_TRIPS = 1;
    private final int FRAGMENT_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuthentication();

        // 6 - Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        if (isBusyDriver()) {
            Toast.makeText(this, "You can't change page you are in current trip !", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (id) {
            case R.id.activity_main_drawer_last_trips:
                break;
            case R.id.activity_main_drawer_waiting_trips:
                showFragment(FRAGMENT_WAITING_TRIPS);
                break;
            case R.id.activity_main_drawer_settings:
                break;
            case R.id.activity_main_drawer_log_out:
                logOut();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // 5 - Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_WAITING_TRIPS:
                //this.showTripItem();
                this.showWaitingTrip();
                break;
            case FRAGMENT_HISTORY_TRIPS:
                //this.showProfileFragment();
                break;
            case FRAGMENT_SETTINGS:
                //this.showParamsFragment();
                break;
            default:
                break;
        }
    }

    // ---

    // 4 - Create each fragment page and show it

    public void showWaitingTrip() {
        if (this.waitingTripsFragment == null) this.waitingTripsFragment = new WaitingTripsFragment();
        this.startTransactionFragment(this.waitingTripsFragment);
    }

    // ---

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    // 1 - Show first fragment when activity is created
    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showFragment(FRAGMENT_WAITING_TRIPS);
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    private void checkAuthentication() {
        if (!db.isSigned()) {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backstack
            startActivity(intent);
            finish();
        }
    }

    private void logOut() {
        db.signOut();
        checkAuthentication();
    }

    @Override
    protected void onStop() {

        Log.i(TAG, "onDestroy: ");
        //Prevent quit
        if (isBusyDriver()) {
            Log.i(TAG, "DRIVER BUSY " + WaitingTripViewHolder.getBusyKey());
            db.getTrip(WaitingTripViewHolder.getBusyKey(), new ActionCallBack<Trip>() {
                @Override
                public void onSuccess(Trip obj) {
                    obj.setDriverEmail(null);
                    obj.setStatusAsEnum(Trip.TripStatus.AVAILABLE);
                    Log.i(TAG, "SUCESS DESTROY UPDATE " + obj.getKey());
                    db.updateTrip(obj, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object obj) {
                            Log.i(TAG, "onSuccess: REUPDATE TRIP");

                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Log.i(TAG, "onFailure: " + exception.getMessage());
                        }

                        @Override
                        public void onProgress(String status, double percent) {

                        }
                    });
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i(TAG, "onFailure: " + exception.getMessage());


                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });
        }

        super.onStop();

    }


}

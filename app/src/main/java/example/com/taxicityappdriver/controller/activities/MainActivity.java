package example.com.taxicityappdriver.controller.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import example.com.taxicityappdriver.controller.WaitingTripAdapter;
import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.controller.fragments.SettingsFragment;
import example.com.taxicityappdriver.services.DriverService;
import example.com.taxicityappdriver.services.MyBroadcastReceiver;
import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.fragments.HistoryTripFragment;
import example.com.taxicityappdriver.controller.fragments.WaitingTripsFragment;
import example.com.taxicityappdriver.services.ClosingService;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

import static example.com.taxicityappdriver.controller.WaitingTripAdapter.isBusyDriver;


/**
 *  © Copyrights
 *  Netanel COHEN SOLAL - 1444669
 *  Raphael Amar - 1186865
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private static BackEnd db = BackEndFactory.getInstance();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private WaitingTripsFragment waitingTripsFragment;
    private HistoryTripFragment historyTripFragment;
    private SettingsFragment settingsFragment;
    private final String TAG = "mainActivity";

    private final int FRAGMENT_WAITING_TRIPS = 0;
    private final int FRAGMENT_HISTORY_TRIPS = 1;
    private final int FRAGMENT_SETTINGS = 2;


    /**
     * Create the activity and init it.
     * 1) Check if the user is authenticated else start authActivity.
     * 2) Register the Activity to the Services
     * 3) Configure the ToolBar and Navigation Drawer.
     * 4) Show firstFragment (After UI initialisation) [WaitingTripsFragments]
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Register for Notifications
        checkAuthentication();

        registerReceiver(
                new MyBroadcastReceiver(),
                new IntentFilter("NewOrder"));

        startService(new Intent(getBaseContext(), DriverService.class));

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        startService(new Intent(getBaseContext(), ClosingService.class));

        initNavHeader(); //Init Profile picture and Name

        findViewById(R.id.activity_main_frame_layout).post(new Runnable() {
            @Override
            public void run() {
                showFirstFragment(); //Show after UI initialisation
            }
        });

    }

    /***
     * Handle back click to close menu
     */
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialisation Profile picture and Name
     */
    private void initNavHeader() {
        final TextView name = navigationView.getHeaderView(0).findViewById(R.id.name_nav_header);

        name.post(new Runnable() {
            @Override
            public void run() {
                if (db.getCurrentDriver() != null)
                    name.setText(db.getCurrentDriver().getFirstName() + " " + db.getCurrentDriver().getLastName() + " - " + db.getCurrentDriver().getIdNumber());
                else
                    name.setText(getString(R.string.welcome_back_hint_nv_drawer));
            }
        });


    }


    /**
     * Showing appropriate fragment when the item is selected in the menu.
     *
     * @param item item selected
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Prevent quit the activity when the driver is busy
        if (isBusyDriver()) {
            Toast.makeText(this, getString(R.string.busy_driver_not_quit_msg), Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (id) {
            case R.id.activity_main_drawer_last_trips:
                showFragment(FRAGMENT_HISTORY_TRIPS);
                break;
            case R.id.activity_main_drawer_waiting_trips:
                showFragment(FRAGMENT_WAITING_TRIPS);
                break;
            case R.id.activity_main_drawer_settings:
                showFragment(FRAGMENT_SETTINGS);
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

    /**
     * Configure the Toolbar
     */
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Configure Drawer Layout
     */
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.my_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configure NavigationView
     */
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    /**
     * Show fragment according an Identifier
     *
     * @param fragmentIdentifier
     */
    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_WAITING_TRIPS:
                this.showWaitingTrip();
                break;
            case FRAGMENT_HISTORY_TRIPS:
                this.showTripHistory();
                break;
            case FRAGMENT_SETTINGS:
                this.showSettingsFragment();
                break;
            default:
                break;
        }
    }

    /**
     * Show Waiting Fragment
     */
    public void showWaitingTrip() {
        if (this.waitingTripsFragment == null)
            this.waitingTripsFragment = new WaitingTripsFragment();
        this.startTransactionFragment(this.waitingTripsFragment);
    }

    /**
     * Show Trip history fragment
     */
    public void showTripHistory() {
        if (this.historyTripFragment == null) this.historyTripFragment = new HistoryTripFragment();
        this.startTransactionFragment(this.historyTripFragment);
    }

    /**
     * Show Settings fragment
     */
    public void showSettingsFragment() {
        if (this.settingsFragment == null) this.settingsFragment = new SettingsFragment();
        this.startTransactionFragment(this.settingsFragment);
    }


    /**
     * Generic method that will replace and show a fragment inside the MainActivity Frame Layout
     *
     * @param fragment
     */
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    /**
     * Show first fragment when activity is created
     */
    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showFragment(FRAGMENT_WAITING_TRIPS);
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    /**
     * Check If the user is authenticated ELSE start AuthActivity
     */
    private void checkAuthentication() {
        if (!db.isSigned()) {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backStack
            startActivity(intent);
            finish();
        }
    }

    /**
     * LogOut user (Destroy Database Instance)
     */
    private void logOut() {
        db.signOut();
        checkAuthentication();
    }


}

package example.com.taxicityappdriver.controller.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ramotion.fluidslider.FluidSlider;

import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.WaitingTripAdapter;
import example.com.taxicityappdriver.model.entities.Trip;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.interfaces.NotifyDataChange;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static example.com.taxicityappdriver.model.helpers.Helpers.ucFirst;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingTripsFragment extends Fragment {

    public final static BackEnd db = BackEndFactory.getInstance();
    private final String TAG = "WaitingTripsFragment";

    private List<Trip> items; //Notified list of trip items.

    private RecyclerView recyclerView; //RecyclerView instance.
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout instance.
    private FloatingActionButton fab; //FloatingActionButton instance to activate filters.

    //Save state of filters
    private static boolean switchByDistanceState;
    private static boolean switchByCityState;
    private static int distanceSavedState;
    private static String citySavedInstance;

    public WaitingTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Bind View Reference
        View view = inflater.inflate(R.layout.fragment_waiting_trips, container, false);
        recyclerView = view.findViewById(R.id.mainRecycleView);
        swipeRefreshLayout = view.findViewById(R.id.fragment_main_swipe_container);
        fab = view.findViewById(R.id.fab);

        //Refresh Layout Listener
        final WaitingTripsFragment MyFragment = this;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Reload the fragment
                db.stopNotifyToTripList();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MyFragment).attach(MyFragment).commit();


            }
        });


        //Click Floating Button Listener.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog(); //Showing the filter dialog
            }
        });


        if (WaitingTripAdapter.isBusyDriver()) {
            switchByDistanceState = false;
            switchByCityState = false;
        }

        if (switchByDistanceState)
            applyFilterByDistance(distanceSavedState);
        else if (switchByCityState)
            applyFilterByCity(citySavedInstance);
        else
            initRecyclerView();

        return view;

    }


    /**
     * Initialisation of the RecyclerView.
     */
    private void initRecyclerView() {

        //Init Layout Manager for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true); //Reverse Data
        linearLayoutManager.setReverseLayout(true); //Reverse Data
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Initialisation Of data
        db.notifyToTripListWaiting(new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    WaitingTripAdapter adapter = new WaitingTripAdapter(items);
                    recyclerView.setAdapter(adapter);

                } else
                    recyclerView.getAdapter().notifyDataSetChanged();


            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), getString(R.string.error_getting_trip_list) + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onDestroy() {
        db.stopNotifyToTripList();
        super.onDestroy();
    }

    /**
     * Apply filter by Distance (Change notify function)
     *
     * @param distance the maximum distance (in km)
     */
    public void applyFilterByDistance(int distance) {

        //Reset
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);

        //Init Layout Manager for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true); //Reverse Data
        linearLayoutManager.setReverseLayout(true); //Reverse Data
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Initialisation Of data
        db.notifyToTripListWaitingByDistance(distance, new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    WaitingTripAdapter adapter = new WaitingTripAdapter(items);
                    recyclerView.setAdapter(adapter);

                } else
                    recyclerView.getAdapter().notifyDataSetChanged();


            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), getString(R.string.error_getting_trip_list) + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Apply filter by City Name (Change notify function)
     * (Case sensitive)
     *
     * @param city The city to find.
     */
    public void applyFilterByCity(final String city) {
        //Reset
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);

        //Init Layout Manager for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true); //Reverse Data
        linearLayoutManager.setReverseLayout(true); //Reverse Data
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //Initialisation Of data
        db.notifyToTripListWaitingByCity(city, new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    WaitingTripAdapter adapter = new WaitingTripAdapter(items);
                    //adapter.setHasStableIds(true);
                    recyclerView.setAdapter(adapter);

                } else
                    recyclerView.getAdapter().notifyDataSetChanged();


            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), getString(R.string.error_getting_trip_list) + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


    /**
     * Init the Filter dialog View
     */
    private void showFilterDialog() {
        //Prevent filtering busy driver
        if (WaitingTripAdapter.isBusyDriver()) {
            Toast.makeText(getContext(), getString(R.string.busy_driver_not_quit_msg), Toast.LENGTH_SHORT).show();
            return;
        }

        //Init Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_waiting_trip, null);


        //Bind View
        final Switch filterByCity = dialogView.findViewById(R.id.filter_by_city_switch);
        final Switch filterByDistance = dialogView.findViewById(R.id.filter_by_distance_switch);
        final FluidSlider fluidSliderDistance = dialogView.findViewById(R.id.distance_fluid_slider);
        final EditText cityNameEditText = dialogView.findViewById(R.id.filter_by_city_edit_text);
        final LinearLayout linearLayourCity = dialogView.findViewById(R.id.filter_by_city_linear_layout);
        final int minDistance = 5;
        final int maxDistance = 600;
        final int totalDisatance = maxDistance - minDistance;

        //Init Listeners
        fluidSliderDistance.setStartText(String.valueOf(minDistance));
        fluidSliderDistance.setEndText(String.valueOf(maxDistance));
        fluidSliderDistance.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float pos) {
                final String value = String.valueOf((int) (minDistance + totalDisatance * pos));
                fluidSliderDistance.setBubbleText(value); //Change value of the fluie slider
                return Unit.INSTANCE;
            }
        });


        //Restore State
        if (switchByDistanceState) {
            filterByDistance.setChecked(true);
            fluidSliderDistance.setVisibility(View.VISIBLE);
            fluidSliderDistance.setPosition(distanceSavedState);
        } else if (switchByCityState) {
            filterByCity.setChecked(true);
            linearLayourCity.setVisibility(View.VISIBLE);
            if (citySavedInstance != null)
                cityNameEditText.setText(citySavedInstance);
        }

        //Apply just One filter at time
        filterByDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchByDistanceState = isChecked;
                if (isChecked) {
                    fluidSliderDistance.setVisibility(View.VISIBLE);
                    fluidSliderDistance.setPosition(distanceSavedState);
                    filterByCity.setChecked(false);
                } else
                    fluidSliderDistance.setVisibility(View.GONE);
            }
        });

        filterByCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchByCityState = isChecked;
                if (isChecked) {
                    linearLayourCity.setVisibility(View.VISIBLE);
                    filterByDistance.setChecked(false);
                } else
                    linearLayourCity.setVisibility(View.GONE);
            }
        });


        //Init UI View of the Dialog
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle(getString(R.string.filter_title_dialog));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.apply_filter_btn_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Apply filter functions
                if (switchByDistanceState) {
                    distanceSavedState = (int) (minDistance + totalDisatance * fluidSliderDistance.getPosition());
                    applyFilterByDistance(distanceSavedState);
                } else if (switchByCityState) {
                    if (cityNameEditText != null && !TextUtils.isEmpty(cityNameEditText.getText())) {
                        citySavedInstance = ucFirst(cityNameEditText.getText().toString());
                        applyFilterByCity(citySavedInstance);
                    }
                } else {
                    initRecyclerView();
                }


            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dismiss_btn_dialog_filter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.show();
    }

}

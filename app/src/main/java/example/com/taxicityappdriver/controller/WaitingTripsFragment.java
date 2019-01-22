package example.com.taxicityappdriver.controller;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.backend.NotifyDataChange;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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

    private static boolean switchByDistanceState;
    private static boolean switchByCityState;

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
                //Prevent Busy driver to quit the trip.
                if (false) {
                    Toast.makeText(getContext(), "You can't refresh when your have a trip in progress !", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    //Reload the fragment
                    db.stopNotifyToTripList();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(MyFragment).attach(MyFragment).commit();
                }

            }
        });


        //Click Floating Button Listener.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog(); //Showing the filter dialog
            }
        });


        //Initialisation of the RecyclerView.
        this.initRecyclerView(view);

        return view;

    }


    private void refresh() {
        db.stopNotifyToTripList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * Initialisation of the RecyclerView.
     *
     * @param v
     */
    private void initRecyclerView(final View v) {

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
                    //adapter.setHasStableIds(true);
                    recyclerView.setAdapter(adapter);

                } else
                    recyclerView.getAdapter().notifyDataSetChanged();


            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "error to get trip list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onDestroy() {
        db.stopNotifyToTripList();
        super.onDestroy();
    }

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
        db.notifyToTripListWaitingByDistance(6000, new NotifyDataChange<List<Trip>>() {
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
                Toast.makeText(getContext(), "error to get trip list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

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
                Toast.makeText(getContext(), "error to get trip list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void showFilterDialog() {

        //Init Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_waiting_trip, null);

        final Switch filterByCity = dialogView.findViewById(R.id.filter_by_city_switch);
        final Switch filterByDistance = dialogView.findViewById(R.id.filter_by_distance_switch);
        final FluidSlider fluidSliderDistance = dialogView.findViewById(R.id.distance_fluid_slider);
        final EditText cityNameEditText = dialogView.findViewById(R.id.filter_by_city_edit_text);
        final LinearLayout linearLayourCity = dialogView.findViewById(R.id.filter_by_city_linear_layout);
        final int minDistance = 5;
        final int maxDistance = 600;
        final int totalDisatance = maxDistance - minDistance;

        fluidSliderDistance.setStartText(String.valueOf(minDistance));
        fluidSliderDistance.setEndText(String.valueOf(maxDistance));
        fluidSliderDistance.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float pos) {
                final String value = String.valueOf((int) (minDistance + totalDisatance * pos));
                fluidSliderDistance.setBubbleText(value + " km");
                return Unit.INSTANCE;
            }
        });


        //Restore State
        if (switchByDistanceState) {
            filterByDistance.setChecked(true);
            fluidSliderDistance.setVisibility(View.VISIBLE);
        } else if (switchByCityState) {
            filterByCity.setChecked(true);
            linearLayourCity.setVisibility(View.VISIBLE);
        }

        filterByDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchByDistanceState = isChecked;
                if (isChecked) {
                    fluidSliderDistance.setVisibility(View.VISIBLE);
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
        alertDialog.setTitle("Filters");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchByDistanceState) {
                    int distance = (int) (minDistance + totalDisatance * fluidSliderDistance.getPosition());
                    applyFilterByDistance(distance);
                } else if (switchByCityState) {
                    if (cityNameEditText != null && !TextUtils.isEmpty(cityNameEditText.getText()))
                        applyFilterByCity(cityNameEditText.getText().toString());
                }


            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.show();
    }

}

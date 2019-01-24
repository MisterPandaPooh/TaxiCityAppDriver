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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ramotion.fluidslider.FluidSlider;

import java.util.Date;
import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.HistoryTripAdapter;
import example.com.taxicityappdriver.controller.WaitingTripAdapter;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.entities.Trip;
import example.com.taxicityappdriver.model.helpers.Helpers;
import example.com.taxicityappdriver.model.interfaces.NotifyDataChange;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HistoryTripFragment extends Fragment {
    public final static BackEnd db = BackEndFactory.getInstance();
    private final String TAG = "WaitingTripsFragment";

    //Notified list of trip items.
    private List<Trip> items;

    //View fields
    private RecyclerView recyclerView; //RecyclerView instance.
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout instance.
    private FloatingActionButton fab; //FloatingActionButton instance to activate filters.

    //Saving state filter;
    private boolean filterBeforeDateSwitchState;
    private boolean filterByPriceSwitchState;
    private Date dateFilterValue;
    private int priceFilterValue;

    public HistoryTripFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind View Reference
        View view = inflater.inflate(R.layout.fragment_waiting_trips, container, false);
        recyclerView = view.findViewById(R.id.mainRecycleView);
        swipeRefreshLayout = view.findViewById(R.id.fragment_main_swipe_container);
        fab = view.findViewById(R.id.fab);

        //Refresh Layout Listener
        final HistoryTripFragment MyFragment = this;
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
        db.notifyToTripListFinished(new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    HistoryTripAdapter adapter = new HistoryTripAdapter(items);
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
     * Apply filter before the date
     *
     * @param date The date to filter
     */
    private void applyFilterBeforeDate(Date date) {

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
        db.notifyToTripListBeforeDate(date, new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    HistoryTripAdapter adapter = new HistoryTripAdapter(items);
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
     * Apply filter by trip price.
     *
     * @param price
     */

    private void applyFilterByPrice(int price) {
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
        db.notifyToTripListByAmounth(0, price, new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    HistoryTripAdapter adapter = new HistoryTripAdapter(items);
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
     * Init Filter Dialog to filter the view
     */
    private void showFilterDialog() {
        if (WaitingTripAdapter.isBusyDriver()) {
            Toast.makeText(getContext(), getString(R.string.busy_driver_not_quit_msg), Toast.LENGTH_SHORT).show();
            return;
        }

        //Init Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_history_trip, null);


        //Bind View
        final Switch filterBeforeDate = dialogView.findViewById(R.id.filter_by_date_switch);
        final Switch filterByPrice = dialogView.findViewById(R.id.filter_by_price_switch);
        final FluidSlider fluidSliderPrice = dialogView.findViewById(R.id.price_fluid_slider);
        final LinearLayout linearLayourDate = dialogView.findViewById(R.id.filter_by_date_linear_layout);
        final DatePicker datePicker = dialogView.findViewById(R.id.filter_date_picker);
        final int min = 10;
        final int max = 555;
        final int totalDistance = max - min;

        fluidSliderPrice.setStartText(String.valueOf(min));
        fluidSliderPrice.setEndText(String.valueOf(max));
        fluidSliderPrice.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float pos) {
                final String value = String.valueOf((int) (min + totalDistance * pos));
                fluidSliderPrice.setBubbleText(value);
                return Unit.INSTANCE;
            }
        });


        //Restore State
        if (filterBeforeDateSwitchState) {
            filterBeforeDate.setChecked(true);
            linearLayourDate.setVisibility(View.VISIBLE);

        } else if (filterByPriceSwitchState) {
            filterByPrice.setChecked(true);
            fluidSliderPrice.setVisibility(View.VISIBLE);
        }

        filterBeforeDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterBeforeDateSwitchState = isChecked;
                if (isChecked) {
                    linearLayourDate.setVisibility(View.VISIBLE);
                    filterByPrice.setChecked(false);
                } else
                    linearLayourDate.setVisibility(View.GONE);
            }
        });

        filterByPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterByPriceSwitchState = isChecked;
                if (isChecked) {
                    fluidSliderPrice.setVisibility(View.VISIBLE);
                    filterBeforeDate.setChecked(false);
                } else
                    fluidSliderPrice.setVisibility(View.GONE);
            }
        });


        //Init UI View of the Dialog
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Filters");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.apply_filter_btn_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (filterByPriceSwitchState) {
                    priceFilterValue = (int) (min + totalDistance * fluidSliderPrice.getPosition());
                    applyFilterByPrice(priceFilterValue);
                } else if (filterBeforeDateSwitchState) {
                    dateFilterValue = Helpers.getDateFromDatePicker(datePicker);
                    applyFilterBeforeDate(dateFilterValue);
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

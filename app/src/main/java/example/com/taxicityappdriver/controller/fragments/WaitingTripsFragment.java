package example.com.taxicityappdriver.controller.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.WaitingTripAdapter;
import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.backend.NotifyDataChange;

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
                if (WaitingTripAdapter.isBusyDriver()) {
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

    /**
     * Initialisation of the RecyclerView.
     *
     * @param v
     */
    private void initRecyclerView(final View v) {

        //Init Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true); //Reverse Data
        linearLayoutManager.setReverseLayout(true); //Reverse Data
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        //Initialisation Of data
        db.notifyToTripListAll(new NotifyDataChange<List<Trip>>() {
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


    private void showFilterDialog() {

        //Init Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_waiting_trip, null);

        //Init UI View of the Dialog
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Filters");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.show();
    }

}

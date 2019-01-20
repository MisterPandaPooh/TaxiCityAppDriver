package example.com.taxicityappdriver.controller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.FoldingCellListAdapter;
import example.com.taxicityappdriver.controller.TripItemViewAdapter;
import example.com.taxicityappdriver.controller.WaitingTripAdapter;
import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.backend.NotifyDataChange;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingTripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Trip> items;
    public final static BackEnd db = BackEndFactory.getInstance();
    private WaitingTripAdapter adapter;
    private static Driver driver;
    int i = 0;
    private final String TAG = "WaitingTripsFragment";


    public WaitingTripsFragment() {
        // Required empty public constructor
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_trips, container, false);

        // get our list view
        theListView = view.findViewById(R.id.mainListView);

        final Driver driver = new Driver();
        driver.setFirstName("Jean");
        driver.setLastName("DelaVeine");

        // prepare elements to display

        adapter = new FoldingCellListAdapter(getContext(),items,driver);
        db.notifyToTripList(new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                items = obj;
                if (theListView.getAdapter() == null) {
                    Log.i(TAG, Integer.toString(items.size()));
                    theListView.setAdapter(new FoldingCellListAdapter(getActivity(), items, driver));
                    ((ArrayAdapter)theListView.getAdapter()).notifyDataSetChanged();
                    setListViewHeight(theListView);

                } else{
                    Log.i(TAG," ELSE :"+ Integer.toString(items.size()));
                    ((ArrayAdapter)theListView.getAdapter()).notifyDataSetChanged();
                    setListViewHeight(theListView);
                }

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "error to get trip list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        //theListView.setAdapter(new FoldingCellListAdapter(getActivity(), TripItemViewAdapter.getTestingList(), driver));

        //adapter = new FoldingCellListAdapter(getActivity(), items, driver);




        // set elements to adapter
        //theListView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                Log.i("michelle", "CLICK : " + i++);
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
        return view;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_trips, container, false);
        recyclerView = view.findViewById(R.id.mainListView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Driver driver = new Driver();
        driver.setFirstName("Jean");
        driver.setLastName("DelaVeine");
        WaitingTripAdapter.driver = driver;
        db.notifyToTripList(new NotifyDataChange<List<Trip>>() {
            @Override
            public void OnDataChanged(List<Trip> obj) {
                if (recyclerView.getAdapter() == null) {
                    items = obj;
                    if (WaitingTripViewHolder.driver == null)
                        WaitingTripViewHolder.driver = WaitingTripAdapter.driver;
                    recyclerView.setAdapter(new WaitingTripAdapter(items));
                } else
                    recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "error to get trip list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });


        return view;

    }

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView() {
        final Driver driver = new Driver();
        driver.setFirstName("Jean");
        driver.setLastName("DelaVeine");
        // 3.1 - Reset list
        this.items = WaitingTripViewHolder.getTestingList();
        // 3.2 - Create adapter passing the list of users
        this.adapter = new WaitingTripAdapter(items);
        WaitingTripAdapter.driver = driver;
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.stopNotifyToTripList();
    }


}

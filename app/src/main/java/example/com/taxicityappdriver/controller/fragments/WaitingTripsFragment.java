package example.com.taxicityappdriver.controller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.FoldingCellListAdapter;
import example.com.taxicityappdriver.controller.TripItemViewAdapter;
import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingTripsFragment extends Fragment {

    private ListView theListView;
    int i = 0;



    public WaitingTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_trips, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get our list view
        theListView = view.findViewById(R.id.mainListView);

        // prepare elements to display
        final ArrayList<Trip> items = TripItemViewAdapter.getTestingList();


        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        Driver driver = new Driver();
        driver.setFirstName("Jean");
        driver.setLastName("DelaVeine");

        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(getActivity(), items, driver);




        // set elements to adapter
        theListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

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
    }

}

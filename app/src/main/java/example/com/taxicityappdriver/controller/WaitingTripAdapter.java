package example.com.taxicityappdriver.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;

public class WaitingTripAdapter extends RecyclerView.Adapter<WaitingTripViewHolder> {

    private List<Trip> items;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    public static Driver driver;
    private final String TAG = "WaitingTripAdapter";
    public AdapterView.OnItemClickListener onItemClickListener;

    public WaitingTripAdapter(List<Trip> listTrips) {
        items = listTrips;


    }

    @NonNull
    @Override
    public WaitingTripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cell_trip, viewGroup, false);
        if(WaitingTripViewHolder.driver==null)
            WaitingTripViewHolder.driver = this.driver;
        return new WaitingTripViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingTripViewHolder viewHolder, final int position) {
        viewHolder.setTrip(items.get(position));

        final FoldingCell finalCell = (FoldingCell) viewHolder.itemView;
        viewHolder.setFoldListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCell.toggle(false);
                registerToggle(position);
            }
        });
        viewHolder.initItems();
        viewHolder.tripIdTitleView.setText("Trip #" + (position + 1));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }


}

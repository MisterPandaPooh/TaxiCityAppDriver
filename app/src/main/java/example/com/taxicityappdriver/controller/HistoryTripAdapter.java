package example.com.taxicityappdriver.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.model.entities.Trip;

public class HistoryTripAdapter extends RecyclerView.Adapter<HistoryTripViewHolder> {

    private List<Trip> items;//List of all trip items.

    private final static String TAG = "HistoryTripAdapter";

    public HistoryTripAdapter(List<Trip> listTrips) {
        items = listTrips;
    }

    @NonNull
    @Override
    public HistoryTripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cell_trip, viewGroup, false);

        //InitWaitingTripViewHolder
        HistoryTripViewHolder.setContext(context);
        return new HistoryTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryTripViewHolder viewHolder, int position) {

        viewHolder.setTrip(items.get(position));

        //Set the Fold Listener (Maybe change position)
        final FoldingCell finalCell = (FoldingCell) viewHolder.itemView;
        viewHolder.setFoldListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalCell.toggle(false);



            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewHolder.itemView.getContext(),"euueue",Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.init();
    }




    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

package example.com.taxicityappdriver.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;

public class FoldingCellListAdapter extends ArrayAdapter<Trip> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Driver driver;
    private final String TAG = "FoldingCellListAdapter";

    public FoldingCellListAdapter(Context context, List<Trip> objects, Driver driver) {
        super(context, 0, objects);
        this.driver = driver;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Trip item = getItem(position);
        Log.i(TAG, item.getDestinationAddress());
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        TripItemViewAdapter.context = getContext();
        TripItemViewAdapter.driver = this.driver;
        final TripItemViewAdapter viewHolder;

        if (cell == null) {
            viewHolder = new TripItemViewAdapter(item);
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell_trip, parent, false);
            // binding view parts to view holder
            viewHolder.startingHourTitleView = cell.findViewById(R.id.datetime_trip_title_view);
            viewHolder.destinationAddressTitleView = cell.findViewById(R.id.trip_destination_title_view);
            viewHolder.distanceFromYouTitleView = cell.findViewById(R.id.distance_from_you_title_view);
            viewHolder.tripDistanceTitleView = cell.findViewById(R.id.trip_distance_title_view);
            viewHolder.startingHour = cell.findViewById(R.id.datetime_trip);
            viewHolder.customerButton = cell.findViewById(R.id.customer_btn);
            viewHolder.sourceAddress = cell.findViewById(R.id.source_address);
            viewHolder.destinationAddress = cell.findViewById(R.id.trip_destination);
            viewHolder.distanceFromYou = cell.findViewById(R.id.distance_from_you);
            viewHolder.tripDistance = cell.findViewById(R.id.trip_distance);
            viewHolder.requestTripButton = cell.findViewById(R.id.request_trip_btn);
            viewHolder.smsButton = cell.findViewById(R.id.sms_btn);
            viewHolder.callButton = cell.findViewById(R.id.call_btn);
            viewHolder.emailButton = cell.findViewById(R.id.mail_btn);
            viewHolder.imgHeader = cell.findViewById(R.id.header_img);
            viewHolder.tripIdTitleView = cell.findViewById(R.id.trip_title_id);
            //set driver
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (TripItemViewAdapter) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder

        final FoldingCell finalCell = cell;
        viewHolder.setFoldListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCell.toggle(false);
                registerToggle(position);
            }
        });


        viewHolder.tripIdTitleView.setText("Trip #"+(position+1));
        viewHolder.initItem();

        return cell;
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

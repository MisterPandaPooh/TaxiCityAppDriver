package example.com.taxicityappdriver.controller;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.model.helpers.TripHelper;

/**
 * Inherit of the WaitingTrip viewHolder.
 */
public class HistoryTripViewHolder extends WaitingTripViewHolder {

    public HistoryTripViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected boolean wasRequestByYou() {
        return true; //Override method
    }

    @Override
    protected void initUIContent() {
        super.initUIContent();
        new CalculateDistanceAsyncTask().doInBackground(null);
        updateUIHistory();

    }

    /**
     * Update to the history trips viewHolder;
     */
    private void updateUIHistory() {

        //????
        final FoldingCell cell = (FoldingCell) itemView;
        cell.post(new Runnable() {
            @Override
            public void run() {
                cell.toggle(true);

            }
        });

        //Change Visibility for Unused Views
        requestTripButton.setVisibility(View.GONE);
        endTripButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        dividerRequested.setVisibility(View.GONE);
        smsButton.setVisibility(View.GONE);
        emailButton.setVisibility(View.GONE);
        //restore fold listeners
        imgHeader.setOnClickListener(getFoldListener());
        itemView.setOnClickListener(getFoldListener());

        //Change call Button design
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                3
        );
        callButton.setLayoutParams(param);
        callButton.setText("Call " + trip.getCustomerName().toUpperCase()+ " ?");
        callButton.setAllCaps(false);


        //Change distance from you
        ((TextView) itemView.findViewById(R.id.distance_from_you_lbl)).setText(context.getString(R.string.price_of_trip_lbl));
        ((TextView) itemView.findViewById(R.id.distance_from_you_lbl_title_view)).setText(context.getString(R.string.price_of_trip_lbl));

        distanceFromYouTitleView.setText((int)TripHelper.calculatePrice(tripDistanceInKm) + " $");
        distanceFromYou.setText(distanceFromYouTitleView.getText());

    }



    //Prevent Action on Trip => Override important methods
    @Override
    protected View.OnClickListener onRequestTripClickListener() {
        return null;
    }

    @Override
    protected View.OnClickListener onCancelClickListener() {
        return null;
    }

    @Override
    protected View.OnClickListener onEndTripClickListener() {
        return null;
    }
}

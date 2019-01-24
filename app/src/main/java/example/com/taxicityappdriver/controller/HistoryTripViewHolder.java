package example.com.taxicityappdriver.controller;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.model.helpers.TripHelper;

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
        adapteUIHistory();

    }

    private void adapteUIHistory() {
        final FoldingCell cell = (FoldingCell) itemView;
        cell.post(new Runnable() {
            @Override
            public void run() {
                cell.toggle(true);

            }
        });
        requestTripButton.setVisibility(View.GONE);
        endTripButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        dividerRequested.setVisibility(View.GONE);
        smsButton.setVisibility(View.GONE);
        emailButton.setVisibility(View.GONE);
        imgHeader.setOnClickListener(getFoldListener());
        itemView.setOnClickListener(getFoldListener());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                3
        );
        callButton.setLayoutParams(param);
        callButton.setText("Call " + trip.getCustomerName().toUpperCase()+ " ?");
        callButton.setAllCaps(false);


        //Change distance from you
        ((TextView) itemView.findViewById(R.id.distance_from_you_lbl)).setText("Price of the Trip");
        ((TextView) itemView.findViewById(R.id.distance_from_you_lbl_title_view)).setText("Price of the Trip");
        distanceFromYouTitleView.setText((int)TripHelper.calculatePrice(tripDistanceInKm) + " $");
        distanceFromYou.setText(distanceFromYouTitleView.getText());

    }

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

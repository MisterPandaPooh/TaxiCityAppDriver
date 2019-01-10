package example.com.taxicityappdriver.controller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ramotion.foldingcell.FoldingCell;

import example.com.taxicityappdriver.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripItemFragment extends Fragment {


    public TripItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FoldingCell fc = (FoldingCell) view.findViewById(R.id.folding_cell);

        // attach click listener to fold btn
        final View v = view.findViewById(R.id.cell_title_view);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });

        final View v1 = view.findViewById(R.id.header_img);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
    }
}

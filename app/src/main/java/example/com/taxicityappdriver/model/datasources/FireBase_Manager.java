package example.com.taxicityappdriver.model.datasources;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;

public class FireBase_Manager implements BackEnd<String> {
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference refTrips = db.getReference("Trips");
    private final String TAG = "firebaseManager";


    public void addTrip(final Trip trip, final ActionCallBack<String> action) {
        String idTrip = trip.getKey();

        //Keep the same key when updating
        if (idTrip == null)
            idTrip = refTrips.push().getKey();
        else
            trip.setKey(null);


        final String finalIdTrip = idTrip;
        refTrips.child(idTrip).setValue(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(finalIdTrip);
                action.onProgress("Adding trip in progress...", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("Failed to add the trips...", 100);
            }
        });
    }


    public void removeTrip(final String key, final ActionCallBack<String> action) {

        refTrips.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Trip value = dataSnapshot.getValue(Trip.class);

                if (value == null) {
                    action.onFailure(new Exception("We can't find the request trip"));
                } else {
                    refTrips.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(key);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            action.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                action.onFailure(databaseError.toException());
            }
        });

    }


    public void updateTrip(final Trip toUpdate, final ActionCallBack<String> action) {
        final String key = toUpdate.getKey();

        Log.e(TAG, "Update Started");
        removeTrip(key, new ActionCallBack<String>() {
            @Override
            public void onSuccess(String obj) {
                addTrip(toUpdate, action);
            }

            @Override
            public void onFailure(Exception exception) {
                action.onFailure(exception);
            }

            @Override
            public void onProgress(String status, double percent) {
                action.onProgress(status, percent);
            }
        });
    }

    @Override
    public void getTrip(final String key, final ActionCallBack<Trip> action) {

        final Trip[] trip = {null};
        refTrips.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trip[0] = dataSnapshot.getValue(Trip.class);

                if (trip[0] == null) {

                    action.onFailure(new Exception("We can't find the Trip"));
                } else {
                    action.onSuccess(trip[0]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                action.onFailure(databaseError.toException());
            }
        });

    }

}


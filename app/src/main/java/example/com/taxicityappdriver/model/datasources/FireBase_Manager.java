package example.com.taxicityappdriver.model.datasources;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;
import example.com.taxicityappdriver.model.backend.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;

public class FireBase_Manager implements BackEnd<String> {
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference refTrips = db.getReference("Trips");
    private static DatabaseReference refDriver = db.getReference("Drivers");
    private final String TAG = "firebaseManager";
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();


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

    @Override
    public void addDriver(Driver driver, final ActionCallBack<String> action) {
        String email = driver.getEmail();


        final String finalEmailDriver = email;
        //Test if the driver already exist
        getDriver(finalEmailDriver, new ActionCallBack<Driver>() {
            @Override
            public void onSuccess(Driver obj) {
                action.onFailure(new Exception("Driver already Exist !"));
            }

            @Override
            public void onFailure(Exception exception) {
                //If the driver don't exits
                refDriver.child(finalEmailDriver).setValue(finalEmailDriver).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.onSuccess(finalEmailDriver);
                        action.onProgress("Adding driver in progress...", 100);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        action.onFailure(e);
                        action.onProgress("Failed to add the driver...", 100);
                    }
                });

            }

            @Override
            public void onProgress(String status, double percent) {

            }
        });

    }

    @Override
    public void removeDriver(final String email, final ActionCallBack<String> action) {

        refDriver.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Driver value = dataSnapshot.getValue(Driver.class);

                if (value == null) {
                    action.onFailure(new Exception("We can't find the request trip"));
                } else {
                    refTrips.child(email).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(email);
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

    //By ID
    @Override
    public void getDriver(final String email, final ActionCallBack<Driver> action) {
        final Driver[] drivers = {null};
        refDriver.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drivers[0] = dataSnapshot.getValue(Driver.class);

                if (drivers[0] == null) {

                    action.onFailure(new Exception("We can't find the Driver"));
                } else {
                    drivers[0].setEmail(email);
                    action.onSuccess(drivers[0]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                action.onFailure(databaseError.toException());
            }
        });

    }

    @Override
    public void updateDriver(final Driver toUpdate, final ActionCallBack<String> action) {
        final String email = toUpdate.getEmail();

        Log.e(TAG, "Update Started");
        removeTrip(email, new ActionCallBack<String>() {
            @Override
            public void onSuccess(String obj) {
                addDriver(toUpdate, action);
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
    public void signUp(String email, String password, final ActionCallBack<Object> action) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    action.onSuccess(mAuth.getCurrentUser());
                else
                    action.onFailure(task.getException());

            }
        });

    }

    @Override
    public void signIn(String email, String password, final ActionCallBack<Object> action) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    action.onSuccess(mAuth.getCurrentUser());
                else
                    action.onFailure(task.getException());
            }
        });

    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void forgotPassword(String email, final ActionCallBack<String> action) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            action.onSuccess("Email sent.");
                        } else
                            action.onFailure(task.getException());
                    }
                });
    }

    @Override
    public boolean isSigned() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public Object getCurrentUser() {
        return mAuth.getCurrentUser();
    }

}


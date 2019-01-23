package example.com.taxicityappdriver.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import example.com.taxicityappdriver.controller.WaitingTripViewHolder;
import example.com.taxicityappdriver.model.entities.Trip;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

import static example.com.taxicityappdriver.controller.WaitingTripAdapter.isBusyDriver;

//TODO A REVOIR
public class ClosingService extends Service {

    private final static String TAG = "ClosingService";
    private final static BackEnd db = BackEndFactory.getInstance();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        resetCurrentTrip();


    }

    private void resetCurrentTrip() {
        if (isBusyDriver()) {
            Log.i(TAG, "DRIVER BUSY " + WaitingTripViewHolder.getBusyKey());
            db.getTrip(WaitingTripViewHolder.getBusyKey(), new ActionCallBack<Trip>() {
                @Override
                public void onSuccess(Trip obj) {
                    obj.setDriverEmail(null);
                    obj.setStatusAsEnum(Trip.TripStatus.AVAILABLE);
                    Log.i(TAG, "SUCESS DESTROY UPDATE " + obj.getKey());
                    db.updateTrip(obj, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object obj) {
                            Log.i(TAG, "onSuccess: REUPDATE TRIP");
                            // Destroy the service
                            stopSelf();

                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Log.i(TAG, "onFailure: " + exception.getMessage());
                        }

                        @Override
                        public void onProgress(String status, double percent) {

                        }
                    });
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i(TAG, "onFailure: " + exception.getMessage());


                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });
        }

    }
}

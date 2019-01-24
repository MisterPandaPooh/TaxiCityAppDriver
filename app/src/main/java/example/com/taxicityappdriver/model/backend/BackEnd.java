package example.com.taxicityappdriver.model.backend;

import android.net.Uri;

import java.net.URI;
import java.util.Date;
import java.util.List;

import example.com.taxicityappdriver.model.entities.Driver;
import example.com.taxicityappdriver.model.entities.Trip;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.interfaces.NotifyDataChange;

public interface BackEnd<T> {
    //trip
    void addTrip(final Trip trip, final ActionCallBack<T> action);

    void removeTrip(final String key, final ActionCallBack<T> action);

    void getTrip(final String key, final ActionCallBack<Trip> action);

    void updateTrip(final Trip toUpdate, final ActionCallBack<T> action);


    //Driver
    void addDriver(final Driver driver, final ActionCallBack<T> action);

    void removeDriver(final String key, final ActionCallBack<T> action);

    void getDriver(final String key, final ActionCallBack<Driver> action);

    void updateDriver(final Driver toUpdate, final ActionCallBack<T> action);


    //Auth
    void signUp(String email, String password, final ActionCallBack<Object> action);

    void signIn(String email, String password, final ActionCallBack<Object> action);

    void signOut();

    void forgotPassword(String email, final ActionCallBack<T> action);

    boolean isSigned();

    Driver getCurrentDriver();

    void deleteCurrentUser();

    void changeUserPassword(String newPassword, ActionCallBack<T> action);

    public void updateProfilePicture(Uri uri, ActionCallBack<T> action);

    public Uri getUserProfilePicture();

    void notifyToTripListAll(final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListWaiting(final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListBeforeDate(final Date date, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListByAmounth(final double min, final double max, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListWaitingByDistance(final int distanceInKm, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListWaitingByCity(final String destinationCity, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListByDriver(final String driverEmail, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListFinished(final NotifyDataChange<List<Trip>> notifyDataChange);

    void stopNotifyToTripList();


}

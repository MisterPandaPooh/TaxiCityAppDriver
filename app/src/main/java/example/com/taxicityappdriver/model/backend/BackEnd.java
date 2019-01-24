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

    /* ************ TRIP ************ */

    /**
     * Add trip to the Database
     *
     * @param trip   the trip to Add.
     * @param action the Action CallBack
     */
    void addTrip(final Trip trip, final ActionCallBack<T> action);

    /**
     * Remove Trip to the dateBase.
     *
     * @param key    key of the trip to remove
     * @param action the Action CallBack
     */
    void removeTrip(final String key, final ActionCallBack<T> action);

    /**
     * Get Trip from DataBase
     *
     * @param key    The key of the trip to get
     * @param action The Action CallBack
     */
    void getTrip(final String key, final ActionCallBack<Trip> action);


    /**
     * Update trip to the Database
     *
     * @param toUpdate the trip to update.
     * @param action   the Action CallBack
     */
    void updateTrip(final Trip toUpdate, final ActionCallBack<T> action);


    /* ************ TRIP ************ */

    /**
     * Add driver to the Database
     *
     * @param driver the driver to Add.
     * @param action the Action CallBack
     */

    void addDriver(final Driver driver, final ActionCallBack<T> action);

    /**
     * Remove Driver to the dateBase.
     *
     * @param key    key of the driver to remove
     * @param action the Action CallBack
     */
    void removeDriver(final String key, final ActionCallBack<T> action);

    /**
     * Get Driver from DataBase
     *
     * @param key    The key of the driver to get
     * @param action The Action CallBack
     */
    void getDriver(final String key, final ActionCallBack<Driver> action);


    /**
     * Update Driver to the Database
     *
     * @param toUpdate the driver to update.
     * @param action   the Action CallBack
     */
    void updateDriver(final Driver toUpdate, final ActionCallBack<T> action);


    /* ************ AUTHENTICATION  ************ */

    /**
     * SignUp Driver and get the driver
     *
     * @param email    Driver email
     * @param password Driver password
     * @param action   The Action CallBack.
     */
    void signUp(String email, String password, final ActionCallBack<Object> action);


    /**
     * SignIN Driver and get the driver
     *
     * @param email    Driver email.
     * @param password Driver password.
     * @param action   The Action CallBack.
     */
    void signIn(String email, String password, final ActionCallBack<Object> action);


    /**
     * Send forgot email password
     *
     * @param email  Driver Email
     * @param action The Action CallBack.
     */
    void forgotPassword(String email, final ActionCallBack<T> action);

    /**
     * SignOut the user.
     */
    void signOut();

    /**
     * Check if the user is signed
     *
     * @return true if signed
     */
    boolean isSigned();

    /**
     * Get the current Driver (signed)
     *
     * @return The current Driver
     */
    Driver getCurrentDriver();

    /**
     * Delete Current user (authentication)
     */
    void deleteCurrentUser();

    /**
     * Change the password for the Driver
     *
     * @param newPassword The new password.
     * @param action      The Action callBack.
     */
    void changeUserPassword(String newPassword, ActionCallBack<T> action);

    /**
     * Update The profile picture to the storage datSource.
     *
     * @param uri    The URI of the image.
     * @param action The action callBack.
     */
    public void updateProfilePicture(Uri uri, ActionCallBack<T> action);

    /**
     * @return Current User Profile Picture
     */
    public Uri getUserProfilePicture();


    /* ************ ONCHANGE NOTIFY LIST  ************ */

    /**
     * List of All trips in the Database.
     *
     * @param notifyDataChange The callBack.
     */
    void notifyToTripListAll(final NotifyDataChange<List<Trip>> notifyDataChange);

    /**
     * List of All WAITING trips in the Database.
     * (Note: Contains Also current trip.)
     *
     * @param notifyDataChange The callBack.
     */
    void notifyToTripListWaiting(final NotifyDataChange<List<Trip>> notifyDataChange);

    /**
     * List of All WAITING trips < to a certain distance in the Database.
     *
     * @param distanceInKm     The MAX distance of the trips.
     * @param notifyDataChange The CallBack.
     */
    void notifyToTripListWaitingByDistance(final int distanceInKm, final NotifyDataChange<List<Trip>> notifyDataChange);

    void notifyToTripListWaitingByCity(final String destinationCity, final NotifyDataChange<List<Trip>> notifyDataChange);


    /**
     * List of All WAITING trips in the Database.
     * (Note: NOT Contains current trip.)
     *
     * @param notifyDataChange The callBack.
     */
    void notifyToTripListFinished(final NotifyDataChange<List<Trip>> notifyDataChange);

    /**
     * List of all FINISHED trips before the date.
     *
     * @param date             The date.
     * @param notifyDataChange The callBack.
     */
    void notifyToTripListBeforeDate(final Date date, final NotifyDataChange<List<Trip>> notifyDataChange);

    /**
     * List of all FINISHED trips between min and max trip price.
     *
     * @param min              Min trip price.
     * @param max              Max trip price.
     * @param notifyDataChange The callBack.
     */
    void notifyToTripListByAmounth(final double min, final double max, final NotifyDataChange<List<Trip>> notifyDataChange);


    /**
     * List of ALL trip by Driver email.
     *
     * @param driverEmail      Driver email.
     * @param notifyDataChange The CallBack.
     */
    void notifyToTripListByDriver(final String driverEmail, final NotifyDataChange<List<Trip>> notifyDataChange);


    /**
     * Stop Notify List(s)
     */
    void stopNotifyToTripList();


}

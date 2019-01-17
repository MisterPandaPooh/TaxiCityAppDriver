package example.com.taxicityappdriver.model.backend;

import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;

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

}

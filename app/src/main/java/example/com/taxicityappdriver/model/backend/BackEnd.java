package example.com.taxicityappdriver.model.backend;

import example.com.taxicityappdriver.entities.Trip;

public interface BackEnd<T> {
    void addTrip(final Trip trip, final ActionCallBack<T> action);

    void removeTrip(final String key, final ActionCallBack<T> action);

    void getTrip(final String key, final ActionCallBack<Trip> action);

    void updateTrip(final Trip toUpdate, final ActionCallBack<T> action);

}

package example.com.taxicityappdriver.model.helpers;

import android.location.Location;

import example.com.taxicityappdriver.model.entities.Driver;
import example.com.taxicityappdriver.model.entities.Trip;

public class LocationHelper {
    public static float calculTripDistance(Trip trip) {
        float[] result = new float[1];
        Location.distanceBetween(trip.getSourceLatitude(), trip.getSourceLongitude(), trip.getDestinationLatitude(), trip.getDestinationLongitude(), result);
        return result[0];
    }

    public static float calculDistanceFromYou(Trip trip, Driver driver) {
        float[] result = new float[1];
        Location.distanceBetween(trip.getSourceLatitude(), trip.getSourceLongitude(), driver.getCurrentLocationLat(), driver.getCurrentLocationLong(), result);
        return result[0];
    }
}


package example.com.taxicityappdriver.model.helpers;

import android.location.Location;

import example.com.taxicityappdriver.model.entities.Driver;
import example.com.taxicityappdriver.model.entities.Trip;

public class TripHelper {

    private final static double PRICE_BY_KM = 2.0;
    private final static double MIN_PRICE_TRIP = 11.90;

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


    public static double calculatePrice(float tripDistanceInKm) {
        double price = PRICE_BY_KM * tripDistanceInKm;
        return (price < MIN_PRICE_TRIP) ? MIN_PRICE_TRIP : price;
    }
}


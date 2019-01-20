package example.com.taxicityappdriver.entities;

import com.google.firebase.database.Exclude;

import java.util.Date;

import example.com.taxicityappdriver.model.helpers.Helpers;

public class Trip {




    public enum TripStatus {AVAILABLE, IN_PROGRESS, FINISHED}

    @Exclude
    private String key;

    private String startingHour; //Not firebase

    private String endingHour; //Not Firebase

    private String customerName;

    private String customerPhone;

    private String customerEmail;


    private TripStatus status;

    private double sourceLongitude;

    private double sourceLatitude;

    private String sourceAddress;

    private double destinationLongitude;

    private double destinationLatitude;

    private String destinationAddress;

    private int driverID;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(String startingHour) {
        this.startingHour = startingHour;
    }

    public String getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(String endingHour) {
        this.endingHour = endingHour;
    }


    @Exclude
    public Date getStartingHourAsDate() {
        return new Date(startingHour);
    }
    @Exclude
    public Date getEndingHourAsDate() {
        return new Date(endingHour);
    }




    @Exclude
    public TripStatus getStatusAsEnum() {
        return status;
    }

    @Exclude
    public void setStatusAsEnum(TripStatus status) {
        this.status = status;
    }

    //Include enum in firebase
    public String getStatus() {
        if (status == null) {
            return null;
        } else {
            return status.name();
        }
    }

    public void setStatus(String statusString) {
        if (statusString == null) {
            status = null;
        } else {
            this.status = TripStatus.valueOf(statusString);
        }
    }


}


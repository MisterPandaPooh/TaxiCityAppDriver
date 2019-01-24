package example.com.taxicityappdriver.model.entities;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Driver {

    @Exclude
    protected String email; //KEY WILL BE EMAIL

    protected long idNumber;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected long creditCardNumber;
    protected long cVV;
    protected String expireDateCreditCard;
    protected String createdDate; // Date when the driver is singUp.
    protected double currentLocationLat;
    protected double currentLocationLong;
    protected boolean isBusy;
    protected int totalTripsCounter;
    protected double totalSumOfTrips; //Total cost of all the driver trips


    @Exclude
    public String getEmail() {
        return email;
    }

    @Exclude
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public long getcVV() {
        return cVV;
    }

    public void setcVV(long cVV) {
        this.cVV = cVV;
    }

    public String getExpireDateCreditCard() {
        return expireDateCreditCard;
    }

    public void setExpireDateCreditCard(String expireDateCreditCard) {
        this.expireDateCreditCard = expireDateCreditCard;
    }


    public double getCurrentLocationLat() {
        return currentLocationLat;
    }

    public void setCurrentLocationLat(double currentLocationLat) {
        this.currentLocationLat = currentLocationLat;
    }

    public double getCurrentLocationLong() {
        return currentLocationLong;
    }

    public void setCurrentLocationLong(double currentLocationLong) {
        this.currentLocationLong = currentLocationLong;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    @Exclude
    public Date getCreatedDateAsDate() {
        return new Date(createdDate);
    }


    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public int getTotalTripsCounter() {
        return totalTripsCounter;
    }

    public void setTotalTripsCounter(int totalTripsCounter) {
        this.totalTripsCounter = totalTripsCounter;
    }

    public double getTotalSumOfTrips() {
        return totalSumOfTrips;
    }

    public void setTotalSumOfTrips(double totalSumOfTrips) {
        this.totalSumOfTrips = totalSumOfTrips;
    }
}

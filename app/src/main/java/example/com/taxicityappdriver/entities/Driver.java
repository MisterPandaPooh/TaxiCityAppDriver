package example.com.taxicityappdriver.entities;

import com.google.firebase.database.Exclude;

import java.net.URL;
import java.util.Date;

import example.com.taxicityappdriver.model.helpers.Helpers;

public class Driver {

    @Exclude
    protected String email;

    protected long idNumber;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber; //long ?
    protected long creditCardNumber;
    protected long cVV;
    protected String expireDateCreditCard;
    protected Date createdDate;
    protected double currentLocationLat;
    protected double currentLocationLong;
    protected boolean isBusy;
    protected int checkAroundAroundKm;

    //KEY WILL BE EMAIL


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
        if (createdDate == null)
            return null;
        return Helpers.ISO_8601_FORMAT.format(createdDate);
    }

    public void setCreatedDate(String createdDate) {
        if (createdDate == null)
            this.createdDate = null;
        else
            this.createdDate = new Date(createdDate);
    }

    public int getCheckAroundAroundKm() {
        return checkAroundAroundKm;
    }

    public void setCheckAroundAroundKm(int checkAroundAroundKm) {
        this.checkAroundAroundKm = checkAroundAroundKm;
    }

    @Exclude
    public Date getCreatedDateAsDate() {
        return createdDate;
    }

    @Exclude
    public void setCreatedDateAsDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}

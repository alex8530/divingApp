package com.diverapp.diverapp;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip {

    String name;
    int type ;
    String tripClass;
    String description;
    String bankAccount;
    String sartDate;
    String endDate;

    int activion;
//    List<Double> location;
    int ticketNumber;
    int minummTicket;

    String locationName;
    List<String> imagesURL;
    String uid;
    String providerId;
    int price;
    //    String imageUrl;

    List<Uri> uriList;
private String provider_name,provider_bio;

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_bio() {
        return provider_bio;
    }

    public void setProvider_bio(String provider_bio) {
        this.provider_bio = provider_bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTripClass() {
        return tripClass;
    }

    public void setTripClass(String tripClass) {
        this.tripClass = tripClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSartDate() {
        return sartDate;
    }

    public void setSartDate(String sartDate) {
        this.sartDate = sartDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getActivion() {
        return activion;
    }

    public void setActivion(int activion) {
        this.activion = activion;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getMinummTicket() {
        return minummTicket;
    }

    public void setMinummTicket(int minummTicket) {
        this.minummTicket = minummTicket;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<String> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(List<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Uri> getUriList() {
        return uriList;
    }

    public void setUriList(List<Uri> uriList) {
        this.uriList = uriList;
    }
}

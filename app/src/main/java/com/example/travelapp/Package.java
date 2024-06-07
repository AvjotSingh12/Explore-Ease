package com.example.travelapp;

import com.google.gson.annotations.SerializedName;

public class Package {
    @SerializedName("Package Name")
    private String packageName;
    @SerializedName("Hotel Details")
    private String hotelDetails;
    private String airline;
    @SerializedName("Itinerary")
    private String itinerary;
    @SerializedName("Places Covered")
    private String placesCovered;
    @SerializedName("Sightseeing Places Covered")
    private String sightseeingPlacesCovered;
    @SerializedName("Start City")
    private String startCity;
    @SerializedName("Per Person Price")
    private String perpersonprice;

    public Package(String packageName, String hotelDetails, String airline, String itinerary, String placesCovered, String sightseeingPlacesCovered, String startCity, String perpersonprice) {
        this.packageName = packageName;
        this.hotelDetails = hotelDetails;
        this.airline = airline;
        this.itinerary = itinerary;
        this.placesCovered = placesCovered;
        this.sightseeingPlacesCovered = sightseeingPlacesCovered;
        this.startCity = startCity;
        this.perpersonprice = perpersonprice;
    }

    public String getPerpersonprice() {
        return perpersonprice;
    }

    public void setPerpersonprice(String perpersonprice) {
        this.perpersonprice = perpersonprice;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(String hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getPlacesCovered() {
        return placesCovered;
    }

    public void setPlacesCovered(String placesCovered) {
        this.placesCovered = placesCovered;
    }

    public String getSightseeingPlacesCovered() {
        return sightseeingPlacesCovered;
    }

    public void setSightseeingPlacesCovered(String sightseeingPlacesCovered) {
        this.sightseeingPlacesCovered = sightseeingPlacesCovered;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }
    public double getHotelRating() {
        if (hotelDetails != null && hotelDetails.contains(":")) {
            String[] parts = hotelDetails.split(":");
            if (parts.length > 1) {
                try {
                    return Double.parseDouble(parts[1]);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }
}

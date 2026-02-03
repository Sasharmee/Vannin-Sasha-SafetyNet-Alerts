package com.openclassrooms.safetynet_alerts.model;

public class FirestationModel {

    private String address;

    private String station;

    public FirestationModel() {
       //constructeur vide pour Jackson
    }

    public FirestationModel(String address, String station){
        this.address = address;
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}

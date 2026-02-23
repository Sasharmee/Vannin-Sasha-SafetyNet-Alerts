package com.openclassrooms.safetynet_alerts.model;

/**
 * Modèle représentant l'association entre une adresse couverte et un numéro de caserne de pompiers.
 */

public class FirestationModel {

    private String address;

    private String station;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public FirestationModel() {
    }

    /**
     * Constructeur permettant de créer une association entre une adresse et une caserne.
     *
     * @param address adresse couverte par la caserne
     * @param station numéro de la caserne
     */
    public FirestationModel(String address, String station) {
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

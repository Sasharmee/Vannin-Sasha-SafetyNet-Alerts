package com.openclassrooms.safetynet_alerts.model;

import java.util.Objects;

/**
 * Modèle représentant l'association entre une personne et ses données personnelles.
 */

public class PersonModel {

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String zip;

    private String phone;

    private String email;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public PersonModel() {
    }

    /**
     * Constructeur permettant de créer une association entre une personne et ses données personnelles.
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de la personne
     * @param address   adresse de la personne
     * @param city      ville de résidence de la personne
     * @param zip       code postal de la personne
     * @param phone     numéro de téléphone de la personne
     * @param email     adresse mail de la personne
     */
    public PersonModel(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonModel)) return false;
        PersonModel p = (PersonModel) o;
        return firstName.equals(p.firstName) && lastName.equals(p.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}

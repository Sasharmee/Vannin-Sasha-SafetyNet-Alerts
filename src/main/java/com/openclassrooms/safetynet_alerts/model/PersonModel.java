package com.openclassrooms.safetynet_alerts.model;

import java.util.Objects;

//point important pour pouvoir delete on va utiliser combinaison prénom+nom

public class PersonModel {

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String zip;

    private String phone;

    private String email;

    public PersonModel(){
        //constructeur vide pour Jackson
    }

    public PersonModel(String firstName, String lastName, String address, String city, String zip, String phone, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip= zip;
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

    //ajout de la méthode d'authentification via prénom + nom
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

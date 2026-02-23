package com.openclassrooms.safetynet_alerts.model;

import java.util.List;

/**
 * Modèle représentant l'association entre une personne et ses données médicales.
 */
public class MedicalrecordModel {
    private String firstName;

    private String lastName;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public MedicalrecordModel() {

    }

    /**
     * Constructeur permettant de créer une association entre une personne et ses données médicales.
     *
     * @param firstName   prénom de la personne
     * @param lastName    nom de la personne
     * @param birthdate   date de naissance de la personne
     * @param medications liste des médicaments de la personne
     * @param allergies   liste des allergies de la personne
     */

    public MedicalrecordModel(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}

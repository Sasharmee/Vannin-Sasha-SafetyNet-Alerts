package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * DTO représentant les informations personnelles et médicales
 * d'une personne, utilisé dans le cadre de l'endpoint
 * /personInfo?lastName={lastName}.
 *
 * <p>Ce DTO regroupe les informations suivantes :</p>
 * <ul>
 *     <li>Le nom de famille</li>
 *     <li>L’adresse</li>
 *     <li>L’âge</li>
 *     <li>L’adresse email</li>
 *     <li>La liste des médicaments</li>
 *     <li>La liste des allergies</li>
 * </ul>
 */
public class PersonInfoDTO {

    private String lastName;

    private String address;

    private int age;

    private String email;

    private List<String> medications;

    private List<String> allergies;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */

    public PersonInfoDTO() {
    }

    /**
     * Constructeur permettant d'instancier un PersonInfoDTO
     *
     * @param lastName    nom de famille de la personne
     * @param address     adresse de la personne
     * @param age         âge de la personne
     * @param email       adresse email de la personne
     * @param medications liste des médicaments de la personne
     * @param allergies   liste des allergies de la personne
     */
    public PersonInfoDTO(String lastName, String address, int age, String email, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

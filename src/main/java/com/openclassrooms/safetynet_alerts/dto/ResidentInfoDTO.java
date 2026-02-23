package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * DTO représentant les informations d’un résident vivant à une adresse donnée dans le cadre de l’endpoint /fire.
 *
 * <p>
 * Ce DTO contient :
 * <ul>
 *     <li>Le nom de famille du résident</li>
 *     <li>Son numéro de téléphone</li>
 *     <li>Son âge</li>
 *     <li>La liste de ses médicaments</li>
 *     <li>La liste de ses allergies</li>
 * </ul>
 */
public class ResidentInfoDTO {

    private String lastName;

    private String phone;

    private int age;

    private List<String> medications;

    private List<String> allergies;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */

    public ResidentInfoDTO() {
    }

    /**
     * Constructeur permettant d'instancier ResidentInfoDTO
     *
     * @param lastName    nom de famille de la personne
     * @param phone       numéro de téléphone de la personne
     * @param age         âge de la personne
     * @param medications liste de médicaments de la personne
     * @param allergies   liste des allergies de la personne
     */
    public ResidentInfoDTO(String lastName, String phone, int age, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

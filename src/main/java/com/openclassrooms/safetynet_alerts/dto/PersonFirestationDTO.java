package com.openclassrooms.safetynet_alerts.dto;

/**
 * DTO représentant une personne couverte par une caserne dans le cadre de l’endpoint /firestation?stationNumber={stationNumber}.
 * <p>
 * <p>
 * Ce DTO contient uniquement :
 * <ul>
 *     <li>Le prénom</li>
 *     <li>Le nom de famille</li>
 *     <li>L’adresse</li>
 *     <li>Le numéro de téléphone</li>
 * </ul>
 */
public class PersonFirestationDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phone;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */

    public PersonFirestationDTO() {
    }

    /**
     * Constructeur permettant d'instancier un PersonFirestationDTO
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de la personne
     * @param address   adresse de la personne
     * @param phone     numéro de téléphone de la personne
     */
    public PersonFirestationDTO(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

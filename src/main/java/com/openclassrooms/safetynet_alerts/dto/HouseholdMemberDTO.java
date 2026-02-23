package com.openclassrooms.safetynet_alerts.dto;

/**
 * DTO représentant un membre du foyer vivant à la même adresse qu’un enfant,
 * utilisé dans le cadre de l’endpoint /childAlert.
 *
 * <p>Ce DTO contient uniquement :</p>
 * <ul>
 *     <li>Le prénom</li>
 *     <li>Le nom de famille</li>
 * </ul>
 */

public class HouseholdMemberDTO {

    private String firstName;

    private String lastName;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */

    public HouseholdMemberDTO() {
    }

    /**
     * Constructeur permettant d'instancier un HouseholdMemberDTO
     *
     * @param firstName prénom du membre du foyer
     * @param lastName  nom du membre du foyer
     */
    public HouseholdMemberDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}

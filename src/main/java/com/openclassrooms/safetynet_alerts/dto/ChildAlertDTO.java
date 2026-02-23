package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * Ce DTO représente un enfant vivant à une adresse donnée utilisé dans le cadre de l'endpoint /childAlert
 *
 * <p>
 * Ce DTO contient :
 * <ul>
 *     <li>Le prénom de l’enfant</li>
 *     <li>Son nom de famille</li>
 *     <li>Son âge</li>
 *     <li>La liste des autres membres du foyer vivant à la même adresse</li>
 * </ul>
 */
public class ChildAlertDTO {

    private String firstName;

    private String lastName;

    private int age;

    private List<HouseholdMemberDTO> householdMembers;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public ChildAlertDTO() {
    }

    /**
     * Constructeur permettant d'instancier un ChildAlertDTO
     *
     * @param firstName        prénom de l'enfant
     * @param lastName         nom de l'enfant
     * @param age              âge de l'enfant(=<18 ans )
     * @param householdMembers liste des autres membres du foyer
     */
    public ChildAlertDTO(String firstName, String lastName, int age, List<HouseholdMemberDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<HouseholdMemberDTO> getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(List<HouseholdMemberDTO> householdMembers) {
        this.householdMembers = householdMembers;
    }
}

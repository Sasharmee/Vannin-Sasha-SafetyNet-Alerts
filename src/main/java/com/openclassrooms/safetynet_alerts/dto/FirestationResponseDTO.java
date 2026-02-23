package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * Ce DTO représente la liste des personnes couvertes par une caserne donnée,
 * ainsi que le décompte d'adultes et d'enfants.
 * Il est utilisé dans le cadre de l'endpoint /firestation avec le paramètre stationNumber
 *
 * <p>
 * Ce DTO contient :
 * <ul>
 *     <li>La liste des personnes couvertes par la caserne</li>
 *     <li>Le nombre d'adultes</li>
 *     <li>Le nombre d'enfants</li>
 * </ul>
 * <p>
 */
public class FirestationResponseDTO {

    private List<PersonFirestationDTO> persons;
    private int adultCount;
    private int childCount;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public FirestationResponseDTO() {
    }

    /**
     * Constructeur permettant d'instancier un FireResponseDTO
     *
     * @param persons    liste des personnes couvertes par la caserne
     * @param adultCount le nombre d'adultes
     * @param childCount le nombre d'enfants
     */
    public FirestationResponseDTO(List<PersonFirestationDTO> persons, int adultCount, int childCount) {
        this.persons = persons;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }

    public List<PersonFirestationDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonFirestationDTO> persons) {
        this.persons = persons;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}

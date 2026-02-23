package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * DTO représentant la liste des résidents d’un foyer ainsi que le numéro de la caserne couvrant l’adresse.
 * Il est utilisé dans le cadre de l'endpoint /fire
 *
 * <p>
 * Ce DTO contient :
 * <ul>
 *     <li>La liste des membres du foyer</li>
 *     <li>Le numéro de caserne</li>
 * </ul>
 */

public class FireDTO {

    private List<ResidentInfoDTO> residents;

    private String stationNumber;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public FireDTO() {
    }

    /**
     * Constructeur permettant d'instancier un FireDTO
     *
     * @param residents     liste des résidents du foyer
     * @param stationNumber numéro de la caserne
     */
    public FireDTO(List<ResidentInfoDTO> residents, String stationNumber) {
        this.residents = residents;
        this.stationNumber = stationNumber;
    }

    public List<ResidentInfoDTO> getResidents() {
        return residents;
    }

    public void setResidents(List<ResidentInfoDTO> residents) {
        this.residents = residents;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }
}

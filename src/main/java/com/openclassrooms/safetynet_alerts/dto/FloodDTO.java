package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

/**
 * Ce DTO représente la liste des foyers desservis par la station, les personnes y sont regroupées par adresse
 * Il est utilisé dans le cadre de l'endpoint /flood
 *
 * <p>
 * Ce DTO contient :
 * <ul>
 *     <li>l'adresse </li>
 *     <li>La liste des personnes couvertes</li>
 *
 */
public class FloodDTO {

    private String address;

    private List<ResidentInfoDTO> residents;

    /**
     * Constructeur vide requis par Jackson pour la
     * désérialisation JSON.
     */
    public FloodDTO() {
    }

    /**
     * Constructeur permettant d'instancier un FloodDTO
     *
     * @param address   adresse couverte par la station
     * @param residents liste des personnes vivant au foyer
     */
    public FloodDTO(String address, List<ResidentInfoDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ResidentInfoDTO> getResidents() {
        return residents;
    }

    public void setResidents(List<ResidentInfoDTO> residents) {
        this.residents = residents;
    }
}

package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

public class FloodDTO {

    private String address;

    private List<ResidentInfoDTO> residents;

    public FloodDTO() {//Constructeur vide Jackson
    }

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

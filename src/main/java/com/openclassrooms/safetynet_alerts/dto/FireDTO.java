package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

public class FireDTO {

    private List<ResidentInfoDTO> residents;

    private String stationNumber;

    public FireDTO() {
    }

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

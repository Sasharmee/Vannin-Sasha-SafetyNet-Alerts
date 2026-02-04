package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

public class FirestationResponseDTO {

    private List<PersonFirestationDTO> persons;
    private int adultCount;
    private int childCount;

    public FirestationResponseDTO() {//constructeur vide pour Jackson
    }

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

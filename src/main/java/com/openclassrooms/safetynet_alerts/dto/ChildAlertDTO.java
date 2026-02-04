package com.openclassrooms.safetynet_alerts.dto;

import java.util.List;

public class ChildAlertDTO {

    private String firstName;

    private String lastName;

    private int age;

    private List<HouseholdMemberDTO> householdMembers;

    public ChildAlertDTO() {//Constructeur vide pour Jackson
    }

    public ChildAlertDTO(String firstName, String lastName, int age, List<HouseholdMemberDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers =householdMembers;
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

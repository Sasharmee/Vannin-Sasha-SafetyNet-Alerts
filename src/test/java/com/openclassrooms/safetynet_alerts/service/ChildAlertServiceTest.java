package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.dto.HouseholdMemberDTO;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private ChildAlertService childAlertService;

    private List<PersonModel> persons;

    private PersonModel child;
    private PersonModel adult;
    private PersonModel other;

    @BeforeEach
    void setUp(){
        persons = new ArrayList<>();

        child = new PersonModel();
        child.setFirstName("Samy");
        child.setLastName("Ymas");
        child.setAddress("77 Paris");
        child.setEmail("Samy@mail.com");

        persons.add(child);

        adult = new PersonModel();
        adult.setFirstName("Cons");
        adult.setLastName("Snoc");
        adult.setAddress("77 Paris");
        adult.setEmail("Cons@mail.com");


        persons.add(adult);

        other = new PersonModel();
        other.setFirstName("Jimmy");
        other.setLastName("Jonny");
        other.setAddress("XYZ");
        other.setEmail("Jonny@mail.com");

        persons.add(other);
    }

    //Test comportement attendu, happy path
    @Test
    void getChildrenByAddress_shouldReturnChildDTO() throws Exception{
        //GIVEN
        when(personRepository.findAll()).thenReturn(persons);
        //Age child
        when(ageService.calculateAge(child)).thenReturn(10);
        when(ageService.isChild(child)).thenReturn(true);
        //Age adult
        //when(ageService.calculateAge(adult)).thenReturn(51);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");
        //THEN
        assertThat(result).hasSize(1);

        ChildAlertDTO dto = result.get(0);
        assertThat(dto.getFirstName()).isEqualTo("Samy");
        assertThat(dto.getLastName()).isEqualTo("Ymas");
        assertThat(dto.getAge()).isEqualTo(10);
        assertThat(dto.getHouseholdMembers()).hasSize(1);
        HouseholdMemberDTO memberDTO = dto.getHouseholdMembers().get(0);
        assertThat(memberDTO.getFirstName()).isEqualTo("Cons");
        assertThat(memberDTO.getLastName()).isEqualTo("Snoc");
    }

    //Test quand pas de child à l'address
    @Test
    void getChildrenByAddress_whenNoChildrenAtAddress_shouldReturnEmptyList() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);
        when(ageService.isChild(child)).thenReturn(false);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");

        //THEN
        assertThat(result).isEmpty();
    }

    //Test quand child pas bonne address
    @Test
    void getChildrenByAddress_whenChildrenWithoutAddress_shouldReturnEmptyList() throws Exception{
        child.setAddress(null);
        when(personRepository.findAll()).thenReturn(persons);
        //when(ageService.isChild(child)).thenReturn(true);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");

        //THEN
        assertThat(result).isEmpty();
    }

}

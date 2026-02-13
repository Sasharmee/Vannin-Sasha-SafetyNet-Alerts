package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
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
public class PhoneAlertServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private PhoneAlertService phoneAlertService;

    private List<PersonModel> persons;
    private List<FirestationModel> firestations;

    private PersonModel p1;
    private FirestationModel f1;

    @BeforeEach
    void setUp() {
        persons = new ArrayList<>();

        p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");

        persons.add(p1);

        firestations = new ArrayList<>();

        f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        firestations.add(f1);
    }

    //Test Happy path, on retourne List de phone
    @Test
    void getPhoneByStation_shouldReturnPhone() throws Exception{
        //données
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        //WHEN
        List<String> phones = phoneAlertService.getPhoneByStation("1");

        //THEN
        assertThat(phones).hasSize(1);
        assertThat(phones).containsExactly("123-456-789");

    }

    //Test si la station ne couvre aucune address
    @Test
    void getPhoneByStation_whenNoAddressFoundByStation_shouldReturnEmptyList() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        //WHEN
        List<String> phones = phoneAlertService.getPhoneByStation("99");

        //THEN
        assertThat(phones).isEmpty();
    }

    //Test si l'address de la firestation est null
    @Test
    void getPhoneByStation_whenAddressIsNull_shouldIgnoreFirestation() throws Exception {
        f1.setAddress(null);

        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<String> phones = phoneAlertService.getPhoneByStation("1");

        assertThat(phones).isEmpty();
    }

    //Test si la person n'a pas de phone
    @Test
    void getPhoneByStation_shouldIgnorePerson_whenPhoneIsNull() throws Exception {
        p1.setPhone(null);

        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<String> phones = phoneAlertService.getPhoneByStation("1");

        assertThat(phones).isEmpty();
    }

    //@Test
    //void getPhoneByStation_shouldIgnorePerson_whenAddressIsNull() throws Exception {
    //    p1.setAddress(null);
    //
    //    when(personRepository.findAll()).thenReturn(persons);
    //    when(firestationRepository.findAll()).thenReturn(firestations);
    //
    //    List<String> phones = phoneAlertService.getPhoneByStation("1");
    //
    //    assertThat(phones).isEmpty();
    //}
}

package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.FireDTO;
import com.openclassrooms.safetynet_alerts.dto.ResidentInfoDTO;
import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
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
public class FireServiceTest {


    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalrecordRepository medicalrecordRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private FireService fireService;

    private List<PersonModel> persons;
    private List<MedicalrecordModel> medicalrecords;
    private List<FirestationModel> firestations;

    private PersonModel p1;
    private MedicalrecordModel m1;
    private FirestationModel f1;

    @BeforeEach
    void setUp(){
        persons = new ArrayList<>();

        p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");

        persons.add(p1);

        medicalrecords = new ArrayList<>();

        m1= new MedicalrecordModel();
        m1.setFirstName("Samy");
        m1.setLastName("Ymas");
        m1.setMedications(List.of("medication:50mg"));
        m1.setAllergies(List.of("codeine"));

        medicalrecords.add(m1);

        firestations = new ArrayList<>();

        f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        firestations.add(f1);
    }

    //Test happy path, on retourne dto avec list de residents et stationNumber
    @Test
    void getFireByAddress_shouldReturnDTO() throws Exception{
        //on appelle les lists qu'on a configuré dans setUp
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);
        when(ageService.calculateAge(p1)).thenReturn(51);

        //WHEN
        FireDTO result = fireService.getFireByAddress("77 Paris");

        //THEN
        assertThat(result).isNotNull();
        assertThat(result.getStationNumber()).isEqualTo("1");
        assertThat(result.getResidents()).hasSize(1);

        ResidentInfoDTO resident = result.getResidents().get(0);
        assertThat(resident.getLastName()).isEqualTo("Ymas");
        assertThat(resident.getPhone()).isEqualTo("123-456-789");
        assertThat(resident.getAge()).isEqualTo(51);
        assertThat(resident.getMedications()).containsExactly("medication:50mg");
        assertThat(resident.getAllergies()).containsExactly("codeine");
    }

    //Test si on ne retrouve rien à l'address renseigné
    @Test
    void getFireByAddress_whenNoResultForAddress_shouldReturnEmpty() throws Exception{
        //on appelle les lists qu'on a configuré dans setUp
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //WHEN
        FireDTO result = fireService.getFireByAddress("XYZ");

        //THEN
        assertThat(result.getResidents()).isEmpty();
        assertThat(result.getStationNumber()).isNull();
    }

    //Test lorsqu'il n'y a personne à l'address renseignée
    @Test
    void getFireByAddress_whenNobodyAtAddress_shouldReturnEmpty() throws Exception{
        when(personRepository.findAll()).thenReturn(List.of());
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //WHEN
        FireDTO result= fireService.getFireByAddress("77 Paris");

        //THEN
        assertThat(result.getStationNumber()).isEqualTo("1");
        assertThat(result.getResidents()).isEmpty();
    }

    //Test lorsqu'il n'y a pas de medicalrecord associé à la personne
    @Test
    void getFireByAddress_whenNoMedicalRecordIsFound_shouldReturnEmptyMedicalLists() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(medicalrecordRepository.findAll()).thenReturn(List.of());
        when(ageService.calculateAge(p1)).thenReturn(51);

        //WHEN
        FireDTO result = fireService.getFireByAddress("77 Paris");

        //THEN
        ResidentInfoDTO resident = result.getResidents().get(0);
        assertThat(resident.getMedications()).isEmpty();
        assertThat(resident.getAllergies()).isEmpty();
    }
}

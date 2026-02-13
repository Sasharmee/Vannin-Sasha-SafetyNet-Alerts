package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
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
public class PersonInfoServiceTest {

    //Dépendance
    @Mock
    private PersonRepository personRepository;
    //Dépendance
    @Mock
    private MedicalrecordRepository medicalrecordRepository;
    //Dépendance
    @Mock
    private AgeService ageService;

    @InjectMocks
    private PersonInfoService personInfoService;

    private List<PersonModel> persons;
    private List<MedicalrecordModel> medicalrecords;

    @BeforeEach
    void setUp(){

        persons = new ArrayList<>();

        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setEmail("Samy@mail.com");

        persons.add(p1);

        medicalrecords = new ArrayList<>();
        MedicalrecordModel m1= new MedicalrecordModel();
        m1.setFirstName("Samy");
        m1.setLastName("Ymas");
        m1.setMedications(List.of("medications:50mg"));
        m1.setAllergies(List.of("codeine"));

        medicalrecords.add(m1);

    }

    //Test Happy Path, on retourne le DTO avec toutes les infos
    @Test
    void getPersonInfoByLastName_shouldReturnDTO() throws Exception{
        //On appelle les listes configurées dans le setUp
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //age donnée car controlé via le mock
        PersonModel samy = persons.get(0);
        when(ageService.calculateAge(samy)).thenReturn(51);

        //WHEN lorsqu'on fait appel au service pour Ymas
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Ymas");

        //THEN, on attend les résultats suivants
        assertThat(result).hasSize(1);

        PersonInfoDTO personInfoDTO = result.get(0);
         assertThat(personInfoDTO.getLastName()).isEqualTo("Ymas");
         assertThat(personInfoDTO.getAddress()).isEqualTo("77 Paris");
         assertThat(personInfoDTO.getAge()).isEqualTo(51);
         assertThat(personInfoDTO.getEmail()).isEqualTo("Samy@mail.com");
         assertThat(personInfoDTO.getMedications()).containsExactly("medications:50mg");
         assertThat(personInfoDTO.getAllergies()).containsExactly("codeine");
    }

    //Test lorsqu'on ne trouve pas de person ni medicalrecord avec le nom associé
    @Test
    void getPersonInfoByLastName_shouldReturnEmptyList_whenNoLastNameFound() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("XYZ");

        assertThat(result).isEmpty();
    }

    //Test lorsqu'on ne retrouve pas de medicalrecord
    @Test
    void getPersonInfoByLastName_shouldReturnEmptyMedicalLists_whenNoMedicalRecordFound() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of());

        //age donnée car controlé via le mock
        PersonModel samy = persons.get(0);
        when(ageService.calculateAge(samy)).thenReturn(51);

        //WHEN lorsqu'on fait appel au service pour Ymas
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Ymas");

        assertThat(result).hasSize(1);
        PersonInfoDTO personInfoDTO =result.get(0);

        assertThat(personInfoDTO.getMedications()).isEmpty();
        assertThat(personInfoDTO.getAllergies()).isEmpty();
    }

    //Test lorsque la person n'a pas de medications ni d'allergies
    @Test
    void getPersonInfoByLastName_shouldKeepEmptyLists_whenMedicalListsAreEmpty() throws Exception {
        MedicalrecordModel mNull = new MedicalrecordModel();
        mNull.setFirstName("Samy");
        mNull.setLastName("Ymas");
        mNull.setMedications(List.of());
        mNull.setAllergies(List.of());

        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of(mNull));

        PersonModel samy = persons.get(0);
        when(ageService.calculateAge(samy)).thenReturn(51);

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Ymas");

        assertThat(result).hasSize(1);
        PersonInfoDTO dto = result.get(0);

        assertThat(dto.getMedications()).isEmpty();
        assertThat(dto.getAllergies()).isEmpty();
    }
}

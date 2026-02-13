package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.dto.PersonFirestationDTO;
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
public class FirestationResponseServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private FirestationResponseService firestationResponseService;

    private List<PersonModel> persons;
    private List<FirestationModel> firestations;

    private PersonModel child;
    private PersonModel adult;
    private PersonModel other;

    private FirestationModel f1;

    @BeforeEach
    void setUp(){
        persons = new ArrayList<>();

        child = new PersonModel();
        child.setFirstName("Samy");
        child.setLastName("Ymas");
        child.setAddress("77 Paris");
        child.setPhone("123-456-789");

        persons.add(child);

        adult = new PersonModel();
        adult.setFirstName("Cons");
        adult.setLastName("Snoc");
        adult.setAddress("77 Paris");
        adult.setPhone("111-222-333");


        persons.add(adult);

        other = new PersonModel();
        other.setFirstName("Jimmy");
        other.setLastName("Jonny");
        other.setAddress("XYZ");
        other.setEmail("Jonny@mail.com");

        persons.add(other);
        firestations = new ArrayList<>();

        f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        firestations.add(f1);
    }

    //Test happy path, on return le DTO avec la list de person et leurs infos ainsi que le nombre d'adult et de child
    @Test
    void getPersonsCoveredByStation_shouldReturnDTO() throws Exception{
        //on appelle les lists configurées dans setUp
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(ageService.isAdult(adult)).thenReturn(true);
        when(ageService.isAdult(child)).thenReturn(false);

        //WHEN
        FirestationResponseDTO result = firestationResponseService.getPersonsCoveredByStation("1");

        //THEN
        assertThat(result).isNotNull();
        assertThat(result.getAdultCount()).isEqualTo(1);
        assertThat(result.getChildCount()).isEqualTo(1);

        assertThat(result.getPersons()).hasSize(2);
        assertThat(result.getPersons()).extracting(PersonFirestationDTO::getFirstName).containsExactlyInAnyOrder("Samy", "Cons");

        //vérification DTO PersonFirestationDTO
        PersonFirestationDTO samy = result.getPersons().stream()
                .filter(p -> p.getFirstName().equals("Samy"))
                .findFirst()
                .orElseThrow();

        assertThat(samy.getLastName()).isEqualTo("Ymas");
        assertThat(samy.getAddress()).isEqualTo("77 Paris");
        assertThat(samy.getPhone()).isEqualTo("123-456-789");
    }

    //Test lorsqu'aucune address concorde avec la station renseignée
    @Test
    void getPersonsCoveredByStation_whenNoAddressMatchesStation_shouldReturnEmpty() throws Exception{
        //on appelle les lists configurées dans setUp
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);
        //WHEN
        FirestationResponseDTO result = firestationResponseService.getPersonsCoveredByStation("99");
        //THEN
        assertThat(result.getPersons()).isEmpty();
        assertThat(result.getAdultCount()).isEqualTo(0);
        assertThat(result.getChildCount()).isEqualTo(0);
    }

    //Test lorsque personne ne vie à l'address
    @Test
    void getPersonsCoveredByStation_whenNoPersonsAtAddress_shouldReturnEmpty() throws Exception{
        //on appelle les données
        when(personRepository.findAll()).thenReturn(List.of());
        when(firestationRepository.findAll()).thenReturn(firestations);
        //WHEN
        FirestationResponseDTO result = firestationResponseService.getPersonsCoveredByStation("1");
        //THEN
        assertThat(result.getPersons()).isEmpty();
        assertThat(result.getChildCount()).isEqualTo(0);
        assertThat(result.getAdultCount()).isEqualTo(0);
    }
}

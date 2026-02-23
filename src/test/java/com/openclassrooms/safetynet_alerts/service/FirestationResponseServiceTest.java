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

/**
 * Tests unitaires du {@link FirestationResponseService}
 * <p>
 * Cette classe valide la logique métier de l'endpoint {@code /firestation?stationNumber={stationNumber}} :
 * à partir du numéro de caserne donné, on doit identifier les personnes couvertes par la caserne,
 * construire le {@link FirestationResponseDTO} contenant : la liste des personnes,
 * le nombre d'adultes et d'enfants
 */
@ExtendWith(MockitoExtension.class)
public class FirestationResponseServiceTest {
    /**
     * Mock du repository des casernes.
     * Permet de simuler les données sans accéder à la source réelle.
     */
    @Mock
    private FirestationRepository firestationRepository;
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données sans accéder à la source réelle.
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Mock du service de calcul d'âge.
     * Permet de contrôler les retours de {@code calculateAge()} et {@code isChild()}.
     */
    @Mock
    private AgeService ageService;
    /**
     * Instance du service à tester, avec injections des mocks.
     */
    @InjectMocks
    private FirestationResponseService firestationResponseService;
    /**
     * Données de test : liste complète de personnes simulées
     */
    private List<PersonModel> persons;
    /**
     * Données de test : liste complète de casernes simulées
     */
    private List<FirestationModel> firestations;
    /**
     * Personne mineure couverte par la caserne donnée
     */
    private PersonModel child;
    /**
     * Personne adulte couverte par la caserne donnée
     */
    private PersonModel adult;
    /**
     * Personne couverte par une autre caserne
     */
    private PersonModel other;
    /**
     * Caserne simulée (station "1") couvrant l'adresse "77 Paris".
     */
    private FirestationModel f1;

    /**
     * Initialise les données de test avant chaque méthode.
     *
     */
    @BeforeEach
    void setUp() {
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

    /**
     * Vérifie que le service retourne correctement un {@link FirestationResponseDTO} lorsque :
     * pour un numéro de caserne donné, on retrouve les personnes dont l'adresse du foyer est couverte
     * et le comptage des enfants et des adultes du foyer a lieu.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonsCoveredByStation_shouldReturnDTO() throws Exception {
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

    /**
     * Vérifie que le service retourne une liste vide lorsque la caserne donnée ne couvre aucun foyer.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonsCoveredByStation_whenNoAddressMatchesStation_shouldReturnEmpty() throws Exception {
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

    /**
     * Vérifie que lorsque l'adresse couverte par la caserne ne contient aucun résident,
     * le service renvoie une liste vide.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonsCoveredByStation_whenNoPersonsAtAddress_shouldReturnEmpty() throws Exception {
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

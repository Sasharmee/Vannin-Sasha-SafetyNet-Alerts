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

/**
 * Tests unitaires de {@link PhoneAlertService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint {@code /phoneAlert} lorsque :
 * une station est donnée, le service doit renvoyer les numéros de téléphone de tous les résidents couverts par cette caserne
 */
@ExtendWith(MockitoExtension.class)
public class PhoneAlertServiceTest {
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données retournées sans accéder à la source réelle.
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Mock du repository des casernes.
     * Permet de simuler les données retournées sans accéder à la source réelle.
     */
    @Mock
    private FirestationRepository firestationRepository;
    /**
     * Instance du service à tester, avec injections automatiques des mocks.
     */
    @InjectMocks
    private PhoneAlertService phoneAlertService;
    /**
     * Données de test : liste complète de personnes simulées
     */
    private List<PersonModel> persons;
    /**
     * Données de test : liste complète de casernes simulées
     */
    private List<FirestationModel> firestations;

    private PersonModel p1;
    private FirestationModel f1;

    /**
     * Initialise les données de test avant chaque méthode
     */
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

    /**
     * Vérifie que le service retourne correctement une liste des numéros des personnes couvertes par la caserne donnée
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPhoneByStation_shouldReturnPhone() throws Exception {
        //données
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        //WHEN
        List<String> phones = phoneAlertService.getPhoneByStation("1");

        //THEN
        assertThat(phones).hasSize(1);
        assertThat(phones).containsExactly("123-456-789");

    }

    /**
     * Vérifie que lorsque aucune correspondance entre la caserne donnée et une adresse,
     * alors le service retourne une liste vide.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPhoneByStation_whenNoAddressFoundByStation_shouldReturnEmptyList() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        //WHEN
        List<String> phones = phoneAlertService.getPhoneByStation("99");

        //THEN
        assertThat(phones).isEmpty();
    }

    /**
     * Vérifie que lorsque la caserne n'est associée à aucune adresse alors celle-ci est ignorée par le service.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPhoneByStation_whenAddressIsNull_shouldIgnoreFirestation() throws Exception {
        f1.setAddress(null);

        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<String> phones = phoneAlertService.getPhoneByStation("1");

        assertThat(phones).isEmpty();
    }

    /**
     * Vérifie que si une personne n'a pas de numéro de téléphone,
     * alors elle est ignorée et rien n'est retourné pas le service.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPhoneByStation_shouldIgnorePerson_whenPhoneIsNull() throws Exception {
        p1.setPhone(null);

        when(personRepository.findAll()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<String> phones = phoneAlertService.getPhoneByStation("1");

        assertThat(phones).isEmpty();
    }
}

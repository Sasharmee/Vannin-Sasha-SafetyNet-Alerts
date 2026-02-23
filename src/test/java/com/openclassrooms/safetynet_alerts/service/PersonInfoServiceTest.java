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

/**
 * Tests unitaires du {@link PersonInfoService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint {@code /personInfolastName={lastName}}:
 * à partir d'un nom donné, le service doit retourner les données personnelles et médicales de la personne portant le nom.
 */
@ExtendWith(MockitoExtension.class)
public class PersonInfoServiceTest {

    /**
     * Mock du repository des personnes.
     * Permet de simuler les données retournées sans accéder à la source réelle.
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Mock du repository des fichiers médicaux.
     * Permet de simuler les données retournées sans accéder à la source réelle.
     */
    @Mock
    private MedicalrecordRepository medicalrecordRepository;
    /**
     * Mock du service de calcul d'âge.
     * Permet de contrôler les retours de {@code calculateAge()}.
     */
    @Mock
    private AgeService ageService;
    /**
     * Instance du service à tester, avec injections automatiques des mocks.
     */
    @InjectMocks
    private PersonInfoService personInfoService;
    /**
     * Données de test : liste complète de personnes simulées
     */
    private List<PersonModel> persons;
    /**
     * Données de test : liste complète de fiches médicales simulées
     */
    private List<MedicalrecordModel> medicalrecords;

    /**
     * Initialise les données de test avant chaque méthode
     */
    @BeforeEach
    void setUp() {

        persons = new ArrayList<>();

        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setEmail("Samy@mail.com");

        persons.add(p1);

        medicalrecords = new ArrayList<>();
        MedicalrecordModel m1 = new MedicalrecordModel();
        m1.setFirstName("Samy");
        m1.setLastName("Ymas");
        m1.setMedications(List.of("medications:50mg"));
        m1.setAllergies(List.of("codeine"));

        medicalrecords.add(m1);

    }

    /**
     * Vérifie que le service retourne correctement un {@link PersonInfoDTO}
     * lorsque le nom correspond à une personne existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonInfoByLastName_shouldReturnDTO() throws Exception {
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

    /**
     * Vérifie que le service lorsqu'on ne retrouve aucune personne ni fiche médicale avec le nom donné.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonInfoByLastName_shouldReturnEmptyList_whenNoLastNameFound() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("XYZ");

        assertThat(result).isEmpty();
    }

    /**
     * Vérifie que lorsque la personne existe, mais que le dossier médical n'est pas retrouvé,
     * alors les listes medications et allergies sont vides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getPersonInfoByLastName_shouldReturnEmptyMedicalLists_whenNoMedicalRecordFound() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of());

        //age donnée car controlé via le mock
        PersonModel samy = persons.get(0);
        when(ageService.calculateAge(samy)).thenReturn(51);

        //WHEN lorsqu'on fait appel au service pour Ymas
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Ymas");

        assertThat(result).hasSize(1);
        PersonInfoDTO personInfoDTO = result.get(0);

        assertThat(personInfoDTO.getMedications()).isEmpty();
        assertThat(personInfoDTO.getAllergies()).isEmpty();
    }

    /**
     * Vérifie que lorsque le dossier médical existe, mais contient
     * des listes vides, celles-ci sont correctement conservées.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
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

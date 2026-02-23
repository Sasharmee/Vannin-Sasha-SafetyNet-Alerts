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

/**
 * Tests unitaires du {@link FireService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint {@code /fire} :
 * à partir d'une adresse donnée, le service doit retourner le numéro de la caserne
 * ainsi que la liste des résidents et leurs informations.
 */
@ExtendWith(MockitoExtension.class)
public class FireServiceTest {
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Mock du repository des casernes.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private FirestationRepository firestationRepository;
    /**
     * Mock du repository des dossiers médicaux.
     * Permet de simuler les données sans accéder à la source réelle.
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
     * Instance du service testé avec injection automatique des mocks.
     */
    @InjectMocks
    private FireService fireService;
    /**
     * Liste simulée de personnes utilisée pour les tests.
     */
    private List<PersonModel> persons;
    /**
     * Liste simulée de dossiers médicaux utilisée pour les tests.
     */
    private List<MedicalrecordModel> medicalrecords;
    /**
     * Liste simulée de casernes utilisée pour les tests.
     */
    private List<FirestationModel> firestations;

    private PersonModel p1;
    private MedicalrecordModel m1;
    private FirestationModel f1;

    /**
     * Initialise les données de tests avant chaque méthode
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

        medicalrecords = new ArrayList<>();

        m1 = new MedicalrecordModel();
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

    /**
     * Vérifie que le service retourne correctement un {@link FireDTO} lorsque :
     * pour une adresse donnée, on retrouve le numéro de caserne couvrant le foyer
     * ainsi que la liste des différents résidents du foyer et leurs informations.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFireByAddress_shouldReturnDTO() throws Exception {
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

    /**
     * Vérifie que le service retourne une liste vide lorsqu'on ne retrouve rien associé à l'adresse donnée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFireByAddress_whenNoResultForAddress_shouldReturnEmpty() throws Exception {
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

    /**
     * Vérifie que lorsque personne ne vit à l'adresse,
     * le service renvoie quand même le numéro de la caserne et une liste de résidents vide.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFireByAddress_whenNobodyAtAddress_shouldReturnEmpty() throws Exception {
        when(personRepository.findAll()).thenReturn(List.of());
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //WHEN
        FireDTO result = fireService.getFireByAddress("77 Paris");

        //THEN
        assertThat(result.getStationNumber()).isEqualTo("1");
        assertThat(result.getResidents()).isEmpty();
    }

    /**
     * Vérifie que si aucun dossier médical n'est trouvé,
     * les listes de médicaments et d'allergies (présentes dans ResidentDTO) sont vides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFireByAddress_whenNoMedicalRecordIsFound_shouldReturnEmptyMedicalLists() throws Exception {
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

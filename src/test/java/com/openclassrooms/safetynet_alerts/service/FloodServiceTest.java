package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
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
 * Tests unitaires du {@link FloodService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint {@code /flood} :
 * à partir de casernes données, le service doit retourner les adresses desservies
 * ainsi que la liste des résidents couverts ainsi que leurs informations.
 */
@ExtendWith(MockitoExtension.class)
public class FloodServiceTest {
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données sans accéder à la source réelle.
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Mock du repository des casernes.
     * Permet de simuler les données sans accéder à la source réelle.
     */
    @Mock
    private FirestationRepository firestationRepository;
    /**
     * Mock du repository des fichiers médicaux.
     * Permet de simuler les données sans accéder à la source réelle.
     */
    @Mock
    private MedicalrecordRepository medicalrecordRepository;
    /**
     * Mock du service de calcul d'âge.
     * Permet de controller les retours de {@code calculateAge()}.
     */
    @Mock
    private AgeService ageService;
    /**
     * Instance du service à tester, avec injections automatiques des mocks.
     */
    @InjectMocks
    private FloodService floodService;
    /**
     * Données de test : liste complète de casernes simulées
     */
    private List<FirestationModel> firestations;
    /**
     * Données de test : liste complète de personnes simulées
     */
    private List<PersonModel> persons;
    /**
     * Données de test : liste complète de fiche médicale simulée
     */
    private List<MedicalrecordModel> medicalrecords;
    /**
     * Personne habitant une adresse couverte.
     */
    private PersonModel p1;
    /**
     * Personne habitant une adresse non couverte.
     */
    private PersonModel p2;

    /**
     * Initialise les données de test avant chaque méthode.
     */
    @BeforeEach
    void setUp() {
        firestations = new ArrayList<>();

        FirestationModel fs1 = new FirestationModel();
        fs1.setAddress("77 Paris");
        fs1.setStation("1");

        FirestationModel fs2 = new FirestationModel();
        fs2.setAddress("88 Lyon");
        fs2.setStation("2");

        firestations.add(fs1);
        firestations.add(fs2);

        persons = new ArrayList<>();
        p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");

        p2 = new PersonModel();
        p2.setFirstName("Cons");
        p2.setLastName("Snoc");
        p2.setAddress("11 Londres");
        p2.setPhone("111-222-333");

        persons.add(p1);
        persons.add(p2);

        medicalrecords = new ArrayList<>();
        MedicalrecordModel mr1 = new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        medicalrecords.add(mr1);
    }

    /**
     * Vérifie que le service retourne correctement un {@link FloodDTO} lorsque :
     * pour les casernes données, on retrouve l'adresse du foyer
     * ainsi que la liste des résidents et leurs informations.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_shouldReturnHouseholdsByAddress() throws Exception {
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);
        when(ageService.calculateAge(p1)).thenReturn(51);

        List<FloodDTO> result = floodService.getFloodByStation("1");

        assertThat(result).hasSize(1);

        FloodDTO dto = result.get(0);
        assertThat(dto.getAddress()).isEqualTo("77 Paris");
        assertThat(dto.getResidents()).hasSize(1);

        ResidentInfoDTO resident = dto.getResidents().get(0);
        assertThat(resident.getLastName()).isEqualTo("Ymas");
        assertThat(resident.getPhone()).isEqualTo("123-456-789");
        assertThat(resident.getAge()).isEqualTo(51);
        assertThat(resident.getMedications()).containsExactly("medication:50mg");
        assertThat(resident.getAllergies()).containsExactly("codeine");
    }

    /**
     * Vérifie que le service gère correctement plusieurs stations séparées par une virgule avec espaces (trim).
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_shouldHandleComaAndTrimSpaces() throws Exception {
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);
        when(ageService.calculateAge(p1)).thenReturn(51);

        List<FloodDTO> result = floodService.getFloodByStation("1, 2");

        assertThat(result).extracting(FloodDTO::getAddress).contains("77 Paris");
    }

    /**
     * Vérifie que lorsque les casernes ne couvrent aucune adresse, le service retourne une liste vide.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_whenNoStationMatches_shouldReturnEmpty() throws Exception {
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        List<FloodDTO> result = floodService.getFloodByStation("99");

        assertThat(result).isEmpty();
    }

    /**
     * Vérifie que lorsque les dossiers médicaux ne sont pas retrouvés, les listes medications et allergies sont vides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_whenNoMedicalRecordFound_shouldReturnEmptyMedicalLists() throws Exception {
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of());
        when(ageService.calculateAge(p1)).thenReturn(51);

        List<FloodDTO> result = floodService.getFloodByStation("1");

        ResidentInfoDTO resident = result.get(0).getResidents().get(0);
        assertThat(resident.getMedications()).isEmpty();
        assertThat(resident.getAllergies()).isEmpty();
    }

    /**
     * Vérifie que lorsque l'adresse d'une personne est nulle, elle n'est pas prise en compte par le service.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_whenAddressIsNull_shouldSkipPersonAndGoForTheNextOne() throws Exception {
        p1.setAddress(null);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        List<FloodDTO> result = floodService.getFloodByStation("1");

        assertThat(result).isEmpty();
    }

    /**
     * Vérifie que lorsque les listes de medications et allergies existent, mais sont vides,
     * alors le service renvoie des listes vides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getFloodByStations_whenMedicalListsAreNull_shouldReturnEmptyLists() throws Exception {
        MedicalrecordModel mrNull = new MedicalrecordModel();
        mrNull.setFirstName("Samy");
        mrNull.setLastName("Ymas");
        mrNull.setMedications(null);
        mrNull.setAllergies(null);

        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of(mrNull));
        when(ageService.calculateAge(p1)).thenReturn(51);

        List<FloodDTO> result = floodService.getFloodByStation("1");

        ResidentInfoDTO resident = result.get(0).getResidents().get(0);
        assertThat(resident.getMedications()).isEmpty();
        assertThat(resident.getAllergies()).isEmpty();
    }
}

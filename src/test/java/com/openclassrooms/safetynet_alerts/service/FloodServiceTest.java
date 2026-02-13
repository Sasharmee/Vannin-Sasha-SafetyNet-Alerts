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

@ExtendWith(MockitoExtension.class)
public class FloodServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    MedicalrecordRepository medicalrecordRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private FloodService floodService;

    private List<FirestationModel> firestations;
    private List<PersonModel> persons;
    private List<MedicalrecordModel> medicalrecords;

    private PersonModel p1; // habite à une adresse couverte
    private PersonModel p2;

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

    //Premier test= happy path
    @Test
    void getFloodByStations_shouldReturnHouseholdsByAddress() throws Exception{
        //GIVEN: les données nécessaires
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);
        when(ageService.calculateAge(p1)).thenReturn(51);

        //WHEN
        List<FloodDTO> result = floodService.getFloodByStation("1");

        //THEN
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

    //Test avec deux stations et utilisation du trim
    @Test
    void getFloodByStations_shouldHandleComaAndTrimSpaces() throws Exception {
        //GIVEN: stations "1, 2" avec espace doit fonctionner
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);
        when(ageService.calculateAge(p1)).thenReturn(51);

        //WHEN
        List<FloodDTO> result = floodService.getFloodByStation("1, 2");

        //THEN: Samy = 77 Paris, donc au moins un household
        assertThat(result).extracting(FloodDTO::getAddress).contains("77 Paris");
    }

    //Test ou aucune la station ne couvre aucune address
    @Test
    void getFloodByStations_whenNoStationMatches_shouldReturnEmpty() throws Exception{
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //WHEN
        List<FloodDTO> result = floodService.getFloodByStation("99");

        //THEN
        assertThat(result).isEmpty();
    }

    //Test le cas ou le medicalrecord associé est vide (TEST A VERIFIER )
    @Test
    void getFloodByStations_whenNoMedicalRecordFound_shouldReturnEmptyMedicalLists() throws Exception{
        //GIVEN: avec medicalrecord associé à Samy vide
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(List.of());
        when(ageService.calculateAge(p1)).thenReturn(51);

        //WHEN
        List<FloodDTO> result = floodService.getFloodByStation("1");

        //THEN
        ResidentInfoDTO resident = result.get(0).getResidents().get(0);
        assertThat(resident.getMedications()).isEmpty();
        assertThat(resident.getAllergies()).isEmpty();
    }

    //Test si l'address de la person est nulle, on passe à la suite dans service et on ne renvoie rien associé à cette personne
    @Test
    void getFloodByStations_whenAddressIsNull_shouldSkipPersonAndGoForTheNextOne() throws Exception{
        p1.setAddress(null);
        when(firestationRepository.findAll()).thenReturn(firestations);
        when(personRepository.findAll()).thenReturn(persons);
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        //WHEN
        List<FloodDTO> result = floodService.getFloodByStation("1");

        //THEN
        assertThat(result).isEmpty();
    }

    //Test lorsque medicalrecord n'est pas vide mais les list s'y trouvant si
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

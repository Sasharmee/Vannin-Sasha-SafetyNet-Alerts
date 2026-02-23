package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du {@link MedicalrecordService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint CRUD {@code /medicalRecord} afin :
 * récupérer la liste des fiches médicales, en ajouter, en mettre à jour et en supprimer.
 */
@ExtendWith(MockitoExtension.class)
public class MedicalrecordServiceTest {
    /**
     * Mock du repository des fiches médicales.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private MedicalrecordRepository medicalrecordRepository;
    /**
     * Instance du service testé avec injection automatique des mocks.
     */
    @InjectMocks
    private MedicalrecordService medicalrecordService;
    /**
     * Liste simulée de fiches médicales utilisée pour les tests.
     */
    private List<MedicalrecordModel> medicalrecords;
    private MedicalrecordModel mr1;

    /**
     * Initialise les données de tests avant chaque méthode
     */
    @BeforeEach
    void setUp() {
        medicalrecords = new ArrayList<>();

        mr1 = new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("10/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        medicalrecords.add(mr1);
    }

    /**
     * Vérifie que le service renvoie correctement la liste des fiches médicales.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getAllMedicalrecord_shouldReturnListOfMedicalrecords() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(medicalrecords);

        List<MedicalrecordModel> result = medicalrecordService.getAllMedicalrecord();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Samy");
        assertThat(result.get(0).getLastName()).isEqualTo("Ymas");
        assertThat(result.get(0).getBirthdate()).isEqualTo("10/06/1985");
        assertThat(result.get(0).getMedications()).containsExactly("medication:50mg");
        assertThat(result.get(0).getAllergies()).containsExactly("codeine");

        verify(medicalrecordRepository).findAll();
        verifyNoMoreInteractions(medicalrecordRepository);
    }

    /**
     * Vérifie que le service ajoute correctement une nouvelle fiche médicale
     * lorsque la personne n'en possède pas déjà une.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addMedicalrecord_whenNoExisting_shouldReturnCreatedMedicalrecord() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        MedicalrecordModel toAdd = new MedicalrecordModel();
        toAdd.setFirstName("Cons");
        toAdd.setLastName("Snoc");
        toAdd.setBirthdate("05/05/1995");
        toAdd.setMedications(List.of("medication:100mg"));
        toAdd.setAllergies(List.of("fish"));


        MedicalrecordModel result = medicalrecordService.addMedicalrecord(toAdd);

        assertThat(result.getFirstName()).isEqualTo("Cons");
        assertThat(result.getLastName()).isEqualTo("Snoc");


        ArgumentCaptor captor =
                ArgumentCaptor.forClass(List.class);

        verify(medicalrecordRepository).saveAll((List<MedicalrecordModel>) captor.capture());

        List<MedicalrecordModel> savedList = (List<MedicalrecordModel>) captor.getValue();

        assertThat(savedList).hasSize(2);

        assertThat(savedList)
                .extracting(MedicalrecordModel::getFirstName)
                .containsExactlyInAnyOrder("Samy", "Cons");

        assertThat(savedList)
                .extracting(MedicalrecordModel::getLastName)
                .containsExactlyInAnyOrder("Ymas", "Snoc");
    }

    /**
     * Vérifie que le service lève une exception
     * lorsque l'on tente d'ajouter une fiche médicale pour une personne qui en possède déjà une.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addMedicalrecord_whenExistingMedicalrecord_shouldThrow() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        MedicalrecordModel duplicate = new MedicalrecordModel();
        duplicate.setFirstName("Samy");
        duplicate.setLastName("Ymas");
        duplicate.setBirthdate("10/06/1980");
        duplicate.setMedications(List.of("medication:150mg"));
        duplicate.setAllergies(List.of("sugar"));

        assertThatThrownBy(() -> medicalrecordService.addMedicalrecord(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Medicalrecord already exists");

        verify(medicalrecordRepository, never()).saveAll(any());
    }

    /**
     * Vérifie que le service met correctement à jour la fiche médicale existante quand
     * le nom et le prénom correspondent.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void updateMedicalrecord_whenFirstNameAndLastNameMatches_shouldReturnUpdatedMedicalrecord() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        MedicalrecordModel updated = new MedicalrecordModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setBirthdate("10/06/1980");
        updated.setMedications(List.of("medication:150mg"));
        updated.setAllergies(List.of("sugar"));

        MedicalrecordModel result = medicalrecordService.updateMedicalrecord(updated);

        assertThat(result.getBirthdate()).isEqualTo("10/06/1980");
        assertThat(result.getMedications()).containsExactly("medication:150mg");
        assertThat(result.getAllergies()).containsExactly("sugar");

        verify(medicalrecordRepository).saveAll(anyList());


    }

    /**
     * Vérifie que la méthode retourne {@code null}
     * lorsqu'aucune personne associée au nom et au prénom renseignés ne correspond à la mise à jour demandée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void updateMedicalrecord_whenNoFirstNameAndLastNameMatch_shouldReturnNull() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        MedicalrecordModel updated = new MedicalrecordModel();
        updated.setFirstName("X");
        updated.setLastName("Y");
        updated.setBirthdate("10/06/1980");
        updated.setMedications(List.of("medication:150mg"));
        updated.setAllergies(List.of("sugar"));

        MedicalrecordModel result = medicalrecordService.updateMedicalrecord(updated);

        assertThat(result).isNull();
    }

    /**
     * Vérifie que le service supprime correctement une fiche médicale via
     * le nom et le prénom donnés.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deleteMedicalrecord_whenFirstNameAndLastNameMatch_shouldDelete() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        boolean removed = medicalrecordService.deleteMedicalrecord("Samy", "Ymas");

        assertThat(removed).isTrue();

        verify(medicalrecordRepository).saveAll(anyList());

    }

    /**
     * Vérifie que le service ne supprime aucune fiche médicale lorsque
     * la combinaise du nom et prénom ne correspond pas .
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deleteMedicalrecord_whenNoMatch_shouldReturnFalse() throws Exception {
        when(medicalrecordRepository.findAll()).thenReturn(new ArrayList<>(medicalrecords));

        boolean removed = medicalrecordService.deleteMedicalrecord("X", "Y");

        assertThat(removed).isFalse();

        verify(medicalrecordRepository, never()).saveAll(anyList());

    }


}

package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.FirestationModel;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du {@link FirestationService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint CRUD {@code /firestation} afin de :
 * récupérer la liste des casernes, en ajouter, les mettre à jour et en supprimer.
 */
@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {
    /**
     * Mock du repository des casernes.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private FirestationRepository firestationRepository;
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private PersonRepository personRepository;
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
    private FirestationService firestationService;
    /**
     * Liste simulée de casernes utilisée pour les tests.
     */
    private List<FirestationModel> firestations;
    private FirestationModel f1;

    /**
     * Initialise les données de tests avant chaque méthode
     */
    @BeforeEach
    void setUp() {
        firestations = new ArrayList<>();

        f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        firestations.add(f1);

    }

    /**
     * Vérifie que le service renvoie correctement la liste des casernes.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getAllFirestation_shouldReturnListOfFirestations() throws Exception {
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<FirestationModel> result = firestationService.getAllFirestation(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAddress()).isEqualTo("77 Paris");
        assertThat(result.get(0).getStation()).isEqualTo("1");

        verify(firestationRepository).findAll();
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service ajoute correctement une nouvelle caserne
     * lorsque l'adresse n'est pas déjà couverte.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addFirestation_whenAddressNotCovered_shouldAddAndSave() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel toAdd = new FirestationModel();
        toAdd.setAddress("88 Lyon");
        toAdd.setStation("2");

        List<FirestationModel> result = firestationService.addFirestation(toAdd);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(FirestationModel::getAddress).containsExactlyInAnyOrder("77 Paris", "88 Lyon");

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service lève une exception lorsque l'on tente d'ajouter une caserne pour une adresse déjà couverte.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addFirestation_whenAddressAlreadyCovered_shouldThrow() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel duplicate = new FirestationModel();
        duplicate.setStation("45");
        duplicate.setAddress("77 Paris");

        assertThatThrownBy(() -> firestationService.addFirestation(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("address already covered by a station");

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service met correctement à jour la caserne existante quand l'adresse correspond.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void updateFirestation_whenAddressMatches_shouldReturnUpdatedStationAndSave() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel updated = new FirestationModel();
        updated.setAddress("77 Paris");
        updated.setStation("3");

        List<FirestationModel> result = firestationService.updateFirestation(updated);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAddress()).isEqualTo("77 Paris");
        assertThat(result.get(0).getStation()).isEqualTo("3");

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }


    @Test
    void updateFirestation_whenNoAddressMatch_shouldReturnNull() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel updated = new FirestationModel();
        updated.setAddress("X");
        updated.setStation("Y");

        List<FirestationModel> result = firestationService.updateFirestation(updated);

        assertThat(result).isNull();

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service supprime correctement une association via l'adresse donnée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deleteFirestation_whenAddressMatch_shouldDeleteAndSave() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation("77 Paris", null);

        assertThat(removed).isTrue();

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service supprime correctement une association via le numéro de caserne donnéee.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deleteFirestation_whenStationMatch_shouldDeleteAndSave() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation(null, "1");

        assertThat(removed).isTrue();

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    /**
     * Vérifie que le service ne supprime aucune association lorsqu'aucun critère ne correspond.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deleteFirestation_whenNoMatch_shouldReturnFalseAndNotSave() throws Exception {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation("x", "Y");

        assertThat(removed).isFalse();

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }
}

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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AgeService ageService;


    @InjectMocks
    private FirestationService firestationService;

    private List<FirestationModel> firestations;
    private FirestationModel f1;

    @BeforeEach
    void setUp() {
        firestations = new ArrayList<>();

        f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        firestations.add(f1);

    }

    //GET: Test Happy Path
    @Test
    void getAllFirestation_shouldReturnListOfFirestations() throws Exception{
        when(firestationRepository.findAll()).thenReturn(firestations);

        List<FirestationModel> result = firestationService.getAllFirestation(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAddress()).isEqualTo("77 Paris");
        assertThat(result.get(0).getStation()).isEqualTo("1");

        verify(firestationRepository).findAll();
        verifyNoMoreInteractions(firestationRepository);
    }

    //ADD-Test happy path
    @Test
    void addFirestation_whenAddressNotCovered_shouldAddAndSave() throws Exception{
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

    //ADD: Test lorsque l'address est déjà couverte par une firestation
    @Test
    void addFirestation_whenAddressAlreadyCovered_shouldThrow() throws Exception{
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel duplicate = new FirestationModel();
        duplicate.setStation("45");
        duplicate.setAddress("77 Paris");

        assertThatThrownBy(()->firestationService.addFirestation(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("address already covered by a station");

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    //UPDATE: Test happy path
    @Test
    void updateFirestation_whenAddressMatches_shouldReturnUpdatedStationAndSave() throws Exception{
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

    //UPDATE: quand y'a pas de match entre la station updated et l'address
    @Test
    void updateFirestation_whenNoAddressMatch_shouldReturnNull() throws Exception{
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        FirestationModel updated = new FirestationModel();
        updated.setAddress("X");
        updated.setStation("Y");

        List<FirestationModel> result= firestationService.updateFirestation(updated);

        assertThat(result).isNull();

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    //DELETE: Test happy path match avec address
    @Test
    void deleteFirestation_whenAddressMatch_shouldDeleteAndSave() throws Exception{
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation("77 Paris", null);

        assertThat(removed).isTrue();

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    //DELETE: Test happy path match avec station
    @Test
    void deleteFirestation_whenStationMatch_shouldDeleteAndSave() throws Exception{
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation(null, "1");

        assertThat(removed).isTrue();

        verify(firestationRepository).findAll();
        verify(firestationRepository).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }

    //DELETE: Test quand pas de match-> pas de delete
    @Test
    void deleteFirestation_whenNoMatch_shouldReturnFalseAndNotSave() throws Exception{
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>(firestations));

        boolean removed = firestationService.deleteFirestation("x", "Y");

        assertThat(removed).isFalse();

        verify(firestationRepository).findAll();
        verify(firestationRepository, never()).saveAll(anyList());
        verifyNoMoreInteractions(firestationRepository);
    }
}

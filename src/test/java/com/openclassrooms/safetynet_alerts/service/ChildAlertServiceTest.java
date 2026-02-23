package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.dto.HouseholdMemberDTO;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
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
 * Tests unitaires du {@link ChildAlertService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint {@code /childAlert}:
 * à partir d'une adresse, le service doit identifier les enfants (=<18 ans), calculer leur âge et lister les autres membres du foyer
 *
 *
 */
@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    /**
     * Mock du repository des personnes.
     * Permet de simuler les données retournées sans accéder à la source réelle.
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
     * Instance du service à tester, avec injections automatiques des mocks.
     */
    @InjectMocks
    private ChildAlertService childAlertService;
    /**
     * Données de test : liste complète de personnes simulées
     */
    private List<PersonModel> persons;
    /**
     * Personne mineure vivant à l'adresse ciblée
     */
    private PersonModel child;
    /**
     * Personne majeure vivant à l'adresse ciblée
     */
    private PersonModel adult;
    /**
     * Personne vivant à une autre adresse
     */
    private PersonModel other;

    /**
     * Initialise les données de test avant chaque méthode
     */
    @BeforeEach
    void setUp() {
        persons = new ArrayList<>();

        child = new PersonModel();
        child.setFirstName("Samy");
        child.setLastName("Ymas");
        child.setAddress("77 Paris");
        child.setEmail("Samy@mail.com");

        persons.add(child);

        adult = new PersonModel();
        adult.setFirstName("Cons");
        adult.setLastName("Snoc");
        adult.setAddress("77 Paris");
        adult.setEmail("Cons@mail.com");


        persons.add(adult);

        other = new PersonModel();
        other.setFirstName("Jimmy");
        other.setLastName("Jonny");
        other.setAddress("XYZ");
        other.setEmail("Jonny@mail.com");

        persons.add(other);
    }

    /**
     * Vérifie que le service retourne correctement un {@link ChildAlertDTO} lorsque :
     * l'enfant vit à l'adresse donnée et qu'un adulte partage le même foyer.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getChildrenByAddress_shouldReturnChildDTO() throws Exception {
        //GIVEN
        when(personRepository.findAll()).thenReturn(persons);
        //Age child
        when(ageService.calculateAge(child)).thenReturn(10);
        when(ageService.isChild(child)).thenReturn(true);
        //Age adult
        //when(ageService.calculateAge(adult)).thenReturn(51);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");
        //THEN
        assertThat(result).hasSize(1);

        ChildAlertDTO dto = result.get(0);
        assertThat(dto.getFirstName()).isEqualTo("Samy");
        assertThat(dto.getLastName()).isEqualTo("Ymas");
        assertThat(dto.getAge()).isEqualTo(10);
        assertThat(dto.getHouseholdMembers()).hasSize(1);
        HouseholdMemberDTO memberDTO = dto.getHouseholdMembers().get(0);
        assertThat(memberDTO.getFirstName()).isEqualTo("Cons");
        assertThat(memberDTO.getLastName()).isEqualTo("Snoc");
    }

    /**
     * Vérifie que le service retourne une liste vide lorsque l'enfant ne vit pas à l'adresse indiquée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getChildrenByAddress_whenNoChildrenAtAddress_shouldReturnEmptyList() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);
        when(ageService.isChild(child)).thenReturn(false);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");

        //THEN
        assertThat(result).isEmpty();
    }

    /**
     * Vérifie que le service retourne une liste vide lorsque l'enfant n'a pas d'adresse (adresse {@code null} pour l'enfant)
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getChildrenByAddress_whenChildrenWithoutAddress_shouldReturnEmptyList() throws Exception {
        child.setAddress(null);
        when(personRepository.findAll()).thenReturn(persons);
        //when(ageService.isChild(child)).thenReturn(true);
        when(ageService.isChild(adult)).thenReturn(false);

        //WHEN
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress("77 Paris");

        //THEN
        assertThat(result).isEmpty();
    }

}

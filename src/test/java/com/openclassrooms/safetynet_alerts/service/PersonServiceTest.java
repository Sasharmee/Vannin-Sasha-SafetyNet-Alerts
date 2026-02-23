package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
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
 * Tests unitaires du {@link PersonService}
 * <p>
 * Cette classe vérifie la logique métier de l'endpoint CRUD {@code /person} afin de :
 * récupérer la liste des personnes, en ajouter, les mettre à jour et en supprimer.
 *
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    /**
     * Mock du repository des personnes.
     * Permet de simuler les données sans accéder à la source réelle
     */
    @Mock
    private PersonRepository personRepository;
    /**
     * Instance du service testé avec injection automatique des mocks.
     */
    @InjectMocks
    private PersonService personService;
    /**
     * Liste simulée de personnes utilisée pour les tests.
     */
    private List<PersonModel> persons;
    private PersonModel p1;

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
        p1.setCity("Paris");
        p1.setZip("77000");
        p1.setPhone("123-456-789");
        p1.setEmail("Samy@mail.com");

        persons.add(p1);
    }

    /**
     * Vérifie que le service renvoie correctement la liste des personnes.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void getAllPersons_shouldReturnListPersons() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        List<PersonModel> result = personService.getAllPersons();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Samy");
        verify(personRepository).findAll(); // Explication
        verifyNoMoreInteractions(personRepository);
    }

    /**
     * Vérifie que le service ajoute correctement une nouvelle personne non existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addPerson_whenNoExisting_shouldAdd() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        PersonModel toAdd = new PersonModel();
        toAdd.setFirstName("Cons");
        toAdd.setLastName("Snoc");

        PersonModel result = personService.addPerson(toAdd);

        assertThat(result).isSameAs(toAdd);

        //vérfier que saveAll a été appelé avec une Liste contenant les 2
        ArgumentCaptor<List<PersonModel>> captor = ArgumentCaptor.forClass(List.class);
        verify(personRepository).saveAll(captor.capture());

        List<PersonModel> saved = captor.getValue();
        assertThat(saved).hasSize(2);
        assertThat(saved).extracting(PersonModel::getFirstName)
                .containsExactlyInAnyOrder("Samy", "Cons");

    }

    /**
     * Vérifie que le service lève une exception lorsque l'on tente d'ajouter une personne déjà existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void addPerson_WhenAlreadyExists_shouldThrowIllegalArgumentException() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        PersonModel duplicate = new PersonModel();
        duplicate.setFirstName("Samy");
        duplicate.setLastName("Ymas");

        assertThatThrownBy(() -> personService.addPerson(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Person already exists");

        verify(personRepository).findAll();
        verify(personRepository, never()).saveAll(any());
    }

    /**
     * Vérifie qu'une personne existante est correctement mise à jour.
     *
     * @throws Exception en caas d'erreur lors de l'exécution du test.
     */
    @Test
    void updatePerson_whenPersonExists_shouldReturnUpdatedPerson() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        PersonModel updated = new PersonModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setAddress("99 Paris");
        updated.setCity("Paris");
        updated.setZip("99000");
        updated.setPhone("111-222-333");
        updated.setEmail("SamyNew@mail.com");

        PersonModel result = personService.updatePerson(updated);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Samy");
        assertThat(result.getLastName()).isEqualTo("Ymas");
        assertThat(result.getAddress()).isEqualTo("99 Paris");
        assertThat(result.getCity()).isEqualTo("Paris");
        assertThat(result.getZip()).isEqualTo("99000");
        assertThat(result.getPhone()).isEqualTo("111-222-333");
        assertThat(result.getEmail()).isEqualTo("SamyNew@mail.com");

        verify(personRepository).saveAll(persons);
    }

    /**
     * Vérifie que la méthode retourne {@code null}
     * lorsqu’aucune personne correspondante n’est trouvée lors d’une mise à jour.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void updatePerson_whenNoMatching_shouldReturnNull() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        PersonModel updated = new PersonModel();
        updated.setFirstName("X");
        updated.setLastName("Y");
        updated.setAddress("Z");

        PersonModel result = personService.updatePerson(updated);

        assertThat(result).isNull();
        verify(personRepository, never()).saveAll(any());
    }

    /**
     * Vérifie qu'une personne est correctement supprimée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deletePerson_whenMatch_shouldDeleteAndSave() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        boolean removed = personService.deletePerson("Samy", "Ymas");

        assertThat(removed).isTrue();
        assertThat(persons).isEmpty();

        verify(personRepository).saveAll(persons);
    }

    /**
     * Vérifie que la suppression retourne {@code false}
     * lorsqu’aucune personne correspondante n’est trouvée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test.
     */
    @Test
    void deletePerson_whenNoMatching_shouldReturnFalse() throws Exception {
        when(personRepository.findAll()).thenReturn(persons);

        boolean removed = personService.deletePerson("No", "One");

        assertThat(removed).isFalse();
        assertThat(persons).hasSize(1);
        verify(personRepository, never()).saveAll(any());
    }
}

package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test du {@link PersonController}
 * <p>
 * Cette classe vérifie le bon fonctionnement des endpoints CRUD exposés par l'URL /person
 */
@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private PersonService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private PersonController controller;
    /**
     * Outil permettant de simuler les requêtes HTTP
     */
    private MockMvc mockMvc;
    /**
     * Outil permettant de convertir un modèle en chaîne JSON
     */
    private ObjectMapper mapper;

    /**
     * Initialise l'environnement de test avant chaque exécution.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    /**
     * Vérifie que l'endpoint GET /person retourne correctement la liste des personnes lorsque le service renvoie des données valides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getAllPersons_shouldReturnListOfPersons() throws Exception {
        //GIVEN: données du test
        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");

        PersonModel p2 = new PersonModel();
        p2.setFirstName("Cons");
        p2.setLastName("Snoc");
        p2.setAddress("77 Paris");
        p2.setPhone("111-222-333");

        List<PersonModel> persons = List.of(p1, p2);

        when(service.getAllPersons()).thenReturn(persons);

        //WHEN+THEN
        mockMvc.perform(get("/person")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Samy"))
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].phone").value("123-456-789"));
    }

    /**
     * Vérifie que l'endpoint GET /person retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getAllPersons_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.getAllPersons()).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/person"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint POST /person ajoute correctement une personne
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void addPerson_shouldReturnCreatedPerson() throws Exception {
        //GIVEN
        PersonModel person = new PersonModel();
        person.setFirstName("Samy");
        person.setLastName("Ymas");
        person.setAddress("77 Paris");
        person.setPhone("123-456-789");

        when(service.addPerson(any(PersonModel.class))).thenReturn(person);


        String json = mapper.writeValueAsString(person);

        //WHEN+THEN
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.address").value("77 Paris"))
                .andExpect(jsonPath("$.phone").value("123-456-789"));
    }

    /**
     * Vérifie que l'endpoint POST /person retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void addPerson_serviceThrowsIOException_shouldReturn500() throws Exception {
        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");
        when(service.addPerson(any(PersonModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(p1);

        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint PUT /person met à jour correctement une personne existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updatePerson_shouldReturnUpdatedPerson() throws Exception {
        //GIVEN
        PersonModel updated = new PersonModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setAddress("77 Paris");
        updated.setPhone("123-456-789");

        when(service.updatePerson(any(PersonModel.class))).thenReturn(updated);

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.address").value("77 Paris"))
                .andExpect(jsonPath("$.phone").value("123-456-789"));
    }

    /**
     * Vérifie que l'endpoint PUT /person retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updatePerson_serviceThrowsIOException_shouldReturn500() throws Exception {
        //GIVEN
        PersonModel updated = new PersonModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setAddress("77 Paris");
        updated.setPhone("123-456-789");

        when(service.updatePerson(any(PersonModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint DELETE /person supprime correctement une personne existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deletePerson_whenRemovedTrue_shouldReturnTrue() throws Exception {
        when(service.deletePerson("Samy", "Ymas")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Samy")
                        .param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Vérifie que l'endpoint DELETE /person retourne false lorsque la suppression ne correspond à aucune personne.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deletePerson_whenRemovedFalse_shouldReturnFalse() throws Exception {
        when(service.deletePerson("Samy", "Ymas")).thenReturn(false);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Samy")
                        .param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Vérifie que l'endpoint DELETE /person retourne 400 lorsque la personne à supprimer n'est pas renseignée
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deletePerson_missingParam_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/person"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Vérifie que l'endpoint DELETE /person retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deletePerson_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.deletePerson("Samy", "Ymas")).thenThrow(new IOException("boom"));

        mockMvc.perform(delete("/person").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}

package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
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
 * Classe de test du {@link MedicalrecordController}
 * <p>
 * Cette classe vérifie le bon fonctionnement des endpoints CRUD exposés par l'URL /medicalRecord
 */
@ExtendWith(MockitoExtension.class)
public class MedicalrecordControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private MedicalrecordService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private MedicalrecordController controller;
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
     * Vérifie que l'endpoint GET /medicalRecord retourne correctement la liste des dossiers médicaux lorsque le service renvoie des données valides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getAllMedicalrecord_shouldReturnListOfMedicalrecords() throws Exception {
        //GIVEN: données du test
        MedicalrecordModel mr1 = new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        MedicalrecordModel mr2 = new MedicalrecordModel();
        mr2.setFirstName("Cons");
        mr2.setLastName("Snoc");
        mr2.setBirthdate("03/06/1990");
        mr2.setMedications(List.of("medication:100mg"));
        mr2.setAllergies(List.of("fish"));

        List<MedicalrecordModel> medicalrecords = List.of(mr1, mr2);

        when(service.getAllMedicalrecord()).thenReturn(medicalrecords);

        //WHEN+THEN
        mockMvc.perform(get("/medicalRecord")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Samy"))
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$[0].medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$[0].allergies[0]").value("codeine"));
    }

    /**
     * Vérifie que l'endpoint GET /medicalRecord retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getAllMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.getAllMedicalrecord()).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint POST /medicalRecord ajoute correctement un nouveau dossier médical.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void addMedicalrecord_shouldReturnCreatedMedicalrecord() throws Exception {
        //GIVEN: données du test
        MedicalrecordModel mr1 = new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        when(service.addMedicalrecord(any(MedicalrecordModel.class))).thenReturn(mr1);

        String json = mapper.writeValueAsString(mr1);

        //WHEN+THEN
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$.medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$.allergies[0]").value("codeine"));
    }

    /**
     * Vérifie que l'endpoint POST /medicalRecord retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void addMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception {
        MedicalrecordModel mr1 = new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        when(service.addMedicalrecord(any(MedicalrecordModel.class))).thenThrow(new IOException("boom"));
        String json = mapper.writeValueAsString(mr1);

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint PUT /medicalRecord met à jour correctement un dossier médical existant.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updateMedicalrecord_shouldReturnUpdatedMedicalrecord() throws Exception {
        //GIVEN
        MedicalrecordModel updated = new MedicalrecordModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setBirthdate("03/06/1985");
        updated.setMedications(List.of("medication:50mg"));
        updated.setAllergies(List.of("codeine"));

        when(service.updateMedicalrecord(any(MedicalrecordModel.class))).thenReturn(updated);

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$.medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$.allergies[0]").value("codeine"));
    }

    /**
     * Vérifie que l'endpoint PUT /medicalRecord retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updateMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception {
        //GIVEN
        MedicalrecordModel updated = new MedicalrecordModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setBirthdate("03/06/1985");
        updated.setMedications(List.of("medication:50mg"));
        updated.setAllergies(List.of("codeine"));

        when(service.updateMedicalrecord(any(MedicalrecordModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint DELETE /medicalRecord supprime correctement un dossier médical existant.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteMedicalrecord_shouldReturnTrue() throws Exception {
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Vérifie que l'endpoint DELETE /medicalRecord retourne false lorsque la suppression ne correspond à aucun dossier médical.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteMedicalrecord_shouldReturnFalse() throws Exception {
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Vérifie que l'endpoint DELETE /medicalRecord retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenThrow(new IOException("boom"));

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

}
